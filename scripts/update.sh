#!/bin/bash
# 更新部署脚本

cd /opt/expert

echo "拉取最新代码..."
git pull origin main

echo "重新构建并部署..."
docker-compose down
docker-compose up -d --build

echo "更新完成"
docker-compose ps