#!/bin/bash
# 启动所有服务

cd /opt/expert
docker-compose up -d

echo "服务启动完成"
docker-compose ps