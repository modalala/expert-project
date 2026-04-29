# 前端Dockerfile - 多阶段构建

# 第一阶段: 构建
FROM node:20-alpine AS builder

WORKDIR /build

# 复制依赖文件
COPY package*.json ./

# 安装依赖
RUN npm ci

# 复制源代码
COPY . .

# 构建生产版本
RUN npm run build

# 第二阶段: Nginx服务
FROM nginx:alpine

LABEL maintainer="expert-system"
LABEL description="专家库管理系统前端服务"

# 复制构建产物
COPY --from=builder /build/dist /usr/share/nginx/html

# 复制Nginx配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 暴露端口
EXPOSE 80

# 启动Nginx
CMD ["nginx", "-g", "daemon off;"]