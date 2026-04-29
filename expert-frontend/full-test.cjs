const WebSocket = require('ws');
const http = require('http');
const fs = require('fs');

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
    const ws = new WebSocket(`ws://localhost:9224/devtools/page/${pageId}`);
    
    ws.on('open', () => {
        console.log('=== 专家初审页面完整测试 ===');
        // 等待数据加载
        setTimeout(() => {
            ws.send(JSON.stringify({id: 1, method: 'Runtime.evaluate', params: {
                expression: `
                    const rows = document.querySelectorAll('.el-table__row');
                    const loading = document.querySelector('.el-loading-mask');
                    const tableData = [];
                    rows.forEach(r => {
                        const cells = r.querySelectorAll('.el-table__cell');
                        if (cells.length > 0) {
                            tableData.push(cells[0]?.innerText || '');
                        }
                    });
                    JSON.stringify({
                        rowCount: rows.length,
                        isLoading: loading !== null,
                        names: tableData.slice(0, 5),
                        fullHtml: document.body.innerHTML.includes('Wangwu') || document.body.innerHTML.includes('Sunli')
                    })
                `
            }}));
        }, 3000);
    });
    
    ws.on('message', (data) => {
        const msg = JSON.parse(data.toString());
        
        if (msg.id === 1 && msg.result) {
            const result = JSON.parse(msg.result.result.value);
            console.log('\n数据加载状态:');
            console.log('- 数据行数:', result.rowCount);
            console.log('- 正在加载:', result.isLoading);
            console.log('- 专家姓名:', result.names.join(', ') || '无');
            console.log('- 包含测试数据:', result.fullHtml ? '✅' : '❌');
            
            // 截图
            ws.send(JSON.stringify({id: 2, method: 'Page.captureScreenshot', params: {format: 'png'}}));
        }
        
        if (msg.id === 2 && msg.result && msg.result.data) {
            const buffer = Buffer.from(msg.result.data, 'base64');
            fs.writeFileSync('../temp/test-results/expert-review-final.png', buffer);
            console.log('\n✅ 截图保存: expert-review-final.png');
            
            // 测试其他页面
            ws.send(JSON.stringify({id: 3, method: 'Runtime.evaluate', params: {
                expression: `window.location.href = '/expert/master'`
            }}));
        }
        
        if (msg.id === 3) {
            setTimeout(() => {
                ws.send(JSON.stringify({id: 4, method: 'Runtime.evaluate', params: {
                    expression: `
                        JSON.stringify({
                            url: window.location.href,
                            title: document.title,
                            rowCount: document.querySelectorAll('.el-table__row').length
                        })
                    `
                }}));
            }, 3000);
        }
        
        if (msg.id === 4 && msg.result) {
            const info = JSON.parse(msg.result.result.value);
            console.log('\n=== 专家主数据页面 ===');
            console.log('URL:', info.url);
            console.log('Title:', info.title);
            console.log('数据行数:', info.rowCount);
            
            ws.send(JSON.stringify({id: 5, method: 'Page.captureScreenshot', params: {format: 'png'}}));
        }
        
        if (msg.id === 5 && msg.result && msg.result.data) {
            const buffer = Buffer.from(msg.result.data, 'base64');
            fs.writeFileSync('../temp/test-results/expert-master-final.png', buffer);
            console.log('✅ 截图保存: expert-master-final.png');
            
            ws.close();
        }
    });
    
    setTimeout(() => ws.close(), 25000);
}

runTest();
