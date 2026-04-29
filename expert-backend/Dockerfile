# 后端Dockerfile - 多阶段构建

# ==================== 构建阶段 ====================
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build

# 安装Maven
RUN apk add --no-cache maven

# 先复制pom.xml，利用Docker缓存层
COPY pom.xml .

# 下载依赖（缓存层，pom.xml不变时跳过）
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

# 暴露端口
EXPOSE 8080

# JVM参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200"

# 健康检查
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/test/db-conn || exit 1

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod"]