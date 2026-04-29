# Docker部署指南 - CentOS 7.6

## 一、服务器环境准备

### 1.1 安装Docker

```bash
# 更新yum源
yum update -y

# 安装Docker
yum install -y yum-utils
yum-config-manager --add-repo https://download.docker.com/linux/centos/docker-ce.repo
yum install -y docker-ce docker-ce-cli containerd.io

# 启动Docker
systemctl start docker
systemctl enable docker

# 验证
docker --version
```

### 1.2 安装Docker Compose

```bash
# 安装
curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose

# 验证
docker-compose --version
```

### 1.3 安装Git

```bash
yum install -y git
git --version
```

---

## 二、项目目录结构

```
/opt/expert/
├── docker-compose.yml
├── .env
├── backend/
│   └── Dockerfile
├── frontend/
│   └── Dockerfile
│   └── nginx.conf
├── mysql/
│   └── init/
│       ├── init_schema.sql
│       └── init_data.sql
└── scripts/
    ├── deploy.sh
    ├── start.sh
    ├── stop.sh
    └── backup.sh
```

---

## 三、克隆项目

```bash
# 创建目录
mkdir -p /opt/expert
cd /opt/expert

# 克隆项目
git clone https://github.com/modalala/expert-project.git .

# 或者指定分支
git clone -b main https://github.com/modalala/expert-project.git .
```

---

## 四、一键部署

```bash
cd /opt/expert
chmod +x scripts/*.sh
./scripts/deploy.sh
```

---

## 五、常用命令

| 操作 | 命令 |
|------|------|
| 启动所有服务 | `docker-compose up -d` |
| 停止所有服务 | `docker-compose down` |
| 查看服务状态 | `docker-compose ps` |
| 查看后端日志 | `docker-compose logs -f backend` |
| 重启后端 | `docker-compose restart backend` |
| 重新构建部署 | `docker-compose up -d --build` |

---

## 六、更新部署

```bash
cd /opt/expert

# 拉取最新代码
git pull origin main

# 重新构建并部署
docker-compose down
docker-compose up -d --build

# 或者使用部署脚本
./scripts/deploy.sh
```

---

## 七、访问地址

- 前端页面: http://<服务器IP>
- 后端API: http://<服务器IP>/api
- 默认账号: admin / Admin@123

---

## 八、腾讯云安全组

开放端口：
- **22**: SSH
- **80**: HTTP
- **443**: HTTPS (如果配置了SSL)