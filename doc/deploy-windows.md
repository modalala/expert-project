# Windows Server 2022 部署指南

## 一、环境准备

### 1.1 安装JDK 17

1. 下载JDK 17：https://adoptium.net/temurin/releases/?version=17
2. 选择Windows x64版本下载安装
3. 配置环境变量：
   - `JAVA_HOME`: `C:\Program Files\Eclipse Adoptium\jdk-17`
   - Path添加: `%JAVA_HOME%\bin`

验证：
```powershell
java -version
```

### 1.2 安装MySQL 8.0

1. 下载MySQL：https://dev.mysql.com/downloads/installer/
2. 安装MySQL Server 8.0
3. 设置root密码
4. 创建数据库：
```sql
CREATE DATABASE expert_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'expert'@'localhost' IDENTIFIED BY 'Expert@123';
GRANT ALL PRIVILEGES ON expert_db.* TO 'expert'@'localhost';
FLUSH PRIVILEGES;
```

### 1.3 安装Nginx (Windows版)

1. 下载：http://nginx.org/en/download.html (Stable version -> nginx/Windows)
2. 解压到 `C:\nginx`
3. 启动测试：
```powershell
cd C:\nginx
start nginx
```

---

## 二、目录结构

建议创建以下目录：

```
C:\expert\
├── backend\           # 后端应用
│   ├── expert-backend-1.0.0.jar
│   ├── application-prod.yml
│   └── logs\
├── frontend\          # 前端静态文件
│   └── dist\
├── sql\               # 数据库脚本
│   ├── init_schema.sql
│   └── init_data.sql
└── scripts\           # 启动脚本
    ├── start-backend.ps1
    └── stop-backend.ps1
```

---

## 三、后端部署

### 3.1 修改生产配置

创建 `C:\expert\backend\application-prod.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/expert_db?useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: expert
    password: Expert@123
    driver-class-name: com.mysql.cj.jdbc.Driver

jwt:
  secret: your-production-secret-key-at-least-256-bits-long-for-security
  expiration: 86400000

server:
  port: 8080

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
```

### 3.2 本地打包

在你的开发机器上：
```powershell
cd D:\code\expert-project\expert-backend

# 使用Maven打包
$env:JAVA_HOME = "D:\JDK\jdk-17.0.14+7"
mvn clean package -DskipTests

# 生成的jar包: target\expert-backend-1.0.0.jar
```

### 3.3 上传到服务器

将以下文件上传到服务器 `C:\expert\backend\`：
- `expert-backend-1.0.0.jar`
- `application-prod.yml`

将SQL文件上传到 `C:\expert\sql\`：
- `init_schema.sql`
- `init_data.sql`

### 3.4 创建启动脚本

创建 `C:\expert\scripts\start-backend.ps1`：

```powershell
# 后端启动脚本
$jarPath = "C:\expert\backend\expert-backend-1.0.0.jar"
$configPath = "C:\expert\backend\application-prod.yml"
$logPath = "C:\expert\backend\logs\app.log"

# 检查是否已运行
$process = Get-Process -Name java -ErrorAction SilentlyContinue
if ($process) {
    Write-Host "Backend already running, PID: $($process.Id)"
    exit
}

# 启动服务
Start-Process -FilePath "java" -ArgumentList "-Xms512m", "-Xmx1024m", "-jar", $jarPath, "--spring.config.location=$configPath" -RedirectStandardOutput $logPath -RedirectStandardError "$logPath.err" -NoNewWindow

Write-Host "Backend started"
```

### 3.5 创建停止脚本

创建 `C:\expert\scripts\stop-backend.ps1`：

```powershell
# 后端停止脚本
$process = Get-Process -Name java -ErrorAction SilentlyContinue
if ($process) {
    Stop-Process -Id $process.Id -Force
    Write-Host "Backend stopped, PID: $($process.Id)"
} else {
    Write-Host "Backend not running"
}
```

### 3.6 启动后端

```powershell
# 执行启动脚本
cd C:\expert\scripts
.\start-backend.ps1

# 查看日志
Get-Content C:\expert\backend\logs\app.log -Tail 50
```

---

## 四、前端部署

### 4.1 本地构建

在你的开发机器上：
```powershell
cd D:\code\expert-project\expert-frontend

# 创建生产环境配置
Set-Content -Path .env.production -Value "VITE_API_BASE_URL=/api"

# 构建
npm run build

# 生成的静态文件: dist\
```

### 4.2 上传到服务器

将 `dist\` 目录下所有文件上传到服务器 `C:\expert\frontend\`

---

## 五、Nginx配置

### 5.1 编辑配置文件

编辑 `C:\nginx\conf\nginx.conf`：

```nginx
worker_processes  1;

events {
    worker_connections  1024;
}

http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;

    # Gzip压缩
    gzip  on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml;

    server {
        listen       80;
        server_name  localhost;

        # 前端静态文件
        root   C:/expert/frontend;
        index  index.html;

        # Vue Router history模式
        location / {
            try_files $uri $uri/ /index.html;
        }

        # 后端API代理
        location /api {
            proxy_pass http://127.0.0.1:8080;
            proxy_set_header Host $host;
            proxy_set_header X-Real-IP $remote_addr;
            proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
            proxy_connect_timeout 60s;
            proxy_send_timeout 60s;
            proxy_read_timeout 60s;
        }

        # 静态资源缓存
        location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)$ {
            expires 30d;
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }
}
```

### 5.2 重载Nginx

```powershell
cd C:\nginx
nginx -t          # 测试配置
nginx -s reload   # 重载配置
```

### 5.3 设置Nginx开机自启

方法一：使用任务计划程序

1. 打开"任务计划程序"
2. 创建基本任务 -> 名称: "Nginx"
3. 触发器: "计算机启动时"
4. 操作: "启动程序" -> `C:\nginx\nginx.exe`
5. 完成

方法二：使用NSSM工具

```powershell
# 下载NSSM: https://nssm.cc/download
# 解压后执行
nssm install nginx C:\nginx\nginx.exe
nssm start nginx
```

---

## 六、数据库初始化

### 6.1 导入SQL

```powershell
# 使用MySQL命令行
cd C:\expert\sql

# 方式1: MySQL命令行
mysql -u expert -p expert_db < init_schema.sql
mysql -u expert -p expert_db < init_data.sql

# 方式2: PowerShell调用
& "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u expert -pExpert@123 expert_db -e "source init_schema.sql"
& "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u expert -pExpert@123 expert_db -e "source init_data.sql"
```

---

## 七、防火墙配置

### 7.1 开放端口

```powershell
# 开放HTTP端口
New-NetFirewallRule -DisplayName "HTTP" -Direction Inbound -Port 80 -Protocol TCP -Action Allow

# 开放HTTPS端口
New-NetFirewallRule -DisplayName "HTTPS" -Direction Inbound -Port 443 -Protocol TCP -Action Allow

# 查看规则
Get-NetFirewallRule | Where-Object {$_.DisplayName -like "*HTTP*"}
```

---

## 八、设置后端开机自启

### 8.1 使用NSSM安装服务

```powershell
# 下载NSSM并解压
# 安装服务
nssm install ExpertBackend
# 在GUI中设置:
# - Path: java.exe的完整路径
# - Arguments: -Xms512m -Xmx1024m -jar C:\expert\backend\expert-backend-1.0.0.jar --spring.config.location=C:\expert\backend\application-prod.yml
# - Startup directory: C:\expert\backend

# 启动服务
nssm start ExpertBackend
```

---

## 九、验证部署

### 9.1 检查服务状态

```powershell
# 检查后端
curl http://localhost:8080/api/test/db-conn

# 检查Nginx代理
curl http://localhost/api/test/db-conn

# 检查前端
curl http://localhost/
```

### 9.2 访问测试

浏览器访问：http://<服务器IP>

登录账号：admin / Admin@123

---

## 十、部署检查清单

| 检查项 | 命令/操作 |
|--------|-----------|
| JDK安装 | `java -version` |
| MySQL运行 | 服务管理器检查MySQL80服务 |
| 数据库初始化 | `mysql -u expert -p -e "SHOW TABLES FROM expert_db"` |
| 后端运行 | `Get-Process java` |
| 后端API | `curl http://localhost:8080/api/test/db-conn` |
| Nginx运行 | `Get-Process nginx` |
| 前端页面 | 浏览器访问 http://localhost |

---

## 十一、常用运维命令

```powershell
# 查看后端日志
Get-Content C:\expert\backend\logs\app.log -Tail 100

# 重启后端
cd C:\expert\scripts
.\stop-backend.ps1
.\start-backend.ps1

# 重载Nginx
nginx -s reload

# 查看端口占用
netstat -ano | findstr :8080
netstat -ano | findstr :80

# 查看进程
Get-Process java
Get-Process nginx
```

---

## 十二、腾讯云安全组

在腾讯云控制台配置安全组入站规则：

| 协议 | 端口 | 来源 | 说明 |
|------|------|------|------|
| TCP | 22 | 你的IP | SSH管理 |
| TCP | 80 | 0.0.0.0/0 | HTTP |
| TCP | 443 | 0.0.0.0/0 | HTTPS |
| TCP | 3389 | 你的IP | 远程桌面 |

---

## 十三、一键部署脚本

创建 `C:\expert\scripts\deploy.ps1`：

```powershell
<#
.SYNOPSIS
    专家库管理系统一键部署脚本
#>

Write-Host "=== 专家库管理系统部署 ===" -ForegroundColor Green

# 1. 检查环境
Write-Host "检查环境..." -ForegroundColor Yellow
if (-not (Get-Command java -ErrorAction SilentlyContinue)) {
    Write-Host "错误: JDK未安装" -ForegroundColor Red
    exit 1
}
if (-not (Get-Service MySQL80 -ErrorAction SilentlyContinue)) {
    Write-Host "错误: MySQL未运行" -ForegroundColor Red
    exit 1
}

# 2. 初始化数据库
Write-Host "初始化数据库..." -ForegroundColor Yellow
$mysqlPath = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
& $mysqlPath -u expert -pExpert@123 expert_db -e "source C:\expert\sql\init_schema.sql"
& $mysqlPath -u expert -pExpert@123 expert_db -e "source C:\expert\sql\init_data.sql"

# 3. 启动后端
Write-Host "启动后端..." -ForegroundColor Yellow
& C:\expert\scripts\stop-backend.ps1
Start-Sleep -Seconds 2
& C:\expert\scripts\start-backend.ps1
Start-Sleep -Seconds 10

# 4. 重载Nginx
Write-Host "重载Nginx..." -ForegroundColor Yellow
cd C:\nginx
nginx -s reload

# 5. 验证
Write-Host "验证部署..." -ForegroundColor Yellow
$apiTest = Invoke-RestMethod -Uri "http://localhost/api/test/db-conn" -Method Get
if ($apiTest.code -eq 200) {
    Write-Host "部署成功！" -ForegroundColor Green
} else {
    Write-Host "API测试失败" -ForegroundColor Red
}

Write-Host "=== 部署完成 ===" -ForegroundColor Green
Write-Host "访问地址: http://localhost" -ForegroundColor Cyan
Write-Host "登录账号: admin / Admin@123" -ForegroundColor Cyan
```

---

## 十四、备份策略

### 14.1 数据库备份脚本

创建 `C:\expert\scripts\backup-db.ps1`：

```powershell
$backupPath = "C:\expert\backup"
$date = Get-Date -Format "yyyyMMdd_HHmmss"
$backupFile = "$backupPath\expert_db_$date.sql"

# 创建备份目录
New-Item -ItemType Directory -Force -Path $backupPath

# 执行备份
$mysqldump = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqldump.exe"
& $mysqldump -u expert -pExpert@123 expert_db > $backupFile

Write-Host "数据库备份完成: $backupFile"
```

设置每天定时备份（任务计划程序）。