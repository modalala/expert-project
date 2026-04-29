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
        ws.send(JSON.stringify({id: 1, method: 'Runtime.enable'}));
        
        console.log('=== 监听控制台消息 ===\n');
        ws.send(JSON.stringify({id: 2, method: 'Runtime.evaluate', params: {
            expression: 'window.location.href = "/expert/review"'
        }}));
    });
    
    ws.on('message', msg => {
        const obj = JSON.parse(msg.toString());
        
        // 监听所有console
        if (obj.method === 'Runtime.consoleAPICalled') {
            const args = obj.params?.args || [];
            args.forEach(arg => {
                if (arg.type === 'string') console.log('Log:', arg.value);
                if (arg.subtype === 'error') console.log('Error:', arg.description?.substring(0, 200));
                if (arg.type === 'object' && !arg.subtype) console.log('Object:', JSON.stringify(arg.preview?.properties || []).substring(0, 100));
            });
        }
        
        if (obj.id === 2) {
            setTimeout(() => {
                ws.send(JSON.stringify({id: 3, method: 'Runtime.evaluate', params: {
                    expression: `
                        // 检查localStorage token
                        const token = localStorage.getItem('token');
                        
                        // 手动测试API
                        fetch('/api/review/list?reviewType=INIT&page=1&size=10', {
                            headers: { 'Authorization': 'Bearer ' + (token || '') }
                        })
                        .then(r => r.json())
                        .then(d => {
                            console.log('API Response:', JSON.stringify(d));
                            return d;
                        })
                        .catch(e => console.log('API Error:', e.message));
                        
                        'API test started'
                    `
                }}));
            }, 3000);
        }
        
        if (obj.id === 3 && obj.result) {
            console.log('\n' + obj.result.result.value);
        }
    });
    
    setTimeout(() => {
        console.log('\n=== 检查完成 ===');
        ws.close();
    }, 15000);
}

run();
