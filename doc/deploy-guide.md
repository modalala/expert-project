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

### 1.2 安全组配置

腾讯云安全组需开放以下端口：
- **22**: SSH
- **80**: HTTP
- **443**: HTTPS（可选）

---

## 二、服务器初始化

### 2.1 连接服务器

```bash
ssh root@<服务器公网IP>
```

### 2.2 系统更新与工具安装

```bash
# 更新系统
apt update && apt upgrade -y

# 安装常用工具
apt install -y wget curl vim git
```

### 2.3 配置防火墙

```bash
# 安装ufw
apt install -y ufw

# 开放端口
ufw allow 22/tcp   # SSH
ufw allow 80/tcp   # HTTP
ufw allow 443/tcp  # HTTPS（可选）

# 启用防火墙
ufw enable
```

### 2.4 设置时区

```bash
timedatectl set-timezone Asia/Shanghai
```

---

## 三、安装Docker

### 3.1 安装Docker

```bash
# 添加Docker官方GPG密钥
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# 添加Docker官方源
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list > /dev/null

# 安装Docker
apt update
apt install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin

# 启动Docker
systemctl start docker
systemctl enable docker

# 验证安装
docker --version
docker compose version
```

### 3.2 配置镜像加速（国内服务器推荐）

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

# 重启Docker使配置生效
systemctl daemon-reload
systemctl restart docker
```

---

## 四、克隆项目

```bash
# 克隆代码到 /root 目录
cd /root
git clone https://github.com/modalala/expert-project.git
cd expert-project
```

---

## 五、项目Docker配置说明

### 5.1 服务架构

```
┌─────────────────────────────────────────────────────────────┐
│                    Docker Network                            │
│                  (expert-network)                            │
│                                                              │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐         │
│  │   MySQL     │  │   Backend   │  │   Frontend  │         │
│  │   :3306     │←─│   :8080     │←─│   :80       │         │
│  │  (内网)     │  │  (内网)     │  │  (公网)     │         │
│  └─────────────┘  └─────────────┘  └─────────────┘         │
│       ↓                ↓                    ↓               │
│  mysql_data卷     多阶段构建          Nginx反向代理         │
│  数据持久化      Docker内编译         → backend:8080       │
└─────────────────────────────────────────────────────────────┘
```

### 5.2 配置文件清单

| 文件 | 路径 | 说明 |
|------|------|------|
| docker-compose.yml | 项目根目录 | 服务编排（MySQL + Backend + Frontend） |
| 后端Dockerfile | `expert-backend/Dockerfile` | **多阶段构建**，Docker内编译 |
| 前端Dockerfile | `expert-frontend/Dockerfile` | **多阶段构建**，Docker内编译 |
| Nginx配置 | `expert-frontend/nginx.conf` | Vue路由 + API反向代理 |
| MySQL初始化 | `mysql/init/01-*.sql` | 表结构（先执行） |
| MySQL初始化 | `mysql/init/02-*.sql` | 初始数据（后执行） |
| 环境变量示例 | `.env.example` | 密码和密钥配置 |

### 5.3 MySQL初始化脚本执行顺序

**重要**：Docker MySQL容器按**字母顺序**执行 `/docker-entrypoint-initdb.d/` 目录下的SQL文件。

项目已正确命名：
```
mysql/init/
  ├── 01-init_schema.sql  ← 先执行（创建表结构）
  └── 02-init_data.sql    ← 后执行（插入初始数据）
```

### 5.4 后端Dockerfile（多阶段构建）

```dockerfile
# ==================== 构建阶段 ====================
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build

# 安装Maven
RUN apk add --no-cache maven

# 先复制pom.xml，利用Docker缓存层
COPY pom.xml .

# 下载依赖（pom.xml不变时跳过，加速构建）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY src ./src

# 构建jar包
RUN mvn clean package -DskipTests -B

# ==================== 运行阶段 ====================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# 复制构建产物
COPY --from=builder /build/target/expert-backend-1.0.0.jar app.jar

# 复制生产配置
COPY src/main/resources/application-prod.yml application-prod.yml

# 创建日志目录
RUN mkdir -p /app/logs

EXPOSE 8080

ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/test/db-conn || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod"]
```

**优势**：
- 无需本地预先编译jar包
- Git clone后直接构建即可
- 利用Docker缓存层加速依赖下载

---

## 六、配置环境变量

```bash
cd /root/expert-project

# 复制环境变量示例文件
cp .env.example .env

# 编辑配置（生产环境必须修改密码和JWT密钥）
vim .env
```

`.env` 文件内容：

```bash
# MySQL配置
MYSQL_ROOT_PASSWORD=Root@123456      # 生产环境请修改
MYSQL_PASSWORD=Expert@123            # 生产环境请修改

# JWT配置（必须修改为复杂密钥）
JWT_SECRET=your-production-secret-key-at-least-256-bits-long-for-hs256-algorithm-security
```

---

## 七、构建并启动

### 7.1 构建镜像

```bash
# 构建所有镜像（首次构建约5-10分钟）
docker compose build
```

### 7.2 启动服务

```bash
# 启动所有服务
docker compose up -d
```

### 7.3 查看服务状态

```bash
# 查看容器状态
docker compose ps

# 应看到三个容器都是 Up 状态
# NAME            STATUS
# expert-mysql    Up (healthy)
# expert-backend  Up (healthy)
# expert-frontend Up
```

### 7.4 查看日志

```bash
# 查看所有服务日志
docker compose logs -f

# 查看单个服务日志
docker compose logs mysql
docker compose logs backend
docker compose logs frontend
```

---

## 八、验证部署

### 8.1 检查API

```bash
# 测试数据库连接
curl http://localhost/api/test/db-conn

# 返回成功：{"success":true,"message":"数据库连接成功"}
```

### 8.2 检查前端

浏览器访问：`http://<服务器公网IP>/`

应看到登录页面。

### 8.3 测试登录

```bash
# 使用初始管理员账户登录
curl -X POST http://localhost/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}'

# 返回token表示成功
```

---

## 九、运维操作

### 9.1 常用命令

```bash
# 查看运行状态
docker compose ps

# 查看日志
docker compose logs -f [service_name]

# 重启单个服务
docker compose restart backend

# 重启所有服务
docker compose restart

# 停止所有服务
docker compose down

# 停止并删除数据卷（慎用！会丢失数据）
docker compose down -v

# 进入容器
docker exec -it expert-backend sh
docker exec -it expert-mysql mysql -u root -p
```

### 9.2 更新部署

```bash
cd /root/expert-project

# 拉取最新代码
git pull

# 重新构建并启动
docker compose build
docker compose up -d

# 或一步完成
docker compose up -d --build
```

### 9.3 数据备份

```bash
# 备份MySQL数据
docker exec expert-mysql mysqldump -u root -pRoot@123456 expert_db > backup_$(date +%Y%m%d).sql

# 恢复数据
docker exec -i expert-mysql mysql -u root -pRoot@123456 expert_db < backup_20240430.sql
```

### 9.4 查看资源占用

```bash
docker stats
docker images
```

---

## 十、HTTPS配置（可选）

### 10.1 申请SSL证书

腾讯云提供免费SSL证书：
1. 登录腾讯云控制台 → SSL证书管理
2. 申请免费证书，填写域名信息
3. 下载Nginx格式证书

### 10.2 上传证书

```bash
# 在项目目录创建ssl目录
mkdir -p /root/expert-project/ssl

# 上传证书文件
# ssl/your-domain.com.crt
# ssl/your-domain.com.key
```

### 10.3 修改Nginx配置

编辑 `expert-frontend/nginx.conf`：

```nginx
# HTTP重定向到HTTPS
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}

# HTTPS配置
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

### 10.4 修改docker-compose.yml

在 frontend 服务添加卷挂载：

```yaml
frontend:
  volumes:
    - ./ssl:/etc/nginx/ssl:ro
```

### 10.5 重新部署

```bash
docker compose up -d --build
```

---

## 十一、部署检查清单

| 步骤 | 命令 | 预期结果 |
|------|------|----------|
| Docker安装 | `docker --version` | 显示版本号 |
| 项目克隆 | `git clone ...` | 目录存在 |
| 环境配置 | `cat .env` | 显示配置 |
| 镜像构建 | `docker compose build` | 构建成功 |
| 服务启动 | `docker compose ps` | 3个容器Up |
| API测试 | `curl localhost/api/test/db-conn` | {"success":true} |
| 前端测试 | 浏览器访问IP | 登录页面 |

---

## 十二、常见问题

### 12.1 Docker安装失败

```bash
# 检查源是否正确
cat /etc/apt/sources.list.d/docker.list

# 重新添加源
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list
apt update && apt install -y docker-ce docker-compose-plugin
```

### 12.2 MySQL容器启动失败

```bash
# 查看日志
docker compose logs mysql

# 常见原因：数据卷权限问题
# 解决：删除旧数据卷重新启动
docker compose down -v
docker compose up -d
```

### 12.3 后端无法连接MySQL

```bash
# 等待MySQL完全启动（约30秒）
docker compose logs mysql | grep "ready for connections"

# 检查网络
docker network inspect expert-network

# 重启后端
docker compose restart backend
```

### 12.4 前端访问502错误

```bash
# 检查后端是否运行
docker compose ps backend

# 检查后端日志
docker compose logs backend

# 后端启动较慢（约60秒），等待健康检查通过
```

### 12.5 镜像构建失败

```bash
# 清理并重新构建
docker compose down
docker system prune -f
docker compose build --no-cache
docker compose up -d
```

### 12.6 端口冲突

```bash
# 检查端口占用
netstat -tlnp | grep -E '80|8080|3306'

# 修改docker-compose.yml端口映射
# 如：改为 "8081:80"
```

---

## 十三、快速部署命令汇总

```bash
# === 一键部署 ===

# 1. 安装Docker
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg && \
echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | tee /etc/apt/sources.list.d/docker.list && \
apt update && apt install -y docker-ce docker-compose-plugin && \
systemctl start docker && systemctl enable docker

# 2. 克隆项目
cd /root && git clone https://github.com/modalala/expert-project.git && cd expert-project

# 3. 配置环境变量
cp .env.example .env

# 4. 构建并启动
docker compose build && docker compose up -d

# 5. 验证
docker compose ps
curl http://localhost/api/test/db-conn
```