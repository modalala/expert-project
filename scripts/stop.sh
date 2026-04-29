#!/bin/bash
# 停止所有服务

cd /opt/expert
docker-compose down

echo "服务已停止"