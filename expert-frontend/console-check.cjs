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
    const ws = new WebSocket(`ws://localhost:9224/devtools/page/${pageId}`);
    
    ws.on('open', () => {
        // 启用console API
        ws.send(JSON.stringify({id: 1, method: 'Runtime.enable'}));
        
        // 检查页面状态
        ws.send(JSON.stringify({
            id: 2, method: 'Runtime.evaluate', params: {
                expression: `
                    JSON.stringify({
                        url: window.location.href,
                        hasVue: typeof Vue !== 'undefined' || document.querySelector('[data-v-app]') !== null,
                        appContent: document.querySelector('#app')?.innerHTML?.substring(0, 100) || 'empty',
                        hasToken: localStorage.getItem('token') ? 'yes' : 'no',
                        errors: window.__lastError || 'none'
                    })
                `,
                returnByValue: true
            }
        }));
    });
    
    ws.on('message', (data) => {
        const msg = JSON.parse(data.toString());
        
        if (msg.id === 2 && msg.result) {
            console.log('=== 页面状态检查 ===');
            const info = JSON.parse(msg.result.result.value);
            console.log('URL:', info.url);
            console.log('Has Vue App:', info.hasVue);
            console.log('App Content:', info.appContent);
            console.log('Has Token:', info.hasToken);
            
            // 检查是否有Vue实例
            ws.send(JSON.stringify({
                id: 3, method: 'Runtime.evaluate', params: {
                    expression: `
                        const app = document.querySelector('#app');
                        if (app && app.__vue_app__) {
                            'Vue app exists';
                        } else if (app && app._vnode) {
                            'Vue 3 vnode exists';
                        } else {
                            'No Vue instance found';
                        }
                    `
                }
            }));
        }
        
        if (msg.id === 3 && msg.result) {
            console.log('Vue状态:', msg.result.result.value);
            
            // 尝试刷新页面
            ws.send(JSON.stringify({
                id: 4, method: 'Runtime.evaluate', params: {
                    expression: `window.location.reload()`
                }
            }));
        }
        
        if (msg.id === 4) {
            console.log('页面已刷新，等待重新加载...');
            setTimeout(() => {
                ws.send(JSON.stringify({
                    id: 5, method: 'Runtime.evaluate', params: {
                        expression: `document.body.innerHTML.substring(0, 500)`
                    }
                }));
            }, 5000);
        }
        
        if (msg.id === 5 && msg.result) {
            console.log('\n刷新后页面内容:');
            console.log(msg.result.result.value);
            ws.close();
        }
        
        // 监听console消息
        if (msg.method === 'Runtime.consoleAPICalled') {
            console.log('Console:', msg.params?.args?.[0]?.value || msg.params);
        }
    });
    
    setTimeout(() => ws.close(), 20000);
}

runTest();
