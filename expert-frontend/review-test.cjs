const WebSocket = require('ws');
const fs = require('fs');
const http = require('http');

function getMainPageId() {
    return new Promise((resolve, reject) => {
        http.get('http://localhost:9224/json', (res) => {
            let data = '';
            res.on('data', chunk => data += chunk);
            res.on('end', () => {
                const pages = JSON.parse(data);
                const mainPage = pages.find(p => p.url.includes('localhost:5176') && !p.url.includes('devtools'));
                resolve(mainPage ? mainPage.id : null);
            });
        }).on('error', reject);
    });
}

async function runTest() {
    const pageId = await getMainPageId();
    console.log('Page ID:', pageId);
    
    const wsUrl = `ws://localhost:9224/devtools/page/${pageId}`;
    const ws = new WebSocket(wsUrl);
    
    ws.on('open', () => {
        console.log('\n=== 导航到专家初审页面 ===');
        ws.send(JSON.stringify({
            id: 1, method: 'Runtime.evaluate', params: {
                expression: `window.location.href = '/expert/review'`
            }
        }));
    });
    
    ws.on('message', (data) => {
        const msg = JSON.parse(data.toString());
        
        if (msg.id === 1) {
            console.log('导航命令已执行');
            setTimeout(() => {
                ws.send(JSON.stringify({
                    id: 2, method: 'Runtime.evaluate', params: {
                        expression: `window.location.href`
                    }
                }));
            }, 2000);
        }
        
        if (msg.id === 2 && msg.result) {
            console.log('当前URL:', msg.result.result.value);
            
            // 截图
            ws.send(JSON.stringify({
                id: 3, method: 'Page.captureScreenshot', params: {format: 'png'}
            }));
        }
        
        if (msg.id === 3 && msg.result && msg.result.data) {
            const buffer = Buffer.from(msg.result.data, 'base64');
            fs.writeFileSync('../temp/test-results/review-page.png', buffer);
            console.log('截图保存: review-page.png');
            
            // 检查页面内容
            ws.send(JSON.stringify({
                id: 4, method: 'Runtime.evaluate', params: {
                    expression: `document.body.innerHTML`
                }
            }));
        }
        
        if (msg.id === 4 && msg.result) {
            const html = msg.result.result.value;
            console.log('\n=== 专家初审页面检查 ===');
            
            // 检查关键元素
            const checks = {
                '页面标题': html.includes('专家初审') || html.includes('Review'),
                '表格元素': html.includes('el-table') || html.includes('table'),
                '搜索表单': html.includes('el-form') || html.includes('form'),
                '数据列表': html.includes('el-table-column') || html.includes('Wangwu') || html.includes('Sunli'),
                '无"功能开发中"': !html.includes('功能开发中') && !html.includes('el-empty description')
            };
            
            for (const [name, passed] of Object.entries(checks)) {
                console.log(`${passed ? '✅' : '❌'} ${name}`);
            }
            
            // 显示页面片段
            console.log('\n=== 页面HTML片段 (前1000字符) ===');
            console.log(html.substring(0, 1000));
            
            ws.close();
        }
    });
    
    ws.on('error', (err) => console.error('Error:', err.message));
    setTimeout(() => ws.close(), 15000);
}

runTest();
