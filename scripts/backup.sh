#!/bin/bash
# 数据库备份脚本

BACKUP_DIR="/opt/expert/backups"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/expert_db_$DATE.sql"

# 创建备份目录
mkdir -p $BACKUP_DIR

# 执行备份
echo "开始备份数据库..."
docker exec expert-mysql mysqldump -u root -p${MYSQL_ROOT_PASSWORD:-Root@123456} expert_db > $BACKUP_FILE

# 压缩备份文件
gzip $BACKUP_FILE

echo "备份完成: $BACKUP_FILE.gz"

# 删除7天前的备份
find $BACKUP_DIR -name "*.sql.gz" -mtime +7 -delete

echo "旧备份已清理"