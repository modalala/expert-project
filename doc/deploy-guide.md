# 腾讯云部署指南

## 一、服务器准备

### 1.1 购买腾讯云CVM

推荐配置：
| 配置项 | 推荐值 | 说明 |
|--------|--------|------|
| CPU | 2核 | 最低配置 |
| 内存 | 4GB | 建议配置 |
| 系统盘 | 50GB SSD | |
| 操作系统 | CentOS 7.9 / Ubuntu 20.04 | 推荐Ubuntu |
| 网络 | 公网IP | 需开放80/443端口 |

### 1.2 安全组配置

开放以下端口：
- **22**: SSH
- **80**: HTTP
- **443**: HTTPS
- **3306**: MySQL（仅内网访问，不建议开放公网）
- **8080**: 后端服务（内网或通过Nginx代理）

---

## 二、环境安装

### 2.1 连接服务器

```bash
ssh root@<服务器公网IP>
```

### 2.2 安装JDK 17

```bash
# Ubuntu
apt update
apt install -y openjdk-17-jdk

# 验证
java -version
```

### 2.3 安装MySQL 8.0

```bash
# Ubuntu
apt install -y mysql-server

# 启动并设置密码
systemctl start mysql
mysql_secure_installation

# 创建数据库和用户
mysql -u root -p
CREATE DATABASE expert_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'expert'@'localhost' IDENTIFIED BY 'Expert@123';
GRANT ALL PRIVILEGES ON expert_db.* TO 'expert'@'localhost';
FLUSH PRIVILEGES;
```

### 2.4 安装Nginx

```bash
apt install -y nginx
systemctl start nginx
systemctl enable nginx
```

### 2.5 安装Node.js（可选，用于本地构建）

```bash
# 安装Node.js 18
curl -fsSL https://deb.nodesource.com/setup_18.x | bash -
apt install -y nodejs

# 验证
node -v
npm -v
```

---

## 三、后端部署

### 3.1 本地打包

在本地开发机器上：

```bash
cd expert-backend

# 修改配置为生产环境
# 编辑 src/main/resources/application.yml
# 修改数据库连接、JWT密钥等

# 打包
mvn clean package -DskipTests

# 生成的jar包: target/expert-backend-1.0.0.jar
```

### 3.2 上传到服务器

```bash
# 本地执行
scp target/expert-backend-1.0.0.jar root@<服务器IP>:/opt/expert/
```

### 3.3 创建生产配置

在服务器上创建 `/opt/expert/application-prod.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/expert_db?useSSL=false&serverTimezone=Asia/Shanghai
    username: expert
    password: Expert@123
    driver-class-name: com.mysql.cj.jdbc.Driver

jwt:
  secret: your-production-secret-key-at-least-256-bits-long
  expiration: 86400000  # 24小时

server:
  port: 8080
```

### 3.4 创建启动脚本

创建 `/opt/expert/start.sh`：

```bash
#!/bin/bash
APP_NAME=expert-backend-1.0.0.jar
LOG_FILE=/opt/expert/app.log

nohup java -Xms512m -Xmx1024m \
  -jar $APP_NAME \
  --spring.profiles.active=prod \
  > $LOG_FILE 2>&1 &

echo "Application started. PID: $!"
```

### 3.5 创建停止脚本

创建 `/opt/expert/stop.sh`：

```bash
#!/bin/bash
PID=$(ps -ef | grep expert-backend | grep -v grep | awk '{print $2}')
if [ -n "$PID" ]; then
  kill -9 $PID
  echo "Application stopped. PID: $PID"
else
  echo "Application not running"
fi
```

### 3.6 启动后端

```bash
chmod +x /opt/expert/*.sh
cd /opt/expert
./start.sh

# 查看日志
tail -f app.log
```

### 3.7 配置Systemd服务（推荐）

创建 `/etc/systemd/system/expert-backend.service`：

```ini
[Unit]
Description=Expert Backend Service
After=network.target mysql.service

[Service]
Type=simple
User=root
WorkingDirectory=/opt/expert
ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar expert-backend-1.0.0.jar --spring.profiles.active=prod
Restart=always
RestartSec=10

[Install]
WantedBy=multi-user.target
```

启用服务：

```bash
systemctl daemon-reload
systemctl enable expert-backend
systemctl start expert-backend
systemctl status expert-backend
```

---

## 四、前端部署

### 4.1 本地构建

在本地开发机器上：

```bash
cd expert-frontend

# 创建生产环境配置
cat > .env.production << 'EOF'
VITE_API_BASE_URL=/api
EOF

# 构建
npm run build

# 生成的静态文件: dist/
```

### 4.2 上传到服务器

```bash
# 本地执行
scp -r dist/* root@<服务器IP>:/var/www/expert-frontend/
```

---

## 五、Nginx配置

### 5.1 创建Nginx配置

创建 `/etc/nginx/sites-available/expert.conf`：

```nginx
server {
    listen 80;
    server_name your-domain.com;  # 替换为你的域名或IP

    # 前端静态文件
    root /var/www/expert-frontend;
    index index.html;

    # 前端路由（Vue Router history模式）
    location / {
        try_files $uri $uri/ /index.html;
    }

    # 后端API代理
    location /api {
        proxy_pass http://127.0.0.1:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;

        # 超时设置
        proxy_connect_timeout 60s;
        proxy_send_timeout 60s;
        proxy_read_timeout 60s;
    }

    # Gzip压缩
    gzip on;
    gzip_types text/plain text/css application/json application/javascript text/xml application/xml application/xml+rss text/javascript;
    gzip_min_length 1000;

    # 静态资源缓存
    location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg|woff|woff2)$ {
        expires 30d;
        add_header Cache-Control "public, immutable";
    }
}
```

### 5.2 启用配置

```bash
ln -s /etc/nginx/sites-available/expert.conf /etc/nginx/sites-enabled/
nginx -t
systemctl reload nginx
```

---

## 六、HTTPS配置（可选）

### 6.1 申请SSL证书

腾讯云提供免费SSL证书：
1. 登录腾讯云控制台
2. SSL证书管理 -> 申请免费证书
3. 填写域名信息
4. 下载证书文件

### 6.2 配置HTTPS

```nginx
server {
    listen 443 ssl;
    server_name your-domain.com;

    ssl_certificate /opt/ssl/your-domain.com.crt;
    ssl_certificate_key /opt/ssl/your-domain.com.key;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    # 其他配置同上...
}

# HTTP重定向到HTTPS
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}
```

---

## 七、数据库初始化

### 7.1 导入初始数据

```bash
# 上传SQL文件
scp src/main/resources/db/*.sql root@<服务器IP>:/opt/expert/sql/

# 导入
mysql -u expert -p expert_db < /opt/expert/sql/init_schema.sql
mysql -u expert -p expert_db < /opt/expert/sql/init_data.sql
```

---

## 八、部署检查清单

| 检查项 | 命令 |
|--------|------|
| 后端服务状态 | `systemctl status expert-backend` |
| 后端端口监听 | `netstat -tlnp | grep 8080` |
| Nginx状态 | `systemctl status nginx` |
| 前端文件 | `ls -la /var/www/expert-frontend/` |
| 数据库连接 | `mysql -u expert -p -e "SELECT 1"` |
| API测试 | `curl http://localhost/api/test/db-conn` |

---

## 九、常见问题

### 9.1 后端启动失败
```bash
# 查看日志
tail -100 /opt/expert/app.log
journalctl -u expert-backend -f
```

### 9.2 数据库连接失败
```bash
# 检查MySQL状态
systemctl status mysql

# 测试连接
mysql -u expert -p expert_db
```

### 9.3 Nginx 502错误
```bash
# 检查后端是否运行
curl http://127.0.0.1:8080/api/test/db-conn

# 查看Nginx错误日志
tail -f /var/log/nginx/error.log
```

---

## 十、一键部署脚本

创建 `/opt/expert/deploy.sh`：

```bash
#!/bin/bash
set -e

echo "=== 停止旧服务 ==="
systemctl stop expert-backend || true

echo "=== 启动新服务 ==="
systemctl start expert-backend

echo "=== 检查服务状态 ==="
sleep 5
systemctl status expert-backend

echo "=== 测试API ==="
curl -s http://localhost:8080/api/test/db-conn

echo "=== 重载Nginx ==="
systemctl reload nginx

echo "=== 部署完成 ==="
```