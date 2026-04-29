const WebSocket = require('ws');
const fs = require('fs');

// 获取最新页面ID
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
    if (!pageId) {
        console.log('No main page found');
        return;
    }
    console.log('Page ID:', pageId);
    
    const wsUrl = `ws://localhost:9224/devtools/page/${pageId}`;
    const ws = new WebSocket(wsUrl);
    
    ws.on('open', () => {
        console.log('\n=== 1. 填写登录表单 ===');
        // 填写用户名
        ws.send(JSON.stringify({
            id: 1, method: 'Runtime.evaluate', params: {
                expression: `document.querySelectorAll('input')[0].value = 'admin'`
            }
        }));
    });
    
    ws.on('message', (data) => {
        const msg = JSON.parse(data.toString());
        
        if (msg.id === 1) {
            // 填写密码
            ws.send(JSON.stringify({
                id: 2, method: 'Runtime.evaluate', params: {
                    expression: `document.querySelectorAll('input')[1].value = 'Admin@123'`
                }
            }));
        }
        
        if (msg.id === 2) {
            console.log('用户名和密码已填写');
            // 点击登录按钮
            ws.send(JSON.stringify({
                id: 3, method: 'Runtime.evaluate', params: {
                    expression: `document.querySelector('button').click()`
                }
            }));
        }
        
        if (msg.id === 3) {
            console.log('登录按钮已点击');
            // 等待页面跳转
            setTimeout(() => {
                ws.send(JSON.stringify({
                    id: 4, method: 'Runtime.evaluate', params: {
                        expression: `window.location.href`
                    }
                }));
            }, 3000);
        }
        
        if (msg.id === 4 && msg.result) {
            console.log('当前URL:', msg.result.result.value);
            
            // 截图
            ws.send(JSON.stringify({
                id: 5, method: 'Page.captureScreenshot', params: {format: 'png'}
            }));
        }
        
        if (msg.id === 5 && msg.result && msg.result.data) {
            const buffer = Buffer.from(msg.result.data, 'base64');
            fs.writeFileSync('../temp/test-results/after-login.png', buffer);
            console.log('截图保存: after-login.png');
            
            // 检查页面内容
            ws.send(JSON.stringify({
                id: 6, method: 'Runtime.evaluate', params: {
                    expression: `document.body.innerHTML.substring(0, 2000)`
                }
            }));
        }
        
        if (msg.id === 6 && msg.result) {
            console.log('\n=== 页面内容片段 ===');
            const html = msg.result.result.value;
            // 检查是否包含关键元素
            if (html.includes('dashboard') || html.includes('首页')) {
                console.log('✅ 已跳转到首页');
            }
            console.log(html.substring(0, 500));
            ws.close();
        }
    });
    
    ws.on('error', (err) => {
        console.error('WebSocket error:', err.message);
    });
    
    setTimeout(() => ws.close(), 15000);
}

runTest();
