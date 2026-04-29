const WebSocket = require('ws');
const http = require('http');
const fs = require('fs');

http.get('http://localhost:9224/json', (res) => {
    let data = '';
    res.on('data', chunk => data += chunk);
    res.on('end', () => {
        const pages = JSON.parse(data);
        const mainPage = pages.find(p => p.url.includes('localhost:5176') && !p.url.includes('devtools'));
        if (!mainPage) {
            console.log('No page found');
            return;
        }
        
        const ws = new WebSocket(mainPage.webSocketDebuggerUrl);
        
        ws.on('open', () => {
            console.log('=== 截取专家初审页面 ===');
            ws.send(JSON.stringify({id: 1, method: 'Page.captureScreenshot'}));
        });
        
        ws.on('message', (msg) => {
            const obj = JSON.parse(msg.toString());
            
            if (obj.id === 1 && obj.result?.data) {
                fs.writeFileSync('../temp/test-results/expert-review.png', Buffer.from(obj.result.data, 'base64'));
                console.log('✅ 截图1: expert-review.png');
                
                // 导航到专家主数据
                ws.send(JSON.stringify({id: 2, method: 'Runtime.evaluate', params: {
                    expression: 'window.location.href="/expert/master"'
                }}));
            }
            
            if (obj.id === 2) {
                setTimeout(() => {
                    ws.send(JSON.stringify({id: 3, method: 'Page.captureScreenshot'}));
                }, 3000);
            }
            
            if (obj.id === 3 && obj.result?.data) {
                fs.writeFileSync('../temp/test-results/expert-master.png', Buffer.from(obj.result.data, 'base64'));
                console.log('✅ 截图2: expert-master.png');
                
                // 导航到采购方案单
                ws.send(JSON.stringify({id: 4, method: 'Runtime.evaluate', params: {
                    expression: 'window.location.href="/extraction/plan"'
                }}));
            }
            
            if (obj.id === 4) {
                setTimeout(() => {
                    ws.send(JSON.stringify({id: 5, method: 'Page.captureScreenshot'}));
                }, 3000);
            }
            
            if (obj.id === 5 && obj.result?.data) {
                fs.writeFileSync('../temp/test-results/extraction-plan.png', Buffer.from(obj.result.data, 'base64'));
                console.log('✅ 截图3: extraction-plan.png');
                ws.close();
            }
        });
        
        setTimeout(() => ws.close(), 20000);
    });
});
