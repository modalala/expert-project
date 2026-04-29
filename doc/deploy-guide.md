# 腾讯云部署指南（Docker版）

## 一、服务器准备

### 1.1 购买腾讯云CVM

推荐配置：
| 配置项 | 推荐值 | 说明 |
|--------|--------|------|
| CPU | 2核 | 最低配置 |
| 内存 | 4GB | Docker运行需要 |
| 系统盘 | 50GB SSD | 镜像和数据存储 |
| 操作系统 | Ubuntu 24.04 LTS 64bit | Docker支持最佳 |
| 网络 | 公网IP | 需开放80/443端口 |

### 1.2 Ubuntu系统初始化

```bash
# 1. 更新系统
apt update && apt upgrade -y

# 2. 安装常用工具
apt install -y wget curl vim net-tools git

# 3. 配置防火墙（使用ufw）
apt install -y ufw
ufw allow 22/tcp   # SSH
ufw allow 80/tcp   # HTTP
ufw allow 443/tcp  # HTTPS
ufw enable

# 4. 设置时区
timedatectl set-timezone Asia/Shanghai
```

### 1.3 安全组配置

腾讯云安全组需开放以下端口：
- **22**: SSH
- **80**: HTTP
- **443**: HTTPS

---

## 二、安装Docker环境

### 2.1 安装Docker

```bash
# 安装Docker官方GPG密钥
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# 添加Docker官方源
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# 安装Docker
apt update
apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# 启动Docker
systemctl start docker
systemctl enable docker

# 验证
docker --version
docker compose version
```

### 2.2 配置Docker镜像加速（国内服务器推荐）

```bash
mkdir -p /etc/docker
cat > /etc/docker/daemon.json << 'EOF'
{
  "registry-mirrors": [
    "https://mirror.ccs.tencentyun.com"
  ],
  "log-driver": "json-file",
  "log-opts": {
    "max-size": "100m",
    "max-file": "3"
  }
}
EOF

systemctl daemon-reload
systemctl restart docker
```

---

## 三、项目Docker配置说明

项目已包含完整的Docker配置文件，可直接使用：

| 文件 | 路径 | 说明 |
|------|------|------|
| docker-compose.yml | 项目根目录 | 服务编排（MySQL + Backend + Frontend） |
| 后端Dockerfile | `expert-backend/Dockerfile` | Spring Boot镜像 |
| 前端Dockerfile | `expert-frontend/Dockerfile` | Vue3 + Nginx镜像 |
| Nginx配置 | `expert-frontend/nginx.conf` | 反向代理配置 |
| MySQL初始化脚本 | `mysql/init/*.sql` | 数据库表结构和初始数据 |
| 环境变量示例 | `.env.example` | 密码和密钥配置 |

### 3.0 Docker Compose服务架构

```
┌─────────────────────────────────────────────────────────────┐
│                      Docker Network                          │
│                    (expert-network)                          │
│                                                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │   MySQL     │  │   Backend   │  │   Frontend  │         │
│  │   :3306     │←→│   :8080     │←→│   :80       │         │
│  │  (内网)     │  │  (内网)     │  │  (公网)     │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
│       ↓                                     ↓               │
│  mysql_data卷                          Nginx反向代理         │
│  数据持久化                            → backend:8080       │
└─────────────────────────────────────────────────────────────┘
```

三个容器服务：
- **mysql**: MySQL 8.0 数据库，自动初始化表结构和数据
- **backend**: Spring Boot 后端服务，连接MySQL
- **frontend**: Vue3 + Nginx 前端服务，对外暴露80端口

### 3.1 后端Dockerfile

当前配置需要先本地构建jar包：

```bash
cd expert-backend
mvn clean package -DskipTests
```

如需改为多阶段构建（在Docker内编译），将 `expert-backend/Dockerfile` 改为：

```dockerfile
# 构建阶段
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app

RUN apk add --no-cache maven

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# 运行阶段
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY --from=builder /app/target/expert-backend-1.0.0.jar app.jar
COPY src/main/resources/application-prod.yml application-prod.yml

RUN mkdir -p /app/logs

EXPOSE 8080

ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod"]
```

### 3.2 前端Dockerfile（已为多阶段构建）

当前配置已最优，无需修改：
- 构建阶段：Node 20 Alpine
- 运行阶段：Nginx Alpine

### 3.3 Nginx配置

`expert-frontend/nginx.conf` 已包含：
- Vue Router history 模式
- 后端 API 代理到 `backend:8080`
- Gzip 压缩
- 静态资源缓存

---

## 四、部署流程

### 4.1 上传项目到服务器

**方式一：Git克隆（推荐）**

```bash
ssh root@<服务器IP>
cd /opt
git clone <项目Git地址> expert
cd expert
```

**方式二：SCP上传**

```bash
scp -r docker-compose.yml expert-backend expert-frontend mysql .env.example root@<服务器IP>:/opt/expert/
```

### 4.2 配置环境变量

```bash
cd /opt/expert
cp .env.example .env
vim .env
```

修改密码和JWT密钥：
```bash
MYSQL_ROOT_PASSWORD=YourStrongRootPassword@123
MYSQL_PASSWORD=YourStrongAppPassword@123
JWT_SECRET=your-production-secret-key-change-this-in-production
```

### 4.3 构建并启动

```bash
# 构建所有镜像
docker compose build

# 启动所有服务
docker compose up -d

# 查看服务状态
docker compose ps

# 查看日志
docker compose logs -f
```

### 4.4 验证部署

```bash
# 检查容器状态
docker ps

# 测试API
curl http://localhost/api/test/db-conn

# 测试前端
curl http://localhost/

# 查看各服务日志
docker compose logs mysql
docker compose logs backend
docker compose logs frontend
```

---

## 五、运维操作

### 5.1 常用命令

```bash
# 查看运行状态
docker compose ps

# 查看日志
docker compose logs -f [service_name]

# 重启服务
docker compose restart [service_name]

# 停止所有服务
docker compose down

# 停止并删除数据卷（慎用）
docker compose down -v

# 进入容器
docker exec -it expert-backend sh
docker exec -it expert-mysql mysql -u root -p
```

### 5.2 更新部署

```bash
git pull
docker compose build
docker compose up -d

# 或一步完成
docker compose up -d --build
```

### 5.3 数据备份

```bash
# 备份MySQL数据
docker exec expert-mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD} expert_db > backup_$(date +%Y%m%d).sql

# 恢复数据
docker exec -i expert-mysql mysql -u root -p${MYSQL_ROOT_PASSWORD} expert_db < backup.sql
```

### 5.4 查看资源占用

```bash
docker stats
docker images
```

---

## 六、HTTPS配置

### 6.1 SSL证书

将证书放入 `ssl/` 目录：

```
ssl/
  ├── your-domain.com.crt
  └── your-domain.com.key
```

### 6.2 修改Nginx配置

更新 `expert-frontend/nginx.conf`：

```nginx
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl;
    server_name your-domain.com;

    ssl_certificate /etc/nginx/ssl/your-domain.com.crt;
    ssl_certificate_key /etc/nginx/ssl/your-domain.com.key;

    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers HIGH:!aNULL:!MD5;

    root /usr/share/nginx/html;
    index index.html;

    location / {
        try_files $uri $uri/ /index.html;
    }

    location /api {
        proxy_pass http://backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}
```

### 6.3 挂载SSL证书

修改 `docker-compose.yml`，在 frontend 服务添加：

```yaml
frontend:
  volumes:
    - ./ssl:/etc/nginx/ssl:ro
```

---

## 七、部署检查清单

| 检查项 | 命令 |
|--------|------|
| Docker服务 | `systemctl status docker` |
| 容器状态 | `docker compose ps` |
| 后端健康 | `curl http://localhost/api/test/db-conn` |
| 前端访问 | `curl http://localhost/` |
| 数据库连接 | `docker exec expert-mysql mysql -u expert -p` |
| 日志查看 | `docker compose logs -f` |

---

## 八、常见问题

### 8.1 容器启动失败

```bash
docker compose logs backend
docker events
docker images
```

### 8.2 MySQL连接失败

```bash
# 等待MySQL完全启动
docker compose logs mysql | grep "ready for connections"

# 测试连接
docker exec -it expert-mysql mysql -u expert -p
```

### 8.3 端口冲突

```bash
netstat -tlnp | grep -E '80|8080|3306'

# 修改docker-compose.yml端口映射
# 如改为 "8081:80"
```

### 8.4 镜像构建失败

```bash
docker compose build --no-cache --progress=plain
docker compose down
docker system prune -f
docker compose build
```

### 8.5 数据卷问题

```bash
docker volume ls
docker volume inspect expert_mysql_data
```

---

## 九、一键部署脚本

项目已有 `scripts/deploy.sh`，使用方法：

```bash
chmod +x scripts/deploy.sh
./scripts/deploy.sh
```