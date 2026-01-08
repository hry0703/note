# Maven 项目生成详解

## 📋 目录

- [Maven Archetype 命令详解](#maven-archetype-命令详解)
- [pom.xml 文件详解](#pomxml-文件详解)
- [常用 Archetype 模板](#常用-archetype-模板)
- [实战示例](#实战示例)

---

## 🚀 Maven Archetype 命令详解

### 基础命令格式

```bash
mvn archetype:generate [参数]
```

### 完整命令示例

```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DarchetypeVersion=1.4 \
  -DinteractiveMode=false \
  -Dversion=1.0-SNAPSHOT \
  -Dpackage=com.example
```

### 参数详解

#### 1. `-DgroupId=<组织ID>`

**作用**：定义项目的组织或公司标识符

**格式**：通常使用反向域名（reverse domain name）

**示例**：
```bash
-DgroupId=com.example          # 示例公司
-DgroupId=org.apache           # Apache 组织
-DgroupId=com.github.username  # GitHub 用户
-DgroupId=cn.edu.university    # 大学/机构
```

**在项目中的体现**：
- 生成的 Java 代码的包名基础
- 例如：`groupId=com.example` → 包名 `com.example`
- 生成的类路径：`src/main/java/com/example/App.java`

**类比理解**：
- 类似 npm 包的 `@company/package-name` 中的组织名
- 类似 Docker 镜像的命名空间

---

#### 2. `-DartifactId=<项目名称>`

**作用**：定义项目的唯一标识符（项目名称）

**格式**：小写字母、数字、连字符，不能有空格

**示例**：
```bash
-DartifactId=my-app           # 标准格式
-DartifactId=user-service     # 服务名称
-DartifactId=payment-gateway  # 网关名称
-DartifactId=webapp           # Web 应用
```

**在项目中的体现**：
- 项目根目录名称
- 生成的 JAR 文件名：`my-app-1.0.jar`
- 在 `pom.xml` 中作为 `<artifactId>` 的值

**命名规范**：
- ✅ 推荐：`my-app`、`user-service`、`payment-api`
- ❌ 避免：`My App`（空格）、`MyApp`（驼峰，虽然可用但不推荐）

---

#### 3. `-DarchetypeArtifactId=<模板ID>`

**作用**：指定要使用的项目模板（Archetype）

**常用模板**：

| Archetype ID | 说明 | 适用场景 |
|-------------|------|---------|
| `maven-archetype-quickstart` | 标准 Java 项目 | 普通 Java 应用 |
| `maven-archetype-webapp` | Web 应用 | Servlet/JSP 项目 |
| `maven-archetype-j2ee-simple` | J2EE 项目 | 企业级应用 |
| `maven-archetype-site` | 站点项目 | 文档站点 |

**示例**：
```bash
# 标准 Java 项目
-DarchetypeArtifactId=maven-archetype-quickstart

# Web 应用
-DarchetypeArtifactId=maven-archetype-webapp

# 如果不指定，Maven 会列出所有可用模板让你选择
```

**自定义 Archetype**：
```bash
# 使用 Spring Boot 模板
-DarchetypeGroupId=org.springframework.boot \
-DarchetypeArtifactId=spring-boot-starter-parent \
-DarchetypeVersion=3.1.0
```

---

#### 4. `-DarchetypeGroupId=<模板组织ID>`

**作用**：指定 Archetype 模板的组织 ID（通常与 `archetypeArtifactId` 配合使用）

**默认值**：`org.apache.maven.archetypes`（Maven 官方模板）

**示例**：
```bash
# 使用默认（Maven 官方模板）
-DarchetypeGroupId=org.apache.maven.archetypes

# 使用 Spring Boot 模板
-DarchetypeGroupId=org.springframework.boot

# 使用自定义模板
-DarchetypeGroupId=com.company.archetypes
```

---

#### 5. `-DarchetypeVersion=<模板版本>`

**作用**：指定 Archetype 模板的版本号

**默认值**：最新版本（如果不指定）

**示例**：
```bash
-DarchetypeVersion=1.4              # 指定版本
-DarchetypeVersion=1.0-SNAPSHOT    # 快照版本
# 不指定则使用最新版本
```

**建议**：通常不需要指定，使用最新版本即可

---

#### 6. `-DinteractiveMode=<true|false>`

**作用**：是否启用交互模式

**选项**：
- `true`：交互模式，Maven 会提示你输入各个参数
- `false`：非交互模式，使用命令行参数，适合脚本自动化

**示例**：
```bash
# 非交互模式（推荐用于脚本）
-DinteractiveMode=false

# 交互模式（会提示输入）
-DinteractiveMode=true
# 或者不指定此参数，默认就是交互模式
```

**交互模式示例**：
```bash
$ mvn archetype:generate
[INFO] Generating project in Interactive mode
Define value for property 'groupId': com.example
Define value for property 'artifactId': my-app
Define value for property 'version' 1.0-SNAPSHOT: : 1.0
Define value for property 'package' com.example: : 
```

---

#### 7. `-Dversion=<版本号>`

**作用**：指定项目的初始版本号

**默认值**：`1.0-SNAPSHOT`

**版本号规范**：
- `1.0`：正式版本
- `1.0-SNAPSHOT`：开发版本（快照）
- `1.0.0`：语义化版本
- `0.1.0`：初始开发版本

**示例**：
```bash
-Dversion=1.0                # 正式版本
-Dversion=1.0-SNAPSHOT       # 开发版本（默认）
-Dversion=0.1.0              # 初始版本
```

**在项目中的体现**：
- 在 `pom.xml` 中作为 `<version>` 的值
- 生成的 JAR 文件名：`my-app-1.0.jar`

---

#### 8. `-Dpackage=<包名>`

**作用**：指定 Java 代码的包名（可选，通常与 groupId 相同）

**默认值**：与 `groupId` 相同

**示例**：
```bash
-Dpackage=com.example        # 与 groupId 相同
-Dpackage=com.example.app    # 不同的包名
```

**注意**：
- 如果不指定，默认使用 `groupId` 的值
- 包名必须符合 Java 命名规范（小写字母、点分隔）

---

### 命令参数总结表

| 参数 | 必需 | 默认值 | 说明 |
|------|------|--------|------|
| `groupId` | ✅ | 无 | 组织/公司标识符 |
| `artifactId` | ✅ | 无 | 项目名称 |
| `archetypeArtifactId` | ❌ | 交互选择 | 项目模板 ID |
| `archetypeGroupId` | ❌ | `org.apache.maven.archetypes` | 模板组织 ID |
| `archetypeVersion` | ❌ | 最新版本 | 模板版本 |
| `interactiveMode` | ❌ | `true` | 是否交互模式 |
| `version` | ❌ | `1.0-SNAPSHOT` | 项目版本 |
| `package` | ❌ | 与 `groupId` 相同 | Java 包名 |

---

## 📄 pom.xml 文件详解

### pom.xml 是什么？

`pom.xml`（Project Object Model）是 Maven 项目的核心配置文件，类似于：
- npm 的 `package.json`
- Python 的 `requirements.txt` 或 `pyproject.toml`
- Gradle 的 `build.gradle`

### 完整的 pom.xml 结构

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <!-- ========== 1. 模型版本 ========== -->
    <modelVersion>4.0.0</modelVersion>
    
    <!-- ========== 2. 项目坐标（必需） ========== -->
    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    
    <!-- ========== 3. 项目信息 ========== -->
    <name>My Application</name>
    <description>这是一个示例项目</description>
    <url>https://www.example.com</url>
    
    <!-- ========== 4. 属性配置 ========== -->
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    </properties>
    
    <!-- ========== 5. 父项目（可选） ========== -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.0</version>
        <relativePath/>
    </parent>
    
    <!-- ========== 6. 依赖管理 ========== -->
    <dependencies>
        <!-- 依赖项 -->
    </dependencies>
    
    <!-- ========== 7. 依赖管理（版本统一管理） ========== -->
    <dependencyManagement>
        <dependencies>
            <!-- 依赖版本定义 -->
        </dependencies>
    </dependencyManagement>
    
    <!-- ========== 8. 构建配置 ========== -->
    <build>
        <plugins>
            <!-- 插件配置 -->
        </plugins>
    </build>
    
    <!-- ========== 9. 仓库配置 ========== -->
    <repositories>
        <!-- 仓库地址 -->
    </repositories>
    
    <!-- ========== 10. 插件仓库 ========== -->
    <pluginRepositories>
        <!-- 插件仓库地址 -->
    </pluginRepositories>
    
    <!-- ========== 11. 开发者信息 ========== -->
    <developers>
        <!-- 开发者列表 -->
    </developers>
    
    <!-- ========== 12. 许可证信息 ========== -->
    <licenses>
        <!-- 许可证 -->
    </licenses>
    
</project>
```

---

### 各部分详细说明

#### 1. XML 声明和命名空间

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
```

**说明**：
- XML 版本和编码声明
- Maven POM 命名空间定义
- 通常不需要修改，保持默认即可

---

#### 2. 模型版本（modelVersion）

```xml
<modelVersion>4.0.0</modelVersion>
```

**说明**：
- 固定值，表示 POM 模型的版本
- Maven 2.x/3.x 都使用 `4.0.0`
- **必须包含**，但值固定不变

---

#### 3. 项目坐标（Project Coordinates）

这是 Maven 项目的唯一标识，类似于坐标系统。

##### 3.1 groupId（组织ID）

```xml
<groupId>com.example</groupId>
```

**说明**：
- 组织或公司的唯一标识符
- 通常使用反向域名
- 与生成项目时的 `-DgroupId` 参数对应

**示例**：
```xml
<groupId>com.example</groupId>           <!-- 示例公司 -->
<groupId>org.apache</groupId>            <!-- Apache 组织 -->
<groupId>com.github.username</groupId>   <!-- GitHub 用户 -->
<groupId>cn.edu.university</groupId>     <!-- 教育机构 -->
```

---

##### 3.2 artifactId（项目ID）

```xml
<artifactId>my-app</artifactId>
```

**说明**：
- 项目的唯一标识符（项目名称）
- 通常使用小写字母和连字符
- 与生成项目时的 `-DartifactId` 参数对应
- 会作为项目目录名和 JAR 文件名的一部分

**示例**：
```xml
<artifactId>my-app</artifactId>
<artifactId>user-service</artifactId>
<artifactId>payment-gateway</artifactId>
```

---

##### 3.3 version（版本号）

```xml
<version>1.0-SNAPSHOT</version>
```

**说明**：
- 项目的版本号
- `SNAPSHOT` 表示开发版本（快照版本）
- 正式版本通常不带 `SNAPSHOT`

**版本号规范**：
```xml
<version>1.0-SNAPSHOT</version>    <!-- 开发版本 -->
<version>1.0</version>              <!-- 正式版本 -->
<version>1.0.0</version>            <!-- 语义化版本 -->
<version>0.1.0</version>           <!-- 初始版本 -->
```

**版本号规则**：
- `SNAPSHOT`：开发中，可能随时变化
- 正式版本：稳定版本，发布后不应修改
- 版本号格式：`主版本.次版本.修订版本`

---

##### 3.4 packaging（打包类型）

```xml
<packaging>jar</packaging>
```

**说明**：
- 指定项目的打包类型
- 默认值是 `jar`（如果不指定）

**常用类型**：

| 类型 | 说明 | 适用场景 |
|------|------|---------|
| `jar` | Java 归档文件 | 普通 Java 应用 |
| `war` | Web 归档文件 | Web 应用（Servlet/JSP） |
| `pom` | 父项目 | 多模块项目的父 POM |
| `ear` | 企业归档文件 | 企业级应用 |
| `maven-plugin` | Maven 插件 | Maven 插件项目 |

**示例**：
```xml
<packaging>jar</packaging>      <!-- Java 应用 -->
<packaging>war</packaging>      <!-- Web 应用 -->
<packaging>pom</packaging>      <!-- 父项目 -->
```

---

#### 4. 项目信息

```xml
<name>My Application</name>
<description>这是一个示例项目</description>
<url>https://www.example.com</url>
```

**说明**：
- `<name>`：项目显示名称（可选）
- `<description>`：项目描述（可选）
- `<url>`：项目主页 URL（可选）

**示例**：
```xml
<name>用户服务系统</name>
<description>提供用户注册、登录、信息管理等功能</description>
<url>https://github.com/username/user-service</url>
```

---

#### 5. 属性配置（properties）

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
</properties>
```

**说明**：
- 定义项目属性，可以在其他地方使用 `${属性名}` 引用
- 类似于变量定义

**常用属性**：

| 属性 | 说明 | 示例 |
|------|------|------|
| `maven.compiler.source` | Java 源代码版本 | `17`、`11`、`8` |
| `maven.compiler.target` | 编译目标版本 | `17`、`11`、`8` |
| `project.build.sourceEncoding` | 源代码编码 | `UTF-8` |
| `project.reporting.outputEncoding` | 报告输出编码 | `UTF-8` |

**自定义属性示例**：
```xml
<properties>
    <java.version>17</java.version>
    <spring.version>6.0.9</spring.version>
    <junit.version>5.10.0</junit.version>
</properties>

<!-- 在其他地方使用 -->
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-core</artifactId>
    <version>${spring.version}</version>
</dependency>
```

---

#### 6. 父项目（parent）

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.1.0</version>
    <relativePath/>
</parent>
```

**说明**：
- 定义父项目，子项目可以继承父项目的配置
- 常用于多模块项目或使用框架（如 Spring Boot）

**作用**：
- 继承父项目的依赖版本
- 继承父项目的插件配置
- 统一管理版本号

**示例**：
```xml
<!-- Spring Boot 项目 -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.1.0</version>
    <relativePath/>
</parent>

<!-- 多模块项目的父 POM -->
<parent>
    <groupId>com.example</groupId>
    <artifactId>parent-project</artifactId>
    <version>1.0.0</version>
    <relativePath>../pom.xml</relativePath>
</parent>
```

---

#### 7. 依赖管理（dependencies）

```xml
<dependencies>
    <!-- 依赖项列表 -->
</dependencies>
```

**说明**：
- 定义项目所需的依赖库
- 类似于 npm 的 `dependencies` 和 `devDependencies`

##### 7.1 基础依赖结构

```xml
<dependency>
    <groupId>组织ID</groupId>
    <artifactId>项目ID</artifactId>
    <version>版本号</version>
    <scope>作用域</scope>
    <optional>是否可选</optional>
    <exclusions>
        <!-- 排除的传递依赖 -->
    </exclusions>
</dependency>
```

##### 7.2 依赖示例

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
    
    <!-- JUnit（测试框架，仅测试时使用） -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```

##### 7.3 依赖作用域（scope）

| Scope | 说明 | 类比 npm |
|-------|------|----------|
| `compile` | 默认，编译和运行时都需要 | `dependencies` |
| `provided` | 编译时需要，运行时由容器提供 | - |
| `runtime` | 运行时需要，编译时不需要 | - |
| `test` | 仅测试时需要 | `devDependencies` |
| `system` | 使用本地 jar 文件 | - |

**示例**：
```xml
<!-- 编译和运行时都需要 -->
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
    <!-- scope 默认为 compile，可以不写 -->
</dependency>

<!-- 仅测试时使用 -->
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.0</version>
    <scope>test</scope>
</dependency>

<!-- 编译时需要，运行时由容器提供（如 Servlet API） -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>javax.servlet-api</artifactId>
    <version>4.0.1</version>
    <scope>provided</scope>
</dependency>
```

##### 7.4 排除传递依赖

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>3.1.0</version>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

---

#### 8. 依赖版本管理（dependencyManagement）

```xml
<dependencyManagement>
    <dependencies>
        <!-- 定义依赖版本，但不实际引入 -->
    </dependencies>
</dependencyManagement>
```

**说明**：
- 统一管理依赖版本
- 子项目引用时不需要指定版本号
- 常用于父 POM 或多模块项目

**示例**：
```xml
<!-- 父 POM 中定义版本 -->
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>6.0.9</version>
        </dependency>
    </dependencies>
</dependencyManagement>

<!-- 子项目中引用时不需要版本号 -->
<dependencies>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-core</artifactId>
        <!-- 版本号从父 POM 继承 -->
    </dependency>
</dependencies>
```

---

#### 9. 构建配置（build）

```xml
<build>
    <plugins>
        <!-- 插件配置 -->
    </plugins>
    <resources>
        <!-- 资源文件配置 -->
    </resources>
</build>
```

##### 9.1 插件配置

```xml
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
                <encoding>UTF-8</encoding>
            </configuration>
        </plugin>
        
        <!-- Spring Boot 打包插件 -->
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <version>3.1.0</version>
        </plugin>
    </plugins>
</build>
```

**常用插件**：

| 插件 | 作用 |
|------|------|
| `maven-compiler-plugin` | 编译 Java 代码 |
| `maven-surefire-plugin` | 运行测试 |
| `maven-jar-plugin` | 打包 JAR 文件 |
| `maven-war-plugin` | 打包 WAR 文件 |
| `spring-boot-maven-plugin` | Spring Boot 打包插件 |

##### 9.2 资源文件配置

```xml
<build>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
            <filtering>true</filtering>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.xml</include>
            </includes>
        </resource>
    </resources>
</build>
```

---

#### 10. 仓库配置（repositories）

```xml
<repositories>
    <repository>
        <id>central</id>
        <name>Maven Central Repository</name>
        <url>https://repo1.maven.org/maven2</url>
        <releases>
            <enabled>true</enabled>
        </releases>
        <snapshots>
            <enabled>false</enabled>
        </snapshots>
    </repository>
    
    <!-- 阿里云镜像（国内加速） -->
    <repository>
        <id>aliyun</id>
        <name>Aliyun Maven Repository</name>
        <url>https://maven.aliyun.com/repository/public</url>
    </repository>
</repositories>
```

**说明**：
- 定义依赖下载的仓库地址
- 通常不需要配置，使用默认的 Maven Central
- 国内可以使用阿里云等镜像加速

---

#### 11. 开发者信息（developers）

```xml
<developers>
    <developer>
        <id>developer1</id>
        <name>张三</name>
        <email>zhangsan@example.com</email>
        <organization>Example Inc.</organization>
        <roles>
            <role>Architect</role>
            <role>Developer</role>
        </roles>
    </developer>
</developers>
```

**说明**：可选，记录项目开发者信息

---

#### 12. 许可证信息（licenses）

```xml
<licenses>
    <license>
        <name>Apache License, Version 2.0</name>
        <url>https://www.apache.org/licenses/LICENSE-2.0.txt</url>
        <distribution>repo</distribution>
    </license>
</licenses>
```

**说明**：可选，定义项目许可证

---

## 🎯 常用 Archetype 模板

### 1. maven-archetype-quickstart（标准 Java 项目）

**命令**：
```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-app \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false
```

**生成的项目结构**：
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

**适用场景**：普通 Java 应用程序

---

### 2. maven-archetype-webapp（Web 应用）

**命令**：
```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-webapp \
  -DarchetypeArtifactId=maven-archetype-webapp \
  -DinteractiveMode=false
```

**生成的项目结构**：
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

**适用场景**：Servlet/JSP Web 应用

---

### 3. Spring Boot 项目

**命令**：
```bash
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-spring-app \
  -DarchetypeGroupId=org.springframework.boot \
  -DarchetypeArtifactId=spring-boot-starter-parent \
  -DarchetypeVersion=3.1.0 \
  -DinteractiveMode=false
```

**或者使用 Spring Initializr**：
```bash
# 使用 curl 下载 Spring Boot 项目
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
  -o my-spring-app.zip
```

**适用场景**：Spring Boot 应用

---

## 💡 实战示例

### 示例 1：创建标准 Java 项目

```bash
# 1. 生成项目
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=calculator \
  -DarchetypeArtifactId=maven-archetype-quickstart \
  -DinteractiveMode=false

# 2. 进入项目目录
cd calculator

# 3. 查看生成的项目结构
tree -L 4

# 4. 编译项目
mvn compile

# 5. 运行测试
mvn test

# 6. 打包项目
mvn package
```

---

### 示例 2：创建 Web 应用项目

```bash
# 1. 生成 Web 项目
mvn archetype:generate \
  -DgroupId=com.example \
  -DartifactId=my-webapp \
  -DarchetypeArtifactId=maven-archetype-webapp \
  -DinteractiveMode=false

# 2. 进入项目目录
cd my-webapp

# 3. 添加 Servlet API 依赖（编辑 pom.xml）
# 在 pom.xml 的 <dependencies> 中添加：
# <dependency>
#     <groupId>javax.servlet</groupId>
#     <artifactId>javax.servlet-api</artifactId>
#     <version>4.0.1</version>
#     <scope>provided</scope>
# </dependency>

# 4. 打包为 WAR 文件
mvn package
# 生成：target/my-webapp.war
```

---

### 示例 3：完整的 pom.xml 示例

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    
    <modelVersion>4.0.0</modelVersion>
    
    <!-- 项目坐标 -->
    <groupId>com.example</groupId>
    <artifactId>my-app</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    
    <!-- 项目信息 -->
    <name>我的应用</name>
    <description>这是一个示例 Maven 项目</description>
    
    <!-- 属性配置 -->
    <properties>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring.version>6.0.9</spring.version>
    </properties>
    
    <!-- 依赖管理 -->
    <dependencies>
        <!-- Spring Core -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>${spring.version}</version>
        </dependency>
        
        <!-- Gson -->
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>2.10.1</version>
        </dependency>
        
        <!-- JUnit 测试 -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>5.10.0</version>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <!-- 构建配置 -->
    <build>
        <plugins>
            <!-- 编译插件 -->
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

---

## 📚 总结

### 命令参数记忆口诀

```
groupId      → 组织标识（com.example）
artifactId   → 项目名称（my-app）
archetype    → 项目模板（quickstart/webapp）
interactive  → 交互模式（false=自动，true=手动）
version      → 项目版本（1.0-SNAPSHOT）
package      → Java 包名（通常与 groupId 相同）
```

### pom.xml 核心部分

1. **项目坐标**：`groupId`、`artifactId`、`version`、`packaging`
2. **依赖管理**：`dependencies`、`dependencyManagement`
3. **构建配置**：`build`、`plugins`
4. **属性配置**：`properties`

### 快速参考

| 操作 | 命令 |
|------|------|
| 生成项目 | `mvn archetype:generate` |
| 编译项目 | `mvn compile` |
| 运行测试 | `mvn test` |
| 打包项目 | `mvn package` |
| 安装到本地 | `mvn install` |
| 清理项目 | `mvn clean` |

---

**祝学习顺利！💪**

*最后更新：2024年*

