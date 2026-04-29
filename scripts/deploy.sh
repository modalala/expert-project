#!/bin/bash
# 专家库管理系统一键部署脚本

set -e

echo "=========================================="
echo "  专家库管理系统 Docker部署脚本"
echo "=========================================="

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 项目目录
PROJECT_DIR="/opt/expert"
COMPOSE_FILE="$PROJECT_DIR/docker-compose.yml"

# 检查Docker
echo -e "${YELLOW}检查Docker环境...${NC}"
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Docker未安装，请先安装Docker${NC}"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}Docker Compose未安装，请先安装Docker Compose${NC}"
    exit 1
fi

echo -e "${GREEN}Docker环境检查通过${NC}"

# 检查.env文件
if [ ! -f "$PROJECT_DIR/.env" ]; then
    echo -e "${YELLOW}创建.env配置文件...${NC}"
    cp "$PROJECT_DIR/.env.example" "$PROJECT_DIR/.env"
    echo -e "${GREEN}.env文件已创建，请根据需要修改配置${NC}"
fi

# 进入项目目录
cd $PROJECT_DIR

# 停止旧容器
echo -e "${YELLOW}停止旧容器...${NC}"
docker-compose down 2>/dev/null || true

# 拉取最新代码
echo -e "${YELLOW}拉取最新代码...${NC}"
git pull origin main

# 构建后端
echo -e "${YELLOW}构建后端项目...${NC}"
cd expert-backend
if [ -f "mvnw" ]; then
    ./mvnw clean package -DskipTests
else
    mvn clean package -DskipTests
fi
cd ..

# 构建并启动容器
echo -e "${YELLOW}构建Docker镜像并启动服务...${NC}"
docker-compose up -d --build

# 等待服务启动
echo -e "${YELLOW}等待服务启动...${NC}"
sleep 30

# 检查服务状态
echo -e "${YELLOW}检查服务状态...${NC}"
docker-compose ps

# 测试API
echo -e "${YELLOW}测试后端API...${NC}"
sleep 10
API_TEST=$(curl -s http://localhost/api/test/db-conn)
if echo "$API_TEST" | grep -q "200"; then
    echo -e "${GREEN}API测试成功${NC}"
else
    echo -e "${RED}API测试失败，请检查日志${NC}"
    docker-compose logs backend
fi

echo "=========================================="
echo -e "${GREEN}部署完成！${NC}"
echo "=========================================="
echo -e "${GREEN}访问地址: http://<服务器IP>${NC}"
echo -e "${GREEN}登录账号: admin / Admin@123${NC}"
echo ""
echo "常用命令:"
echo "  查看日志: docker-compose logs -f backend"
echo "  停止服务: docker-compose down"
echo "  重启服务: docker-compose restart"
echo "=========================================="