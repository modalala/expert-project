const WebSocket = require('ws');
const http = require('http');

function getMainPageId() {
    return new Promise((resolve) => {
        http.get('http://localhost:9224/json', (res) => {
            let data = '';
            res.on('data', chunk => data += chunk);
            res.on('end', () => {
                const pages = JSON.parse(data);
                const mainPage = pages.find(p => p.url.includes('localhost:5176') && !p.url.includes('devtools'));
                resolve(mainPage ? mainPage.id : null);
            });
        });
    });
}

async function runTest() {
    const pageId = await getMainPageId();
    console.log('Page ID:', pageId);
    
    const ws = new WebSocket(`ws://localhost:9224/devtools/page/${pageId}`);
    let step = 0;
    
    ws.on('open', () => {
        console.log('\n=== 刷新页面 ===');
        ws.send(JSON.stringify({id: 1, method: 'Runtime.evaluate', params: {
            expression: `window.location.reload()`
        }}));
    });
    
    ws.on('message', (data) => {
        const msg = JSON.parse(data.toString());
        
        // 监听console
        if (msg.method === 'Runtime.consoleAPICalled') {
            const val = msg.params?.args?.[0]?.value;
            if (val) console.log('Console:', val);
        }
        
        if (msg.id === 1) {
            console.log('等待页面加载...');
            setTimeout(() => {
                ws.send(JSON.stringify({id: 2, method: 'Runtime.evaluate', params: {
                    expression: `
                        JSON.stringify({
                            url: window.location.href,
                            title: document.title,
                            appHtml: document.querySelector('#app')?.innerHTML?.substring(0, 300) || 'empty',
                            hasError: document.body.innerHTML.includes('SyntaxError') || false
                        })
                    `
                }}));
            }, 5000);
        }
        
        if (msg.id === 2 && msg.result) {
            const info = JSON.parse(msg.result.result.value);
            console.log('\n=== 页面状态 ===');
            console.log('URL:', info.url);
            console.log('Title:', info.title);
            console.log('App HTML:', info.appHtml.substring(0, 200));
            
            // 检查专家初审页面数据
            if (info.url.includes('/expert/review')) {
                ws.send(JSON.stringify({id: 3, method: 'Runtime.evaluate', params: {
                    expression: `
                        const tables = document.querySelectorAll('.el-table');
                        const rows = document.querySelectorAll('.el-table__row');
                        JSON.stringify({
                            hasTable: tables.length > 0,
                            rowCount: rows.length,
                            pageContent: document.body.innerHTML.includes('专家初审') || document.body.innerHTML.includes('Wangwu')
                        })
                    `
                }}));
            } else {
                ws.close();
            }
        }
        
        if (msg.id === 3 && msg.result) {
            const result = JSON.parse(msg.result.result.value);
            console.log('\n=== 专家初审页面检查 ===');
            console.log('表格存在:', result.hasTable ? '✅' : '❌');
            console.log('数据行数:', result.rowCount);
            console.log('页面内容正确:', result.pageContent ? '✅' : '❌');
            ws.close();
        }
    });
    
    setTimeout(() => ws.close(), 20000);
}

runTest();
