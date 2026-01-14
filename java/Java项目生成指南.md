# Java 项目生成指南

## 📋 目录

- [方法 1：使用 Maven 生成项目（推荐）](#方法-1使用-maven-生成项目推荐)
- [方法 2：使用 Gradle 生成项目](#方法-2使用-gradle-生成项目)
- [方法 3：使用 Spring Initializr 生成 Spring Boot 项目](#方法-3使用-spring-initializr-生成-spring-boot-项目)
- [方法 4：使用 IDE 创建项目](#方法-4使用-ide-创建项目)
- [方法 5：手动创建项目](#方法-5手动创建项目)
- [项目生成后的下一步](#项目生成后的下一步)

---

## 🚀 方法 1：使用 Maven 生成项目（推荐）

### 前置条件

```bash
# 检查 Maven 是否安装
mvn -version

# 如果未安装，使用 SDKMAN 安装
sdk install maven
```

### 快速生成标准 Java 项目

```bash
# 生成标准 Java 项目
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

# 进入项目目录
cd my-app

# 查看项目结构
tree -L 3
# 或
ls -R
```

**生成的项目结构：**
```
my-app/
├── pom.xml
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── example/
    │               └── App.java
    └── test/
        └── java/
            └── com/
                └── example/
                    └── AppTest.java
```

### 生成 Web 应用项目

```bash
# 生成 Web 应用项目
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-webapp \
  -DarchetypeArtifactId=maven-archetype-webapp \
  -DinteractiveMode=false

cd my-webapp
```

**生成的项目结构：**
```
my-webapp/
├── pom.xml
└── src/
    └── main/
        ├── java/
        └── webapp/
            ├── WEB-INF/
            │   └── web.xml
            └── index.jsp
```

### 交互式生成（推荐新手）

```bash
# 不指定参数，Maven 会交互式询问
mvn archetype:generate

# 按提示输入：
# 1. 选择模板（直接回车使用默认）
# 2. groupId: com.example
# 3. artifactId: my-app
# 4. version: 1.0-SNAPSHOT（直接回车）
# 5. package: com.example（直接回车）
```

### 验证项目

```bash
# 编译项目
mvn compile

# 运行测试
mvn test

# 打包项目
mvn package

# 运行项目
mvn exec:java -Dexec.mainClass="com.example.App"
```

---

## 📦 方法 2：使用 Gradle 生成项目

### 前置条件

```bash
# 检查 Gradle 是否安装
gradle -version

# 如果未安装，使用 SDKMAN 安装
sdk install gradle
```

### 使用 Gradle Init 生成项目

```bash
# 创建项目目录
mkdir my-gradle-app
cd my-gradle-app

# 初始化 Gradle 项目
gradle init

# 按提示选择：
# 1. 项目类型：application
# 2. 语言：Java
# 3. 构建脚本：Groovy 或 Kotlin DSL
# 4. 测试框架：JUnit
# 5. 项目名称：my-gradle-app
# 6. 源包：com.example
```

**生成的项目结构：**
```
my-gradle-app/
├── build.gradle          # 构建脚本
├── settings.gradle       # 项目设置
├── gradlew               # Gradle Wrapper（Unix）
├── gradlew.bat           # Gradle Wrapper（Windows）
└── src/
    ├── main/
    │   └── java/
    │       └── com/
    │           └── example/
    │               └── App.java
    └── test/
        └── java/
            └── com/
                └── example/
                    └── AppTest.java
```

### 验证项目

```bash
# 编译项目
./gradlew build

# 运行项目
./gradlew run

# 运行测试
./gradlew test
```

---

## 🌱 方法 3：使用 Spring Initializr 生成 Spring Boot 项目

### 方式 1：使用 Web 界面（最简单）

1. **访问 Spring Initializr**
   - 网址：https://start.spring.io/

2. **配置项目**
   - Project: Maven Project
   - Language: Java
   - Spring Boot: 3.1.0（或最新版本）
   - Project Metadata:
     - Group: `com.example`
     - Artifact: `my-spring-app`
     - Name: `my-spring-app`
     - Package name: `com.example`
     - Packaging: Jar
     - Java: 17

3. **选择依赖**
   - Spring Web
   - Spring Data JPA
   - H2 Database（开发用）
   - 等等...

4. **生成并下载**
   - 点击 "Generate" 按钮
   - 下载 ZIP 文件
   - 解压到工作目录

### 方式 2：使用命令行（curl）

```bash
# 生成 Spring Boot 项目
curl https://start.spring.io/starter.zip \
  -d type=maven-project \
  -d language=java \
  -d bootVersion=3.1.0 \
  -d baseDir=my-spring-app \
  -d groupId=com.example \
  -d artifactId=my-spring-app \
  -d name=my-spring-app \
  -d packageName=com.example \
  -d packaging=jar \
  -d javaVersion=17 \
  -d dependencies=web,data-jpa,h2 \
  -o my-spring-app.zip

# 解压
unzip my-spring-app.zip
cd my-spring-app
```

### 方式 3：使用 Spring CLI

```bash
# 安装 Spring CLI
sdk install springboot

# 生成项目
spring init \
  --dependencies=web,data-jpa,h2 \
  --groupId=com.example \
  --artifactId=my-spring-app \
  --name=my-spring-app \
  --package-name=com.example \
  --java-version=17 \
  my-spring-app
```

### 运行 Spring Boot 项目

```bash
# 进入项目目录
cd my-spring-app

# 运行项目
mvn spring-boot:run

# 或打包后运行
mvn package
java -jar target/my-spring-app-0.0.1-SNAPSHOT.jar
```

---

## 💻 方法 4：使用 IDE 创建项目

### IntelliJ IDEA

#### 创建标准 Java 项目

1. **打开 IntelliJ IDEA**
2. **File → New → Project**
3. **选择项目类型：**
   - Java
   - 选择 JDK 版本（如 Java 17）
   - 选择构建工具（Maven 或 Gradle）
4. **配置项目：**
   - Project name: `my-app`
   - Project location: 选择目录
   - Group: `com.example`
   - Artifact: `my-app`
5. **点击 "Create"**

#### 创建 Spring Boot 项目

1. **File → New → Project**
2. **选择 Spring Initializr**
3. **配置项目信息**
4. **选择依赖**
5. **点击 "Create"**

### VS Code

#### 安装扩展

```bash
# 安装 Java Extension Pack
# 包括：
# - Language Support for Java
# - Debugger for Java
# - Test Runner for Java
# - Maven for Java
# - Project Manager for Java
```

#### 创建项目

1. **打开命令面板**（Cmd+Shift+P / Ctrl+Shift+P）
2. **输入 "Java: Create Java Project"**
3. **选择项目类型：**
   - No Build Tools（纯 Java）
   - Maven
   - Gradle
4. **选择项目位置**
5. **输入项目名称**

### Eclipse

1. **File → New → Java Project**
2. **输入项目名称**
3. **选择 JRE 版本**
4. **点击 "Finish"**

---

## 📝 方法 5：手动创建项目

### 创建标准 Java 项目结构

```bash
# 1. 创建项目目录
mkdir my-manual-app
cd my-manual-app

# 2. 创建目录结构
mkdir -p src/main/java/com/example
mkdir -p src/main/resources
mkdir -p src/test/java/com/example
mkdir -p src/test/resources

# 3. 创建 pom.xml（Maven 项目）
cat > pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.example</groupId>
    <artifactId>my-manual-app</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
</project>
EOF

# 4. 创建主类
cat > src/main/java/com/example/App.java << 'EOF'
package com.example;

public class App {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
EOF

# 5. 创建测试类
cat > src/test/java/com/example/AppTest.java << 'EOF'
package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AppTest {
    @Test
    public void testApp() {
        assertTrue(true);
    }
}
EOF
```

### 验证手动创建的项目

```bash
# 编译项目
mvn compile

# 运行测试
mvn test

# 运行主类
mvn exec:java -Dexec.mainClass="com.example.App"
```

---

## 🎯 项目生成后的下一步

### 1. 配置项目 Java 版本

#### 使用 SDKMAN 配置项目版本

```bash
# 在项目根目录创建 .sdkmanrc
echo "java=17.0.2-tem" > .sdkmanrc
echo "maven=3.9.4" >> .sdkmanrc

# 激活项目配置
sdk env

# 验证
java -version
mvn -version
```

#### 在 pom.xml 中配置

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <java.version>17</java.version>
</properties>
```

### 2. 添加依赖

编辑 `pom.xml`，添加需要的依赖：

```xml
<dependencies>
    <!-- 添加依赖 -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
</dependencies>
```

然后下载依赖：
```bash
mvn install
```

### 3. 编写代码

在 `src/main/java/com/example/` 目录下创建 Java 类。

### 4. 运行项目

```bash
# 编译
mvn compile

# 运行主类
mvn exec:java -Dexec.mainClass="com.example.App"

# 或打包后运行
mvn package
java -cp target/my-app-1.0-SNAPSHOT.jar com.example.App
```

### 5. 运行测试

```bash
# 运行所有测试
mvn test

# 运行特定测试
mvn test -Dtest=AppTest
```

---

## 📊 方法对比

| 方法 | 优点 | 缺点 | 适用场景 |
|------|------|------|---------|
| **Maven** | 简单快速，生态成熟 | 需要了解 Maven 命令 | 标准 Java 项目 |
| **Gradle** | 构建速度快，灵活 | 学习曲线较陡 | 复杂项目 |
| **Spring Initializr** | 一键生成，包含依赖 | 仅限 Spring Boot | Spring Boot 项目 |
| **IDE** | 可视化，集成度高 | 依赖 IDE | 初学者，IDE 用户 |
| **手动创建** | 完全控制，学习价值高 | 繁琐，容易出错 | 学习目的 |

---

## 💡 推荐方案

### 新手推荐

1. **使用 Maven + 交互式生成**
   ```bash
   mvn archetype:generate
   ```

2. **或使用 IDE（IntelliJ IDEA 或 VS Code）**
   - 可视化界面
   - 自动配置
   - 集成工具

### 有经验开发者推荐

1. **标准 Java 项目：Maven**
   ```bash
   mvn archetype:generate -DgroupId=com.example -DartifactId=my-app -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
   ```

2. **Spring Boot 项目：Spring Initializr**
   - Web 界面：https://start.spring.io/
   - 或命令行：使用 curl

3. **复杂项目：Gradle**
   ```bash
   gradle init
   ```

---

## 🔧 常见问题

### Q1: Maven 生成项目很慢怎么办？

**A:** 配置国内镜像（阿里云）：
```bash
# 编辑 ~/.m2/settings.xml
<mirrors>
    <mirror>
        <id>aliyun</id>
        <mirrorOf>central</mirrorOf>
        <name>Aliyun Maven</name>
        <url>https://maven.aliyun.com/repository/public</url>
    </mirror>
</mirrors>
```

### Q2: 如何生成多模块项目？

**A:** 使用 Maven 父项目：
```bash
# 1. 生成父项目
mvn archetype:generate -DgroupId=com.example -DartifactId=parent-project -DarchetypeArtifactId=maven-archetype-pom -DinteractiveMode=false

# 2. 在父项目中创建子模块
cd parent-project
mvn archetype:generate -DgroupId=com.example -DartifactId=module1 -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false
```

### Q3: 生成的项目结构不对怎么办？

**A:** 检查：
1. 确认使用的 Archetype 是否正确
2. 检查 Maven 版本：`mvn -version`
3. 清理并重新生成：`mvn clean archetype:generate`

### Q4: 如何生成 Kotlin 项目？

**A:** 使用 Kotlin Archetype：
```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-kotlin-app \
  -DarchetypeArtifactId=kotlin-archetype-jvm \
  -DinteractiveMode=false
```

---

## 📚 总结

### 快速开始（推荐）

```bash
# 1. 生成标准 Java 项目
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

# 2. 进入项目
cd my-app

# 3. 配置项目 Java 版本
echo "java=17.0.2-tem" > .sdkmanrc
sdk env

# 4. 编译和运行
mvn compile
mvn exec:java -Dexec.mainClass="com.example.App"
```

### 下一步学习

1. **学习 Maven 使用**：查看 `Maven使用指南.md`
2. **学习 Java 语法**：查看 `Java语法指南.md`
3. **学习项目结构**：理解 Maven 标准目录结构
4. **添加依赖**：在 `pom.xml` 中添加需要的库

---

**祝开发顺利！💪**

*最后更新：2024年*
