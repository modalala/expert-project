const WebSocket = require('ws');
const http = require('http');

function getWS() {
    return new Promise(resolve => {
        http.get('http://localhost:9224/json', res => {
            let data = '';
            res.on('data', c => data += c);
            res.on('end', () => {
                const pages = JSON.parse(data);
                const main = pages.find(p => p.url.includes('localhost:5176') && !p.url.includes('devtools'));
                resolve(main ? main.webSocketDebuggerUrl : null);
            });
        });
    });
}

async function run() {
    const wsUrl = await getWS();
    const ws = new WebSocket(wsUrl);
    
    ws.on('open', () => {
        console.log('=== Vue组件状态检查 ===\n');
        ws.send(JSON.stringify({id: 1, method: 'Runtime.evaluate', params: {
            expression: `
                setTimeout(async () => {
                    // 获取Vue实例
                    const app = document.querySelector('#app');
                    
                    // 检查表格数据绑定
                    const tableBody = document.querySelector('.el-table__body-wrapper');
                    const tbody = tableBody?.querySelector('tbody');
                    
                    // 检查是否有v-loading
                    const loadingMask = document.querySelector('.el-loading-mask');
                    
                    // 手动调用API并检查响应
                    const token = localStorage.getItem('token');
                    const resp = await fetch('/api/review/list?reviewType=INIT&page=1&size=10', {
                        headers: { 'Authorization': 'Bearer ' + token }
                    });
                    const json = await resp.json();
                    
                    console.log('Data received:', json.data?.records?.length || 0, 'records');
                    console.log('Table body rows:', tbody?.querySelectorAll('tr')?.length || 0);
                    console.log('Loading active:', loadingMask !== null);
                    
                    // 检查Vue DevTools
                    console.log('Vue app exists:', app?.__vue_app__ !== undefined);
                }, 100);
                'Check started'
            `
        }}));
    });
    
    ws.on('message', msg => {
        const obj = JSON.parse(msg.toString());
        
        if (obj.method === 'Runtime.consoleAPICalled') {
            const val = obj.params?.args?.[0]?.value;
            if (typeof val === 'string') console.log(val);
        }
        
        if (obj.id === 1) {
            setTimeout(() => ws.close(), 5000);
        }
    });
    
    setTimeout(() => ws.close(), 10000);
}

run();
