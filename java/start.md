# Java 学习计划（前端程序员版）

## 📚 学习目标
- 掌握 Java 基础语法和面向对象编程
- 理解 Java 与 JavaScript 的核心差异
- 能够独立开发简单的 Java 应用程序
- 了解 Spring Boot 框架基础

---

## 🎯 学习路径（8-12周）

### 第一阶段：Java 基础（2-3周）

#### 1.1 环境搭建
- [ ] 安装 JDK（推荐 JDK 17 或 21 LTS 版本）
- [ ] 配置环境变量（JAVA_HOME, PATH）
- [ ] 选择 IDE（推荐 IntelliJ IDEA Community 或 VS Code）
- [ ] 创建第一个 Hello World 程序

##### Java 11 vs Java 17 版本对比

**Java 11（2018年发布，LTS 长期支持版本）**
- Oracle 支持到 2026年9月
- 最后一个免费商业使用的 Oracle JDK 版本
- 性能稳定，生态成熟

**Java 17（2021年发布，LTS 长期支持版本）**
- Oracle 支持到 2029年9月
- 目前推荐的 LTS 版本
- 性能更好，新特性更多

**主要区别对比：**

| 特性 | Java 11 | Java 17 |
|------|---------|---------|
| **发布时间** | 2018年9月 | 2021年9月 |
| **LTS 支持** | 支持到 2026年 | 支持到 2029年 |
| **性能** | 良好 | 更优（GC 改进） |
| **新语言特性** | 基础 | 更多（如 sealed classes, pattern matching） |
| **API 增强** | 基础 | 更多新 API |
| **安全性** | 良好 | 更好（安全更新） |
| **企业采用** | 广泛使用 | 逐渐成为主流 |

**Java 17 主要新特性（相比 Java 11）：**

1. **Sealed Classes（密封类）**
   ```java
   // Java 17 新特性
   public sealed class Shape 
       permits Circle, Rectangle, Triangle {
   }
   ```

2. **Pattern Matching for switch（Switch 模式匹配）**
   ```java
   // Java 17 预览特性
   String result = switch (obj) {
       case Integer i -> "整数: " + i;
       case String s -> "字符串: " + s;
       default -> "未知类型";
   };
   ```

3. **Text Blocks（文本块）改进**
   ```java
   // Java 17 支持更灵活的文本块
   String json = """
       {
           "name": "Java",
           "version": 17
       }
       """;
   ```

4. **Records（记录类）**
   ```java
   // Java 17 新特性，简化数据类
   public record Person(String name, int age) {}
   ```

5. **性能改进**
   - G1 垃圾收集器性能提升
   - 启动速度更快
   - 内存使用更优化

**选择建议：**

✅ **选择 Java 17 如果：**
- 新项目开发
- 想使用最新特性
- 需要长期支持（到 2029年）
- 追求更好的性能

✅ **选择 Java 11 如果：**
- 维护现有项目（已使用 Java 11）
- 团队/公司统一使用 Java 11
- 依赖的第三方库只支持 Java 11

**推荐：** 对于新学习者，建议直接使用 **Java 17**，因为：
- 支持时间更长（到 2029年）
- 新特性更丰富，学习价值更高
- 性能更好
- 是当前主流 LTS 版本

##### Java 版本管理工具（类似 nvm）

作为前端程序员，你可能熟悉 `nvm`（Node Version Manager）。Java 也有类似的工具来管理多个版本！

**1. SDKMAN（推荐，最类似 nvm）**

SDKMAN 是 Java 生态中最流行的版本管理工具，功能强大，使用简单。

**安装 SDKMAN（macOS/Linux）：**
```bash
# 安装 SDKMAN
curl -s "https://get.sdkman.io" | bash

# 重新加载 shell 配置
source "$HOME/.sdkman/bin/sdkman-init.sh"

# 验证安装
sdk version
```

**Windows 安装：**
- 需要先安装 Git Bash 或 WSL
- 然后使用相同的命令

**常用命令（对比 nvm）：**

| 功能 | nvm | SDKMAN |
|------|-----|--------|
| 列出可用版本 | `nvm list-remote` | `sdk list java` |
| 安装版本 | `nvm install 17` | `sdk install java 17.0.2-tem` |
| 列出已安装版本 | `nvm list` | `sdk list java | grep installed` |
| 切换版本 | `nvm use 17` | `sdk use java 17.0.2-tem` |
| 设置默认版本 | `nvm alias default 17` | `sdk default java 17.0.2-tem` |
| 当前使用版本 | `nvm current` | `sdk current java` |

**SDKMAN 使用示例：**
```bash
# 查看所有可用的 Java 版本
sdk list java

# 安装 Java 17（Temurin 发行版）
sdk install java 17.0.2-tem

# 安装 Java 11
sdk install java 11.0.19-tem

# 切换到 Java 17
sdk use java 17.0.2-tem

# 设置 Java 17 为默认版本
sdk default java 17.0.2-tem

# 查看当前使用的版本
sdk current java

# 卸载某个版本
sdk uninstall java 11.0.19-tem
```

**SDKMAN 的优势：**
- ✅ 支持多个 JDK 发行版（Oracle, OpenJDK, Temurin, Zulu 等）
- ✅ 可以管理其他工具（Maven, Gradle, Spring Boot CLI 等）
- ✅ 自动配置 JAVA_HOME
- ✅ 跨平台支持（macOS, Linux, Windows via WSL）


**推荐方案对比：**

| 工具 | 优点 | 缺点 | 推荐度 |
|------|------|------|--------|
| **SDKMAN** | 功能强大，自动下载安装，支持多工具 | 需要网络，首次安装稍慢 | ⭐⭐⭐⭐⭐ |
| **jenv** | 轻量级，快速 | 需要手动安装 JDK | ⭐⭐⭐⭐ |
| **手动切换** | 完全控制 | 麻烦，容易出错 | ⭐⭐ |

**建议：** 作为前端程序员，如果你熟悉 `nvm`，强烈推荐使用 **SDKMAN**，使用体验最接近！

**快速开始 SDKMAN：**
```bash
# 1. 安装 SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# 2. 安装 Java 17
sdk install java 17.0.2-tem

# 3. 设置为默认
sdk default java 17.0.2-tem

# 4. 验证
java -version
```

#### 1.2 基础语法（对比 JavaScript）
| Java | JavaScript | 说明 |
|------|-----------|------|
| `int num = 10;` | `let num = 10;` | Java 需要声明类型 |
| `String str = "hello";` | `let str = "hello";` | Java 字符串是对象 |
| `boolean flag = true;` | `let flag = true;` | 布尔值小写 |
| `int[] arr = {1,2,3};` | `let arr = [1,2,3];` | 数组语法不同 |
| `List<String> list = new ArrayList<>();` | `let list = [];` | Java 使用集合类 |

**重点学习：**
- 数据类型（8种基本类型 + 引用类型）
- 变量声明和作用域
- 运算符和表达式
- 控制流（if/else, switch, for, while）
- 数组和集合（ArrayList, HashMap）

#### 1.3 面向对象编程（OOP）
- **类与对象**：理解 `class` 和 `new` 关键字
- **封装**：访问修饰符（public, private, protected）
- **继承**：`extends` 关键字，方法重写
- **多态**：接口和抽象类
- **构造方法**：与 JavaScript 构造函数对比

**实践项目：**
- 创建一个 `Person` 类，包含姓名、年龄等属性
- 实现 `Student` 类继承 `Person`
- 使用接口定义 `Animal` 行为

---

### 第二阶段：Java 进阶特性（2-3周）

#### 2.1 异常处理
```java
try {
    // 可能出错的代码
} catch (Exception e) {
    // 异常处理
} finally {
    // 清理资源
}
```

#### 2.2 集合框架
- **List**：ArrayList, LinkedList
- **Set**：HashSet, TreeSet
- **Map**：HashMap, TreeMap
- **迭代器**：Iterator, forEach

#### 2.3 泛型（Generics）
```java
List<String> list = new ArrayList<>(); // 类型安全
```

#### 2.4 Lambda 表达式和 Stream API（类似 JavaScript 的箭头函数）
```java
// Java Lambda
list.stream()
    .filter(x -> x > 10)
    .map(x -> x * 2)
    .forEach(System.out::println);

// JavaScript 对比
list.filter(x => x > 10)
    .map(x => x * 2)
    .forEach(console.log);
```

#### 2.5 多线程基础
- Thread 类和 Runnable 接口
- 同步和锁的概念

**实践项目：**
- 实现一个简单的图书管理系统（增删改查）
- 使用集合存储数据
- 添加异常处理

---

### 第三阶段：Java 生态和工具（2-3周）

#### 3.1 包（Package）和模块化
- 理解 `package` 和 `import`
- 项目结构组织

#### 3.2 Maven 或 Gradle 构建工具
- 依赖管理（类似 npm）
- 项目构建和打包

#### 3.3 文件 I/O
- 文件读写操作
- 序列化和反序列化

#### 3.4 网络编程基础
- Socket 编程
- HTTP 客户端（类似 fetch API）

**实践项目：**
- 使用 Maven 创建项目
- 实现文件读写功能
- 简单的网络通信程序

---

### 第四阶段：Spring Boot 入门（2-3周）

#### 4.1 Spring Boot 基础
- 创建 Spring Boot 项目
- 理解依赖注入（DI）和 IoC 容器
- RESTful API 开发（类似 Express.js）

#### 4.2 常用注解
- `@RestController`, `@RequestMapping`
- `@Autowired`, `@Service`, `@Repository`
- `@Configuration`, `@Bean`

#### 4.3 数据库操作
- Spring Data JPA
- 简单的 CRUD 操作

**实践项目：**
- 开发一个简单的 REST API
- 实现用户管理功能
- 连接数据库进行数据持久化

---

## 📖 推荐学习资源

### 在线教程
1. **菜鸟教程 - Java 教程**
   - 网址：https://www.runoob.com/java/java-tutorial.html
   - 适合：快速查阅语法

2. **廖雪峰 Java 教程**
   - 网址：https://www.liaoxuefeng.com/wiki/1252599548343744
   - 适合：系统学习，讲解清晰

3. **Oracle 官方 Java 教程**
   - 网址：https://docs.oracle.com/javase/tutorial/
   - 适合：深入学习，权威资料

4. **B站视频课程**
   - 搜索关键词："Java 零基础"、"Java 从入门到精通"
   - 推荐：尚硅谷、黑马程序员等机构课程

### 书籍推荐
1. **《Java 核心技术》（Core Java）**
   - 作者：Cay S. Horstmann
   - 适合：系统学习，内容全面

2. **《Java 编程思想》（Thinking in Java）**
   - 作者：Bruce Eckel
   - 适合：深入理解 Java 设计思想

3. **《Effective Java》**
   - 作者：Joshua Bloch
   - 适合：进阶学习，最佳实践

### 实践平台
1. **LeetCode**
   - 网址：https://leetcode.cn/
   - 适合：算法练习，提升编程能力

2. **牛客网**
   - 网址：https://www.nowcoder.com/
   - 适合：Java 专项练习，面试准备

3. **GitHub**
   - 搜索 Java 项目，阅读开源代码
   - 适合：学习项目结构和最佳实践

### IDE 和工具
1. **IntelliJ IDEA Community**（免费）
   - 下载：https://www.jetbrains.com/idea/download/
   - 功能强大，智能提示好

2. **VS Code + Java 扩展包**
   - 适合：轻量级开发

3. **Maven**
   - 构建工具，类似 npm
   - 官方文档：https://maven.apache.org/

---

## 🔄 前端 vs Java 核心差异对比

### 1. 类型系统
- **JavaScript**：动态类型，运行时确定
- **Java**：静态类型，编译时检查

### 2. 运行环境
- **JavaScript**：浏览器/Node.js
- **Java**：JVM（Java 虚拟机）

### 3. 面向对象
- **JavaScript**：基于原型，ES6 有 class 语法糖
- **Java**：纯面向对象，所有代码必须在类中

### 4. 异步处理
- **JavaScript**：Promise, async/await
- **Java**：Future, CompletableFuture（Java 8+）

### 5. 包管理
- **JavaScript**：npm, yarn
- **Java**：Maven, Gradle

---

## 📅 学习时间规划建议

### 每日学习时间：1-2 小时

**周一到周五：**
- 理论学习：30-45 分钟
- 编码实践：30-45 分钟

**周末：**
- 项目实战：2-3 小时
- 复习总结：1 小时

### 学习节奏
- **第 1-2 周**：每天学习新概念，多写代码
- **第 3-4 周**：开始做小项目，巩固基础
- **第 5-6 周**：深入学习进阶特性
- **第 7-8 周**：学习 Spring Boot，做完整项目
- **第 9-12 周**：独立开发项目，查漏补缺

---

## ✅ 学习检查清单

### 基础阶段
- [ ] 能够独立搭建 Java 开发环境
- [ ] 理解 Java 基本数据类型和变量
- [ ] 掌握控制流语句
- [ ] 理解类和对象的概念
- [ ] 能够使用继承和多态
- [ ] 理解接口和抽象类

### 进阶阶段
- [ ] 能够处理异常
- [ ] 熟练使用集合框架
- [ ] 理解泛型的使用
- [ ] 能够使用 Lambda 和 Stream API
- [ ] 了解多线程基础

### 实战阶段
- [ ] 能够使用 Maven 管理项目
- [ ] 能够读写文件
- [ ] 能够开发简单的 Spring Boot 应用
- [ ] 能够连接数据库进行 CRUD 操作
- [ ] 能够开发 RESTful API

---

## 🎓 学习建议

1. **多写代码**：每天至少写 100 行代码
2. **做笔记**：记录与 JavaScript 的差异点
3. **做项目**：理论结合实践，从简单到复杂
4. **读源码**：阅读优秀的 Java 开源项目
5. **加入社区**：Stack Overflow、掘金、CSDN 等
6. **保持耐心**：Java 比 JavaScript 更严格，需要时间适应

---

## 🚀 第一个项目建议

**项目：待办事项管理系统（Todo List）**

**功能需求：**
1. 使用命令行界面（CLI）
2. 添加、删除、查看、完成待办事项
3. 数据持久化到文件
4. 使用面向对象设计

**技术栈：**
- Java 基础语法
- 集合框架（ArrayList）
- 文件 I/O
- 异常处理

**进阶版本：**
- 使用 Spring Boot 开发 Web 版本
- 添加数据库存储
- 提供 REST API

---

## 📝 学习记录模板

### 日期：____年__月__日
**今日学习内容：**
- 

**代码实践：**
- 

**遇到的问题：**
- 

**解决方案：**
- 

**明日计划：**
- 

---

## 🔗 快速参考链接

- [Java API 文档](https://docs.oracle.com/javase/8/docs/api/)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [Maven 中央仓库](https://mvnrepository.com/)
- [Java 编码规范](https://google.github.io/styleguide/javaguide.html)

---
