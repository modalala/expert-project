const WebSocket = require('ws');
const http = require('http');

function getPages() {
    return new Promise(resolve => {
        http.get('http://localhost:9224/json', res => {
            let data = '';
            res.on('data', chunk => data += chunk);
            res.on('end', () => resolve(JSON.parse(data)));
        });
    });
}

async function test() {
    const pages = await getPages();
    const mainPage = pages.find(p => p.url.includes('localhost:5176') && !p.url.includes('devtools'));
    
    const ws = new WebSocket(mainPage.webSocketDebuggerUrl);
    
    ws.on('open', () => {
        ws.send(JSON.stringify({id: 1, method: 'Runtime.evaluate', params: {
            expression: `
                const results = {};
                
                // 检查当前页面
                results.url = window.location.href;
                results.title = document.title;
                
                // 检查表格
                const tables = document.querySelectorAll('.el-table');
                const rows = document.querySelectorAll('.el-table__row');
                results.tableCount = tables.length;
                results.rowCount = rows.length;
                
                // 获取表格内容
                if (rows.length > 0) {
                    const data = [];
                    rows.forEach(r => {
                        const cells = r.querySelectorAll('.el-table__cell');
                        const rowData = [];
                        cells.forEach(c => rowData.push(c.innerText?.trim() || ''));
                        if (rowData.some(d => d)) data.push(rowData.slice(0, 5));
                    });
                    results.tableData = data.slice(0, 5);
                }
                
                // 检查搜索表单
                results.hasSearchForm = document.querySelector('.search-form, .el-form.inline') !== null;
                
                // 检查是否有"功能开发中"
                results.hasPlaceholder = document.body.innerHTML.includes('功能开发中') || document.body.innerHTML.includes('el-empty');
                
                JSON.stringify(results);
            `
        }}));
    });
    
    ws.on('message', msg => {
        const obj = JSON.parse(msg.toString());
        if (obj.id === 1 && obj.result?.result?.value) {
            console.log('\n=== 页面验证结果 ===');
            const data = JSON.parse(obj.result.result.value);
            console.log('URL:', data.url);
            console.log('标题:', data.title);
            console.log('表格数:', data.tableCount);
            console.log('数据行数:', data.rowCount);
            console.log('搜索表单:', data.hasSearchForm ? '✅' : '❌');
            console.log('功能开发中:', data.hasPlaceholder ? '❌ 存在' : '✅ 无');
            
            if (data.tableData && data.tableData.length > 0) {
                console.log('\n表格数据:');
                data.tableData.forEach((row, i) => {
                    console.log(`  行${i+1}: ${row.join(' | ')}`);
                });
            }
            
            ws.close();
        }
    });
    
    setTimeout(() => ws.close(), 10000);
}

test();
