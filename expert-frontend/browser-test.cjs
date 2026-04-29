const WebSocket = require('ws');
const fs = require('fs');

const wsUrl = 'ws://localhost:9224/devtools/page/D3E07CD0B6EF5D8355E1EE2F63801D68';

console.log('Connecting to:', wsUrl);

const ws = new WebSocket(wsUrl);

ws.on('open', () => {
    console.log('WebSocket connected');
    ws.send(JSON.stringify({id: 1, method: 'Page.captureScreenshot', params: {format: 'png'}}));
});

ws.on('message', (data) => {
    const msg = JSON.parse(data.toString());
    
    if (msg.id === 1 && msg.result && msg.result.data) {
        console.log('Screenshot captured');
        const buffer = Buffer.from(msg.result.data, 'base64');
        fs.writeFileSync('../temp/test-results/login-page.png', buffer);
        console.log('Screenshot saved');
        
        ws.send(JSON.stringify({id: 2, method: 'Runtime.evaluate', params: {
            expression: 'document.body.innerHTML.substring(0, 1500)'
        }}));
    }
    
    if (msg.id === 2 && msg.result && msg.result.result) {
        console.log('\n=== 页面内容 ===');
        console.log(msg.result.result.value);
        
        ws.send(JSON.stringify({id: 3, method: 'Runtime.evaluate', params: {
            expression: 'JSON.stringify(Array.from(document.querySelectorAll("input, button")).map(e => ({tag: e.tagName, type: e.type, text: e.innerText || e.placeholder, id: e.id})))'
        }}));
    }
    
    if (msg.id === 3 && msg.result && msg.result.result) {
        console.log('\n=== 表单元素 ===');
        console.log(msg.result.result.value);
        ws.close();
    }
});

ws.on('error', (err) => {
    console.error('WebSocket error:', err.message);
});

setTimeout(() => {
    console.log('Timeout');
    ws.close();
}, 10000);
