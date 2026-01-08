# Maven 使用指南

## 📖 什么是 Maven？

Maven 是 Java 项目管理和构建工具，类似于前端的 **npm** 或 **yarn**。它可以帮你：

- 📦 **依赖管理**：自动下载和管理项目依赖（类似 `npm install`）
- 🔨 **项目构建**：编译、测试、打包项目（类似 `npm run build`）
- 📋 **项目结构标准化**：统一的目录结构
- 🔄 **生命周期管理**：定义构建流程

---

## 🆚 Maven vs npm 对比

| 功能 | npm (前端) | Maven (Java) |
|------|-----------|--------------|
| **配置文件** | `package.json` | `pom.xml` |
| **依赖安装** | `npm install` | `mvn install` |
| **运行脚本** | `npm run <script>` | `mvn <goal>` |
| **依赖仓库** | npm registry | Maven Central Repository |
| **本地缓存** | `~/.npm` | `~/.m2/repository` |
| **构建工具** | webpack/vite | Maven 内置 |
| **打包** | `npm pack` | `mvn package` |

---

## 🚀 安装 Maven

### 方式 1：使用 SDKMAN（推荐）

```bash
# 查看可用版本
sdk list maven

# 安装 Maven
sdk install maven 3.9.4

# 设置默认版本
sdk default maven 3.9.4

# 验证安装
mvn -version
```

### 方式 2：手动安装

**macOS（使用 Homebrew）：**
```bash
brew install maven
```

**Linux：**
```bash
# Ubuntu/Debian
sudo apt-get install maven

# CentOS/RHEL
sudo yum install maven
```

**Windows：**
1. 下载 Maven：https://maven.apache.org/download.cgi
2. 解压到目录（如 `C:\Program Files\Apache\maven`）
3. 配置环境变量：
   - `MAVEN_HOME=C:\Program Files\Apache\maven`
   - `PATH=%MAVEN_HOME%\bin;%PATH%`

---

## 📁 Maven 项目结构

Maven 使用标准的目录结构（约定优于配置）：

```
my-project/
├── pom.xml                 # 项目配置文件（类似 package.json）
├── src/
│   ├── main/
│   │   ├── java/           # 源代码目录
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── App.java
│   │   └── resources/      # 资源文件（配置文件等）
│   │       └── application.properties
│   └── test/
│       ├── java/            # 测试代码目录
│       │   └── com/
│       │       └── example/
│       │           └── AppTest.java
│       └── resources/       # 测试资源文件
└── target/                  # 编译输出目录（类似 dist/）
    ├── classes/
    └── my-project-1.0.jar
```

**对比前端项目结构：**
```
frontend-project/
├── package.json            # 类似 pom.xml
├── src/                    # 源代码
├── public/                 # 资源文件
└── dist/                   # 构建输出（类似 target/）
```

### 📦 Java 包结构详解（com/example/）

在 Maven 项目结构中，`src/main/java/com/example/` 这样的目录结构是 **Java 包（Package）结构**。

#### 包结构的作用

Java 使用包来组织代码，类似于：
- 📁 文件夹用于组织文件
- 🏷️ 命名空间用于避免命名冲突
- 📚 模块化组织代码

#### 两个层级的含义

```
src/main/java/
└── com/          ← 第一层：组织/公司标识
    └── example/  ← 第二层：项目/模块标识
        └── App.java
```

**第一层：`com/`（组织类型）**
- **含义**：组织/公司类型标识
- **常见类型**：
  - `com` = 商业组织（commercial）
  - `org` = 非营利组织（organization）
  - `edu` = 教育机构（education）
  - `cn` = 国家代码（如中国）
  - `net` = 网络组织

**第二层：`example/`（组织名称）**
- **含义**：公司/组织名称或项目标识
- **示例**：
  - `example` = 示例公司
  - `google` = Google 公司
  - `apache` = Apache 组织
  - `github` = GitHub

#### 完整的包名结构

```
com.example.App.java
│   │      │
│   │      └─ 类名（App）
│   └─────── 公司/组织名（example）
└─────────── 组织类型（com）
```

**在代码中的体现：**
```java
package com.example;  // 完整的包名声明

public class App {
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
```

#### 与 Maven groupId 的对应关系

在生成项目时，`groupId` 决定了包名结构：

```bash
mvn archetype:generate \
  -DgroupId=com.example \    ← 这里定义了包名基础
  -DartifactId=my-app
```

**对应关系：**
- `groupId=com.example` → 包名就是 `com.example`
- 生成的类路径：`src/main/java/com/example/App.java`
- 类中的包声明：`package com.example;`

**实际示例：**

| groupId | 包名 | 目录结构 |
|---------|------|----------|
| `com.example` | `com.example` | `src/main/java/com/example/` |
| `org.apache` | `org.apache` | `src/main/java/org/apache/` |
| `com.github.username` | `com.github.username` | `src/main/java/com/github/username/` |
| `cn.edu.university` | `cn.edu.university` | `src/main/java/cn/edu/university/` |

#### 为什么需要这样的结构？

1. **避免命名冲突**
   - 不同公司的类可以同名，但包名不同
   - 例如：`com.example.User` 和 `com.other.User` 不会冲突

2. **组织代码**
   - 按功能模块组织代码
   - 例如：`com.example.service`、`com.example.controller`

3. **访问控制**
   - Java 提供包级别的访问权限控制

#### 常见包结构示例

在实际项目中，包结构通常按功能模块组织：

```
com.example.myapp/
├── controller/    # 控制器层（处理 HTTP 请求）
├── service/       # 服务层（业务逻辑）
├── model/         # 数据模型层（实体类）
├── dao/           # 数据访问层（数据库操作）
├── util/          # 工具类
└── config/        # 配置类
```

对应的目录结构：
```
src/main/java/com/example/myapp/
├── controller/
│   └── UserController.java
├── service/
│   └── UserService.java
├── model/
│   └── User.java
├── dao/
│   └── UserDao.java
├── util/
│   └── StringUtils.java
└── config/
    └── AppConfig.java
```

**在代码中的使用：**
```java
// UserController.java
package com.example.myapp.controller;

import com.example.myapp.service.UserService;
import com.example.myapp.model.User;

public class UserController {
    private UserService userService;
    // ...
}
```

---

## 📝 pom.xml 配置文件

`pom.xml`（Project Object Model）是 Maven 的核心配置文件，类似于前端的 `package.json`。

### 基础 pom.xml 示例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <!-- 项目基本信息 -->
    <modelVersion>4.0.0</modelVersion>
    
    <!-- 项目坐标（类似 package.json 的 name） -->
    <groupId>com.example</groupId>        <!-- 组织/公司ID -->
    <artifactId>my-project</artifactId>  <!-- 项目名称 -->
    <version>1.0.0</version>             <!-- 版本号 -->
    <packaging>jar</packaging>           <!-- 打包类型：jar/war/pom -->
    
    <!-- 项目属性 -->
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <!-- 依赖列表（类似 package.json 的 dependencies） -->
    <dependencies>
        <!-- 添加依赖 -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>6.0.9</version>
        </dependency>
    </dependencies>
    
    <!-- 构建配置 -->
    <build>
        <plugins>
            <!-- Maven 编译插件 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>17</source>
                    <target>17</target>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

### 对比 package.json

```json
// package.json
{
  "name": "my-project",           // 类似 artifactId
  "version": "1.0.0",             // 类似 version
  "dependencies": {               // 类似 dependencies
    "react": "^18.0.0"
  },
  "scripts": {                   // Maven 使用 goals
    "build": "webpack"
  }
}
```

---

## 📦 依赖管理

### 添加依赖

**在 pom.xml 中添加：**

```xml
<dependencies>
    <!-- Spring Boot Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
        <version>3.1.0</version>
    </dependency>
    
    <!-- Gson（JSON 处理） -->
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
    
    <!-- JUnit（测试框架） -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**对比 npm：**
```bash
# npm
npm install react
npm install --save-dev jest

# Maven（在 pom.xml 中添加后）
mvn install  # 自动下载依赖
```

### 依赖范围（Scope）

类似 npm 的 `devDependencies` 和 `dependencies`：

| Scope | 说明 | npm 类比 |
|-------|------|----------|
| **compile** | 默认，编译和运行时都需要 | `dependencies` |
| **provided** | 编译时需要，运行时由容器提供 | - |
| **runtime** | 运行时需要，编译时不需要 | - |
| **test** | 仅测试时需要 | `devDependencies` |
| **system** | 使用本地 jar 文件 | - |

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>  <!-- 仅测试时使用 -->
</dependency>
```

### 查找依赖坐标

**方式 1：Maven Central Repository**
- 网址：https://mvnrepository.com/
- 搜索依赖，复制坐标

**方式 2：IDE 自动提示**
- IntelliJ IDEA、Eclipse 等 IDE 会自动提示

**方式 3：命令行搜索**
```bash
# 使用 Maven 搜索插件（需要先安装）
mvn dependency:search -Dartifact=spring-boot-starter-web
```

---

## 🔨 Maven 常用命令

### 基础命令

| 命令 | 说明 | npm 类比 |
|------|------|----------|
| `mvn clean` | 清理 target 目录 | `rm -rf dist` |
| `mvn compile` | 编译源代码 | `npm run build` |
| `mvn test` | 运行测试 | `npm test` |
| `mvn package` | 打包项目（生成 jar/war） | `npm pack` |
| `mvn install` | 安装到本地仓库 | `npm install` |
| `mvn deploy` | 部署到远程仓库 | `npm publish` |

### 生命周期命令

Maven 有内置的生命周期，执行命令会自动运行前面的阶段：

```bash
# 完整生命周期（按顺序执行）
mvn clean          # 清理
mvn validate       # 验证
mvn compile        # 编译（包含 validate）
mvn test           # 测试（包含 compile）
mvn package        # 打包（包含 test）
mvn install        # 安装到本地仓库（包含 package）
mvn deploy         # 部署（包含 install）
```

**常用组合：**
```bash
# 清理并打包
mvn clean package

# 清理、编译、测试、打包
mvn clean install

# 跳过测试打包（类似 npm run build --skip-tests）
mvn clean package -DskipTests
```

### 依赖相关命令

```bash
# 查看依赖树（类似 npm list）
mvn dependency:tree

# 查看依赖列表
mvn dependency:list

# 分析依赖
mvn dependency:analyze

# 复制依赖到指定目录
mvn dependency:copy-dependencies

# 下载源码
mvn dependency:sources
```

### 其他常用命令

```bash
# 查看项目信息
mvn help:effective-pom      # 查看有效 pom
mvn help:effective-settings # 查看有效设置

# 运行主类
mvn exec:java -Dexec.mainClass="com.example.App"

# 创建项目骨架
mvn archetype:generate

# 查看版本
mvn -version
```

---

## 🎯 实际使用示例

### 示例 1：创建新项目

**使用 Maven 模板创建：**

```bash
# 创建标准 Java 项目
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

# 进入项目目录
cd my-app
```

**项目结构：**
```
my-app/
├── pom.xml
└── src/
    ├── main/java/com/example/App.java
    └── test/java/com/example/AppTest.java
```

### 示例 2：添加依赖并构建

**1. 编辑 pom.xml，添加依赖：**

```xml
<dependencies>
    <dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.10.1</version>
    </dependency>
</dependencies>
```

**2. 下载依赖：**

```bash
mvn dependency:resolve
# 或
mvn install
```

**3. 编译项目：**

```bash
mvn compile
```

**4. 打包项目：**

```bash
mvn package
# 生成：target/my-app-1.0-SNAPSHOT.jar
```

### 示例 3：运行项目

**方式 1：运行主类**

```bash
# 编译
mvn compile

# 运行
mvn exec:java -Dexec.mainClass="com.example.App"
```

**方式 2：运行 JAR 文件**

```bash
# 打包
mvn package

# 运行（需要指定主类）
java -cp target/my-app-1.0-SNAPSHOT.jar com.example.App

# 或者创建可执行 JAR（需要配置插件）
java -jar target/my-app-1.0-SNAPSHOT.jar
```

---

## 🔧 Maven 配置

### settings.xml

Maven 的全局配置文件，类似 npm 的 `.npmrc`：

**位置：**
- 全局：`$MAVEN_HOME/conf/settings.xml`
- 用户：`~/.m2/settings.xml`（推荐）

**常用配置：**

```xml
<settings>
    <!-- 本地仓库路径（类似 npm 的缓存目录） -->
    <localRepository>${user.home}/.m2/repository</localRepository>
    
    <!-- 镜像配置（使用国内镜像加速） -->
    <mirrors>
        <mirror>
            <id>aliyun</id>
            <name>Aliyun Maven</name>
            <url>https://maven.aliyun.com/repository/public</url>
            <mirrorOf>central</mirrorOf>
        </mirror>
    </mirrors>
    
    <!-- 代理配置 -->
    <proxies>
        <proxy>
            <id>my-proxy</id>
            <active>true</active>
            <protocol>http</protocol>
            <host>proxy.example.com</host>
            <port>8080</port>
        </proxy>
    </proxies>
</settings>
```

### 国内镜像配置（加速下载）

**编辑 `~/.m2/settings.xml`：**

```xml
<settings>
    <mirrors>
        <!-- 阿里云镜像 -->
        <mirror>
            <id>aliyun</id>
            <mirrorOf>central</mirrorOf>
            <name>Aliyun Maven</name>
            <url>https://maven.aliyun.com/repository/public</url>
        </mirror>
    </mirrors>
</settings>
```

**其他国内镜像：**
- 阿里云：https://maven.aliyun.com/repository/public
- 腾讯云：https://mirrors.cloud.tencent.com/nexus/repository/maven-public/
- 华为云：https://repo.huaweicloud.com/repository/maven/

---

## 📚 Maven 仓库

### 仓库类型

1. **本地仓库（Local Repository）**
   - 位置：`~/.m2/repository`
   - 类似：`~/.npm` 或 `node_modules`

2. **中央仓库（Central Repository）**
   - 网址：https://repo1.maven.org/maven2/
   - 类似：npm registry

3. **远程仓库（Remote Repository）**
   - 公司内部仓库或第三方仓库

### 仓库搜索

- **Maven Central**：https://mvnrepository.com/
- **搜索依赖坐标**：在网站上搜索，复制到 pom.xml

---

## 🆚 Maven vs Gradle

| 特性 | Maven | Gradle |
|------|-------|--------|
| **配置文件** | XML (pom.xml) | Groovy/Kotlin DSL (build.gradle) |
| **学习曲线** | 较平缓 | 较陡 |
| **构建速度** | 较慢 | 更快（增量构建） |
| **灵活性** | 约定优于配置 | 更灵活 |
| **生态** | 成熟，广泛使用 | 逐渐流行 |

**选择建议：**
- **Maven**：适合初学者，配置简单，生态成熟
- **Gradle**：适合复杂项目，需要更灵活的构建

---

## 💡 最佳实践

### 1. 使用版本管理

```xml
<properties>
    <spring.version>6.0.9</spring.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
        <version>${spring.version}</version>
    </dependency>
</dependencies>
```

### 2. 使用父 POM

```xml
<!-- 子项目继承父项目配置 -->
<parent>
    <groupId>com.example</groupId>
    <artifactId>parent-project</artifactId>
    <version>1.0.0</version>
</parent>
```

### 3. 多模块项目

```
parent-project/
├── pom.xml              # 父 POM
├── module-a/
│   └── pom.xml
└── module-b/
    └── pom.xml
```

### 4. 清理构建

```bash
# 定期清理，避免缓存问题
mvn clean install
```

### 5. 使用 IDE 集成

- IntelliJ IDEA、Eclipse 等 IDE 都支持 Maven
- 可以图形化管理依赖和运行命令

---

## ❓ 常见问题

### Q1: 依赖下载慢怎么办？

**A:** 配置国内镜像（见上方"国内镜像配置"）

### Q2: 如何查看依赖冲突？

```bash
mvn dependency:tree
```

### Q3: 如何跳过测试？

```bash
mvn clean package -DskipTests
```

### Q4: 如何更新依赖？

```bash
# 强制更新
mvn clean install -U
```

### Q5: Maven 和 npm 的主要区别？

| 方面 | npm | Maven |
|------|-----|-------|
| **配置文件** | JSON | XML |
| **依赖管理** | 扁平化 | 树形结构 |
| **构建** | 需要 webpack 等 | 内置构建 |
| **脚本** | package.json scripts | Maven goals |

---

## 📖 学习资源

- **官方文档**：https://maven.apache.org/
- **Maven Central**：https://mvnrepository.com/
- **Maven 教程**：https://maven.apache.org/guides/

---

## 🎓 总结

Maven 是 Java 开发中必不可少的工具，类似于前端的 npm：

- ✅ **依赖管理**：自动下载和管理依赖
- ✅ **项目构建**：编译、测试、打包
- ✅ **标准化**：统一的项目结构
- ✅ **生命周期**：定义构建流程

**快速开始：**
```bash
# 1. 安装 Maven（使用 SDKMAN）
sdk install maven

# 2. 创建项目
mvn archetype:generate

# 3. 添加依赖到 pom.xml

# 4. 构建项目
mvn clean install
```

