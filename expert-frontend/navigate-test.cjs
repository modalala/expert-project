const WebSocket = require('ws');
const http = require('http');

http.get('http://localhost:9224/json', res => {
    let data = '';
    res.on('data', chunk => data += chunk);
    res.on('end', () => {
        const pages = JSON.parse(data);
        const mainPage = pages.find(p => p.url.includes('localhost:5176') && !p.url.includes('devtools'));
        const ws = new WebSocket(mainPage.webSocketDebuggerUrl);
        
        ws.on('open', () => {
            ws.send(JSON.stringify({id: 1, method: 'Runtime.evaluate', params: {
                expression: 'window.location.href = "/expert/master"'
            }}));
        });
        
        ws.on('message', msg => {
            const obj = JSON.parse(msg.toString());
            if (obj.id === 1) {
                setTimeout(() => ws.send(JSON.stringify({id: 2, method: 'Runtime.evaluate', params: {
                    expression: `
                        JSON.stringify({
                            url: window.location.href,
                            title: document.title,
                            rowCount: document.querySelectorAll('.el-table__row').length,
                            hasPlaceholder: document.body.innerHTML.includes('功能开发中')
                        })
                    `
                }})), 3000);
            }
            if (obj.id === 2 && obj.result?.result?.value) {
                console.log('=== 2. 专家主数据页面 ===');
                const d = JSON.parse(obj.result.result.value);
                console.log('URL:', d.url);
                console.log('标题:', d.title);
                console.log('数据行数:', d.rowCount);
                console.log('功能开发中:', d.hasPlaceholder ? '❌' : '✅');
                
                // 测试采购方案单
                ws.send(JSON.stringify({id: 3, method: 'Runtime.evaluate', params: {
                    expression: 'window.location.href = "/extraction/plan"'
                }}));
            }
            if (obj.id === 3) {
                setTimeout(() => ws.send(JSON.stringify({id: 4, method: 'Runtime.evaluate', params: {
                    expression: `
                        JSON.stringify({
                            url: window.location.href,
                            title: document.title,
                            rowCount: document.querySelectorAll('.el-table__row').length,
                            hasPlaceholder: document.body.innerHTML.includes('功能开发中')
                        })
                    `
                }})), 3000);
            }
            if (obj.id === 4 && obj.result?.result?.value) {
                console.log('\n=== 3. 采购方案单页面 ===');
                const d = JSON.parse(obj.result.result.value);
                console.log('URL:', d.url);
                console.log('标题:', d.title);
                console.log('数据行数:', d.rowCount);
                console.log('功能开发中:', d.hasPlaceholder ? '❌' : '✅');
                
                // 测试评标委员会
                ws.send(JSON.stringify({id: 5, method: 'Runtime.evaluate', params: {
                    expression: 'window.location.href = "/bid/committee"'
                }}));
            }
            if (obj.id === 5) {
                setTimeout(() => ws.send(JSON.stringify({id: 6, method: 'Runtime.evaluate', params: {
                    expression: `
                        JSON.stringify({
                            url: window.location.href,
                            title: document.title,
                            rowCount: document.querySelectorAll('.el-table__row').length,
                            hasPlaceholder: document.body.innerHTML.includes('功能开发中')
                        })
                    `
                }})), 3000);
            }
            if (obj.id === 6 && obj.result?.result?.value) {
                console.log('\n=== 4. 评标委员会页面 ===');
                const d = JSON.parse(obj.result.result.value);
                console.log('URL:', d.url);
                console.log('标题:', d.title);
                console.log('数据行数:', d.rowCount);
                console.log('功能开发中:', d.hasPlaceholder ? '❌
