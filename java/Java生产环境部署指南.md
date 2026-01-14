# Java 生产环境部署指南

## 📋 目录

- [编译和打包](#编译和打包)
- [运行方式](#运行方式)
- [生产环境最佳实践](#生产环境最佳实践)
- [监控和日志](#监控和日志)
- [性能优化](#性能优化)
- [常见问题](#常见问题)

---

## 🔨 编译和打包

### 1. 清理并编译

```bash
# 清理之前的构建产物
mvn clean

# 编译项目（跳过测试，生产环境通常跳过测试）
mvn clean compile -DskipTests

# 或者完整构建（包含测试）
mvn clean install
```

### 2. 打包成 JAR

#### 普通 JAR（需要指定主类运行）

```bash
# 打包
mvn clean package -DskipTests

# 生成的文件位置
# target/firstjava-1.0-SNAPSHOT.jar

# 运行（需要指定主类）
java -cp target/firstjava-1.0-SNAPSHOT.jar com.hry.firstjava.App
```

#### 可执行 JAR（Fat JAR，包含所有依赖）

需要在 `pom.xml` 中配置 Maven Shade Plugin：

```xml
<build>
    <plugins>
        <!-- Maven Shade Plugin - 打包成可执行 JAR -->
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.5.0</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                    <configuration>
                        <transformers>
                            <transformer implementation="org.apache.maven.plugins.shade.resource.ManifestResourceTransformer">
                                <mainClass>com.hry.firstjava.App</mainClass>
                            </transformer>
                        </transformers>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

然后打包：

```bash
mvn clean package -DskipTests

# 生成可执行 JAR
# target/firstjava-1.0-SNAPSHOT.jar

# 直接运行（不需要指定主类）
java -jar target/firstjava-1.0-SNAPSHOT.jar
```

### 3. 打包成 WAR（Web 应用）

```xml
<!-- 修改 pom.xml -->
<packaging>war</packaging>
```

```bash
# 打包
mvn clean package -DskipTests

# 生成的文件
# target/firstjava-1.0-SNAPSHOT.war

# 部署到 Tomcat
cp target/firstjava-1.0-SNAPSHOT.war /path/to/tomcat/webapps/
```

---

## 🚀 运行方式

### 方式 1：直接运行 JAR（简单应用）

```bash
# 基本运行
java -jar target/firstjava-1.0-SNAPSHOT.jar

# 指定 JVM 参数
java -Xms512m -Xmx1024m -jar target/firstjava-1.0-SNAPSHOT.jar

# 后台运行
nohup java -jar target/firstjava-1.0-SNAPSHOT.jar > app.log 2>&1 &

# 查看进程
ps aux | grep java

# 停止进程
kill <PID>
# 或
pkill -f firstjava
```

### 方式 2：使用 systemd（Linux 服务，推荐）

创建服务文件 `/etc/systemd/system/myapp.service`：

```ini
[Unit]
Description=My Java Application
After=network.target

[Service]
Type=simple
User=appuser
WorkingDirectory=/opt/myapp
ExecStart=/usr/bin/java -Xms512m -Xmx1024m -jar /opt/myapp/firstjava-1.0-SNAPSHOT.jar
Restart=always
RestartSec=10
StandardOutput=journal
StandardError=journal

[Install]
WantedBy=multi-user.target
```

使用服务：

```bash
# 重载 systemd 配置
sudo systemctl daemon-reload

# 启动服务
sudo systemctl start myapp

# 停止服务
sudo systemctl stop myapp

# 重启服务
sudo systemctl restart myapp

# 查看状态
sudo systemctl status myapp

# 开机自启
sudo systemctl enable myapp

# 查看日志
sudo journalctl -u myapp -f
```

### 方式 3：使用 Docker（容器化部署）

#### 创建 Dockerfile

```dockerfile
# 使用多阶段构建
# 阶段 1：构建
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# 阶段 2：运行
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/firstjava-1.0-SNAPSHOT.jar app.jar

# 暴露端口（根据应用需要）
EXPOSE 8080

# 运行应用
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### 构建和运行

```bash
# 构建镜像
docker build -t myapp:1.0 .

# 运行容器
docker run -d \
  --name myapp \
  -p 8080:8080 \
  -v /path/to/logs:/app/logs \
  myapp:1.0

# 查看日志
docker logs -f myapp

# 停止容器
docker stop myapp

# 重启容器
docker restart myapp
```

#### 使用 Docker Compose

创建 `docker-compose.yml`：

```yaml
version: '3.8'

services:
  app:
    build: .
    container_name: myapp
    ports:
      - "8080:8080"
    volumes:
      - ./logs:/app/logs
    environment:
      - JAVA_OPTS=-Xms512m -Xmx1024m
    restart: unless-stopped
    networks:
      - app-network

networks:
  app-network:
    driver: bridge
```

运行：

```bash
# 启动
docker-compose up -d

# 停止
docker-compose down

# 查看日志
docker-compose logs -f
```

### 方式 4：使用 Supervisor（进程管理）

安装 Supervisor：

```bash
# Ubuntu/Debian
sudo apt-get install supervisor

# CentOS/RHEL
sudo yum install supervisor
```

创建配置文件 `/etc/supervisor/conf.d/myapp.conf`：

```ini
[program:myapp]
command=/usr/bin/java -Xms512m -Xmx1024m -jar /opt/myapp/firstjava-1.0-SNAPSHOT.jar
directory=/opt/myapp
user=appuser
autostart=true
autorestart=true
stderr_logfile=/var/log/myapp/error.log
stdout_logfile=/var/log/myapp/output.log
environment=JAVA_HOME="/usr/lib/jvm/java-17-openjdk"
```

使用：

```bash
# 重载配置
sudo supervisorctl reread
sudo supervisorctl update

# 启动
sudo supervisorctl start myapp

# 停止
sudo supervisorctl stop myapp

# 重启
sudo supervisorctl restart myapp

# 查看状态
sudo supervisorctl status myapp
```

---

## 🏭 生产环境最佳实践

### 1. JVM 参数优化

#### 内存设置

```bash
# 设置堆内存
-Xms2g          # 初始堆内存
-Xmx4g          # 最大堆内存

# 设置元空间（Java 8+）
-XX:MetaspaceSize=256m
-XX:MaxMetaspaceSize=512m

# 设置直接内存
-XX:MaxDirectMemorySize=1g
```

#### GC 设置

```bash
# 使用 G1 垃圾收集器（推荐 Java 11+）
-XX:+UseG1GC
-XX:MaxGCPauseMillis=200
-XX:G1HeapRegionSize=16m

# 或使用 ZGC（Java 11+，低延迟）
-XX:+UseZGC
-XX:+UnlockExperimentalVMOptions
```

#### 完整 JVM 参数示例

```bash
java \
  -Xms2g \
  -Xmx4g \
  -XX:MetaspaceSize=256m \
  -XX:MaxMetaspaceSize=512m \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/var/log/myapp/heapdump.hprof \
  -XX:+PrintGCDetails \
  -XX:+PrintGCDateStamps \
  -Xloggc:/var/log/myapp/gc.log \
  -Djava.security.egd=file:/dev/./urandom \
  -jar firstjava-1.0-SNAPSHOT.jar
```

### 2. 环境变量配置

```bash
# 创建环境变量文件
cat > /opt/myapp/.env << EOF
JAVA_HOME=/usr/lib/jvm/java-17-openjdk
APP_NAME=myapp
APP_PORT=8080
LOG_LEVEL=INFO
DB_URL=jdbc:mysql://localhost:3306/mydb
DB_USER=myuser
DB_PASSWORD=mypassword
EOF

# 加载环境变量
source /opt/myapp/.env

# 运行应用
java -jar firstjava-1.0-SNAPSHOT.jar
```

### 3. 日志配置

#### 使用 Logback（推荐）

在 `pom.xml` 添加依赖：

```xml
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.4.11</version>
</dependency>
```

创建 `src/main/resources/logback-spring.xml`：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <property name="LOG_PATH" value="/var/log/myapp"/>
    
    <!-- 控制台输出 -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- 文件输出 -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_PATH}/app.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/app.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
            <totalSizeCap>10GB</totalSizeCap>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <!-- 错误日志单独输出 -->
    <appender name="ERROR_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>${LOG_PATH}/error.log</file>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>${LOG_PATH}/error.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>90</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n</pattern>
        </encoder>
    </appender>
    
    <root level="INFO">
        <appender-ref ref="CONSOLE"/>
        <appender-ref ref="FILE"/>
        <appender-ref ref="ERROR_FILE"/>
    </root>
</configuration>
```

### 4. 配置文件管理

#### 使用 application.properties

```properties
# src/main/resources/application.properties
app.name=My Application
app.version=1.0.0
server.port=8080

# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=myuser
spring.datasource.password=mypassword
```

#### 使用环境变量覆盖

```bash
# 生产环境使用环境变量
export DB_PASSWORD=production_password

# 运行应用
java -jar app.jar
```

### 5. 安全配置

```bash
# 使用非 root 用户运行
useradd -r -s /bin/false appuser
chown -R appuser:appuser /opt/myapp

# 设置文件权限
chmod 750 /opt/myapp
chmod 640 /opt/myapp/*.jar
```

---

## 📊 监控和日志

### 1. 健康检查

#### 添加健康检查端点（Spring Boot）

```java
@RestController
public class HealthController {
    @GetMapping("/health")
    public Map<String, String> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", Instant.now().toString());
        return status;
    }
}
```

#### 使用 curl 检查

```bash
# 健康检查
curl http://localhost:8080/health

# 或使用 systemd 健康检查
ExecStart=/usr/bin/java -jar /opt/myapp/app.jar
ExecStartPost=/bin/bash -c 'until curl -f http://localhost:8080/health; do sleep 1; done'
```

### 2. 日志查看

```bash
# 实时查看日志
tail -f /var/log/myapp/app.log

# 查看错误日志
tail -f /var/log/myapp/error.log

# 查看最近 100 行
tail -n 100 /var/log/myapp/app.log

# 搜索日志
grep "ERROR" /var/log/myapp/app.log

# 使用 journalctl（systemd）
journalctl -u myapp -f
```

### 3. JVM 监控

#### 使用 jstat 查看 GC

```bash
# 查看 GC 统计
jstat -gc <PID> 1000

# 查看内存使用
jstat -gccapacity <PID>
```

#### 使用 jmap 生成堆转储

```bash
# 生成堆转储
jmap -dump:format=b,file=heapdump.hprof <PID>

# 查看堆内存使用
jmap -heap <PID>
```

#### 使用 jstack 查看线程

```bash
# 查看线程堆栈
jstack <PID> > threaddump.txt
```

### 4. 应用监控（可选）

#### 使用 Prometheus + Grafana

在 `pom.xml` 添加依赖：

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
    <version>1.11.5</version>
</dependency>
```

---

## ⚡ 性能优化

### 1. JVM 调优

```bash
# 生产环境推荐配置
java \
  -server \
  -Xms4g \
  -Xmx4g \
  -XX:+UseG1GC \
  -XX:MaxGCPauseMillis=200 \
  -XX:InitiatingHeapOccupancyPercent=45 \
  -XX:+DisableExplicitGC \
  -XX:+HeapDumpOnOutOfMemoryError \
  -XX:HeapDumpPath=/var/log/myapp/heapdump.hprof \
  -jar app.jar
```

### 2. 应用优化

- 使用连接池（数据库、HTTP）
- 启用缓存（Redis、本地缓存）
- 异步处理（线程池、消息队列）
- 数据库索引优化

---

## 🔧 常见问题

### Q1: 如何查看应用占用的内存？

```bash
# 查看进程内存
ps aux | grep java

# 使用 jmap
jmap -heap <PID>

# 使用 top
top -p <PID>
```

### Q2: 如何优雅关闭应用？

```bash
# 发送 SIGTERM 信号
kill <PID>

# 或使用 Spring Boot Actuator
curl -X POST http://localhost:8080/actuator/shutdown
```

### Q3: 如何查看应用启动时间？

```bash
# 查看日志中的启动时间
grep "Started" /var/log/myapp/app.log

# 或使用 systemd
systemd-analyze blame | grep myapp
```

### Q4: 如何实现零停机部署？

```bash
# 使用蓝绿部署
# 1. 部署新版本到新服务器
# 2. 健康检查通过后切换流量
# 3. 停止旧版本

# 或使用滚动更新（Kubernetes）
kubectl set image deployment/myapp myapp=myapp:2.0
```

### Q5: 如何排查内存泄漏？

```bash
# 1. 生成堆转储
jmap -dump:format=b,file=heapdump.hprof <PID>

# 2. 使用 Eclipse MAT 分析
# 下载：https://www.eclipse.org/mat/

# 3. 查看 GC 日志
tail -f /var/log/myapp/gc.log
```

---

## 📝 完整部署脚本示例

创建 `deploy.sh`：

```bash
#!/bin/bash

# 配置
APP_NAME="myapp"
APP_VERSION="1.0-SNAPSHOT"
APP_DIR="/opt/myapp"
JAR_FILE="firstjava-${APP_VERSION}.jar"
SERVICE_NAME="myapp"

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
NC='\033[0m' # No Color

echo -e "${GREEN}开始部署 ${APP_NAME}...${NC}"

# 1. 编译打包
echo "编译打包中..."
mvn clean package -DskipTests
if [ $? -ne 0 ]; then
    echo -e "${RED}编译失败！${NC}"
    exit 1
fi

# 2. 备份旧版本
if [ -f "${APP_DIR}/${JAR_FILE}" ]; then
    echo "备份旧版本..."
    cp "${APP_DIR}/${JAR_FILE}" "${APP_DIR}/${JAR_FILE}.backup.$(date +%Y%m%d%H%M%S)"
fi

# 3. 停止服务
echo "停止服务..."
systemctl stop ${SERVICE_NAME}

# 4. 复制新版本
echo "复制新版本..."
mkdir -p ${APP_DIR}
cp target/${JAR_FILE} ${APP_DIR}/

# 5. 启动服务
echo "启动服务..."
systemctl start ${SERVICE_NAME}

# 6. 检查状态
sleep 5
if systemctl is-active --quiet ${SERVICE_NAME}; then
    echo -e "${GREEN}部署成功！${NC}"
    systemctl status ${SERVICE_NAME}
else
    echo -e "${RED}部署失败！${NC}"
    systemctl status ${SERVICE_NAME}
    exit 1
fi
```

使用：

```bash
chmod +x deploy.sh
./deploy.sh
```

---

## 📚 总结

### 生产环境部署检查清单

- [ ] 编译打包（跳过测试）
- [ ] 配置 JVM 参数（内存、GC）
- [ ] 配置日志（文件输出、滚动）
- [ ] 配置环境变量
- [ ] 使用非 root 用户运行
- [ ] 配置服务管理（systemd/supervisor）
- [ ] 配置健康检查
- [ ] 配置监控和告警
- [ ] 配置备份和恢复
- [ ] 配置防火墙和安全组

### 推荐方案

- **小型项目**：直接运行 JAR + systemd
- **中型项目**：Docker + Docker Compose
- **大型项目**：Kubernetes + CI/CD

---
