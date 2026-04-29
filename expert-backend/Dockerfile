# 后端Dockerfile

FROM eclipse-temurin:17-jdk-alpine

LABEL maintainer="expert-system"
LABEL description="专家库管理系统后端服务"

WORKDIR /app

# 复制jar包
COPY target/expert-backend-1.0.0.jar app.jar

# 复制配置文件
COPY src/main/resources/application-prod.yml application-prod.yml

# 创建日志目录
RUN mkdir -p /app/logs

# 暴露端口
EXPOSE 8080

# JVM参数
ENV JAVA_OPTS="-Xms512m -Xmx1024m -XX:+UseG1GC"

# 启动命令
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod"]