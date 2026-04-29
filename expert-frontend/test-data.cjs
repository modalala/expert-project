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
        console.log('=== 专家初审页面数据检查 ===\n');
        ws.send(JSON.stringify({id: 1, method: 'Runtime.evaluate', params: {
            expression: 'window.location.href = "/expert/review"'
        }}));
    });
    
    ws.on('message', msg => {
        const obj = JSON.parse(msg.toString());
        
        // Console消息
        if (obj.method === 'Runtime.consoleAPICalled') {
            const val = obj.params?.args?.[0]?.value;
            if (typeof val === 'string') console.log('Console:', val);
            if (obj.params?.args?.[0]?.subtype === 'error') {
                console.log('Console Error:', obj.params.args[0].description?.substring(0, 100));
            }
        }
        
        if (obj.id === 1) {
            // 等待数据加载
            setTimeout(() => {
                ws.send(JSON.stringify({id: 2, method: 'Runtime.evaluate', params: {
                    expression: `
                        // 检查网络请求
                        const perfEntries = performance.getEntriesByType('resource');
                        const apiCalls = perfEntries.filter(e => e.name.includes('/api/'));
                        
                        // 检查表格
                        const table = document.querySelector('.el-table');
                        const rows = document.querySelectorAll('.el-table__row');
                        const loading = document.querySelector('.el-loading-mask');
                        
                        // 检查Vue组件状态
                        const app = document.querySelector('#app');
                        
                        JSON.stringify({
                            apiCalls: apiCalls.map(a => a.name).slice(0, 5),
                            tableExists: table !== null,
                            rowCount: rows.length,
                            isLoading: loading !== null,
                            tableHtml: table ? table.innerHTML.substring(0, 300) : 'no table'
                        })
                    `
                }}));
            }, 5000);
        }
        
        if (obj.id === 2 && obj.result?.result?.value) {
            const d = JSON.parse(obj.result.result.value);
            console.log('API调用:', d.apiCalls.join(', ') || '无');
            console.log('表格存在:', d.tableExists ? '✅' : '❌');
            console.log('数据行数:', d.rowCount);
            console.log('正在加载:', d.isLoading ? '是' : '否');
            console.log('\n表格HTML片段:');
            console.log(d.tableHtml);
            
            ws.close();
        }
    });
    
    setTimeout(() => ws.close(), 15000);
}

run();
