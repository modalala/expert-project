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
    if (!wsUrl) { console.log('No page'); return; }
    
    const ws = new WebSocket(wsUrl);
    const pages = [
        { url: '/expert/review', name: '专家初审' },
        { url: '/expert/master', name: '专家主数据' },
        { url: '/extraction/plan', name: '采购方案单' },
        { url: '/bid/committee', name: '评标委员会' },
        { url: '/system/role', name: '角色管理' }
    ];
    
    let idx = 0;
    
    ws.on('open', () => {
        console.log('=== 前端页面功能测试 ===\n');
        ws.send(JSON.stringify({id: 100, method: 'Runtime.evaluate', params: {
            expression: 'window.location.href = "' + pages[0].url + '"'
        }}));
    });
    
    ws.on('message', msg => {
        const obj = JSON.parse(msg.toString());
        
        if (obj.id === 100 + idx * 2 && obj.result) {
            setTimeout(() => {
                ws.send(JSON.stringify({id: 100 + idx * 2 + 1, method: 'Runtime.evaluate', params: {
                    expression: 'JSON.stringify({url:location.href,title:document.title,rows:document.querySelectorAll(".el-table__row").length,empty:document.body.innerHTML.includes("功能开发中")})'
                }}));
            }, 2000);
        }
        
        if (obj.id === 100 + idx * 2 + 1 && obj.result?.result?.value) {
            const d = JSON.parse(obj.result.result.value);
            const page = pages[idx];
            console.log(`${idx + 1}. ${page.name}`);
            console.log(`   URL: ${d.url}`);
            console.log(`   标题: ${d.title}`);
            console.log(`   数据行: ${d.rows}`);
            console.log(`   功能开发中: ${d.empty ? '❌' : '✅ 无'}`);
            console.log('');
            
            idx++;
            if (idx < pages.length) {
                ws.send(JSON.stringify({id: 100 + idx * 2, method: 'Runtime.evaluate', params: {
                    expression: 'window.location.href = "' + pages[idx].url + '"'
                }}));
            } else {
                console.log('=== 测试完成 ===');
                ws.close();
            }
        }
    });
    
    setTimeout(() => ws.close(), 30000);
}

run();
