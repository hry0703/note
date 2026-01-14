# Java Project

一个标准的 Java Maven 项目。

## 项目结构

```
java-project/
├── pom.xml                    # Maven 配置文件
├── .sdkmanrc                  # SDKMAN 版本配置
├── .gitignore                 # Git 忽略文件
├── README.md                   # 项目说明
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── hry/
    │   │           └── firstjava/
    │   │               └── App.java    # 主类
    │   └── resources/              # 资源文件
    └── test/
        ├── java/
        │   └── com/
        │       └── hry/
        │           └── firstjava/
        │               └── AppTest.java # 测试类
        └── resources/              # 测试资源文件
```

## 快速开始

### 1. 配置 Java 版本（如果使用 SDKMAN）

```bash
# 激活项目配置
sdk env

# 验证 Java 版本
java -version
```

### 2. 编译项目

```bash
mvn compile
```

### 3. 运行项目

```bash
# 方式 1：使用 Maven 插件运行
mvn exec:java

# 方式 2：编译后运行
mvn compile
java -cp target/classes com.hry.firstjava.App
```

### 4. 运行测试

```bash
mvn test
```

### 5. 打包项目

```bash
mvn package
# 生成：target/firstjava-1.0-SNAPSHOT.jar
```

## 开发说明

- **Java 版本**：17
- **构建工具**：Maven
- **测试框架**：JUnit 5

## 下一步

1. 在 `src/main/java/com/hry/firstjava/` 目录下创建你的 Java 类
2. 在 `pom.xml` 中添加需要的依赖
3. 编写测试代码在 `src/test/java/com/hry/firstjava/` 目录下
