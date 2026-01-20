# Java 语法指南

## 📋 目录

- [基础语法](#基础语法)
- [数据类型](#数据类型)
- [变量和常量](#变量和常量)
- [运算符](#运算符)
- [控制流](#控制流)
- [数组](#数组)
- [面向对象编程](#面向对象编程)
- [集合框架](#集合框架)
- [异常处理](#异常处理)
- [Java vs JavaScript 对比](#java-vs-javascript-对比)

---

## 📝 基础语法

### Hello World 程序

```java
// HelloWorld.java
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}
```

**语法要点：**
- `public class HelloWorld`：类名必须与文件名相同
- `public static void main(String[] args)`：程序入口点
- `System.out.println()`：输出语句
- 每行代码以分号 `;` 结尾
- 代码块用大括号 `{}` 包裹

---

## 🔢 数据类型

### 基本数据类型（Primitive Types）

Java 有 8 种基本数据类型：

| 类型 | 大小 | 范围 | 默认值 | 示例 |
|------|------|------|--------|------|
| `byte` | 1 字节 | -128 到 127 | 0 | `byte b = 100;` |
| `short` | 2 字节 | -32,768 到 32,767 | 0 | `short s = 1000;` |
| `int` | 4 字节 | -2³¹ 到 2³¹-1 | 0 | `int i = 100000;` |
| `long` | 8 字节 | -2⁶³ 到 2⁶³-1 | 0L | `long l = 1000000L;` |
| `float` | 4 字节 | 约 ±3.4E38 | 0.0f | `float f = 3.14f;` |
| `double` | 8 字节 | 约 ±1.7E308 | 0.0d | `double d = 3.14159;` |
| `char` | 2 字节 | Unicode 字符 | '\u0000' | `char c = 'A';` |
| `boolean` | 1 位 | true/false | false | `boolean flag = true;` |

**示例：**
```java
// 整数类型
int age = 25;
long population = 8000000000L;  // 注意 L 后缀

// 浮点类型
float price = 19.99f;  // 注意 f 后缀
double pi = 3.14159265359;

// 字符类型
char grade = 'A';
char chinese = '中';  // 支持 Unicode

// 布尔类型
boolean isActive = true;
boolean isComplete = false;
```

### 引用数据类型（Reference Types）

引用类型包括：类、接口、数组、字符串等。

```java
// 字符串（String 是类，不是基本类型）
String name = "张三";
String message = "Hello, " + name;

// 数组
int[] numbers = {1, 2, 3, 4, 5};

// 对象
Person person = new Person();
```

### 类型转换

#### 自动类型转换（隐式转换）

```java
// 小类型自动转换为大类型
int i = 100;
long l = i;  // int → long（自动）

float f = 3.14f;
double d = f;  // float → double（自动）

char c = 'A';
int code = c;  // char → int（自动，得到 ASCII 码 65）
```

#### 强制类型转换（显式转换）

```java
// 大类型转换为小类型需要强制转换
double d = 3.14;
int i = (int) d;  // double → int（强制），结果：3

long l = 1000000L;
int i = (int) l;  // long → int（强制）

// 注意：可能丢失精度
double pi = 3.14159;
int truncated = (int) pi;  // 结果：3（小数部分丢失）
```

---

## 📦 变量和常量

### 变量声明

```java
// 方式 1：先声明后赋值
int age;
age = 25;

// 方式 2：声明并初始化
int age = 25;

// 方式 3：同时声明多个变量
int x = 10, y = 20, z = 30;
```

### 变量命名规则

```java
// ✅ 合法命名
int age = 25;
String userName = "张三";
boolean isActive = true;
double pricePerUnit = 99.99;

// ❌ 非法命名
// int 2age = 25;        // 不能以数字开头
// String user-name;     // 不能使用连字符
// boolean class;        // 不能使用关键字
```

**命名规范：**
- 驼峰命名法（camelCase）
- 类名首字母大写（PascalCase）
- 常量全大写，用下划线分隔（SNAKE_CASE）
- 见名知意

### 常量

```java
// 使用 final 关键字定义常量
final int MAX_SIZE = 100;
final double PI = 3.14159;
final String COMPANY_NAME = "Example Inc.";

// 常量必须在声明时初始化
final int MIN_VALUE = 0;

// 常量不能修改
// MAX_SIZE = 200;  // ❌ 编译错误
```

### 作用域

```java
public class ScopeExample {
    // 类级别变量（成员变量）
    private int classVariable = 10;
    
    public void method() {
        // 方法级别变量（局部变量）
        int localVariable = 20;
        
        if (true) {
            // 块级别变量
            int blockVariable = 30;
            
            // 可以访问外层变量
            System.out.println(classVariable);  // ✅
            System.out.println(localVariable);  // ✅
            System.out.println(blockVariable);  // ✅
        }
        
        // blockVariable 在这里不可访问
        // System.out.println(blockVariable);  // ❌ 编译错误
    }
}
```

---

## 🔧 运算符

### 算术运算符

```java
int a = 10, b = 3;

System.out.println(a + b);  // 13（加法）
System.out.println(a - b);  // 7（减法）
System.out.println(a * b);  // 30（乘法）
System.out.println(a / b);  // 3（除法，整数除法）
System.out.println(a % b);  // 1（取余）

// 自增自减
int x = 5;
x++;  // x = 6（后置自增）
++x;  // x = 7（前置自增）
x--;  // x = 6（后置自减）
--x;  // x = 5（前置自减）

// 前置和后置的区别
int i = 5;
int j = i++;  // j = 5, i = 6（先赋值后自增）
int k = ++i;  // k = 7, i = 7（先自增后赋值）
```

### 比较运算符

```java
int a = 10, b = 20;

System.out.println(a == b);  // false（等于）
System.out.println(a != b);  // true（不等于）
System.out.println(a < b);   // true（小于）
System.out.println(a > b);   // false（大于）
System.out.println(a <= b);  // true（小于等于）
System.out.println(a >= b);  // false（大于等于）
```

### 逻辑运算符

```java
boolean x = true, y = false;

System.out.println(x && y);  // false（逻辑与）
System.out.println(x || y);  // true（逻辑或）
System.out.println(!x);      // false（逻辑非）

// 短路求值
int a = 10;
if (a > 5 || a++ > 0) {  // a++ 不会执行（短路）
    // ...
}
```

### 赋值运算符

```java
int a = 10;

a += 5;  // a = a + 5，结果：15
a -= 3;  // a = a - 3，结果：12
a *= 2;  // a = a * 2，结果：24
a /= 4;  // a = a / 4，结果：6
a %= 4;  // a = a % 4，结果：2
```

### 三元运算符

```java
// 语法：条件 ? 值1 : 值2
int age = 20;
String status = age >= 18 ? "成年人" : "未成年人";
// 结果：status = "成年人"

int max = a > b ? a : b;  // 取较大值
```

---

## 🔀 控制流

### if-else 语句

```java
int score = 85;

if (score >= 90) {
    System.out.println("优秀");
} else if (score >= 80) {
    System.out.println("良好");
} else if (score >= 60) {
    System.out.println("及格");
} else {
    System.out.println("不及格");
}
```

### switch 语句

```java
// 传统 switch（Java 8+）
int day = 3;
switch (day) {
    case 1:
        System.out.println("星期一");
        break;
    case 2:
        System.out.println("星期二");
        break;
    case 3:
        System.out.println("星期三");
        break;
    default:
        System.out.println("其他");
}

// Switch 表达式（Java 14+）
String dayName = switch (day) {
    case 1 -> "星期一";
    case 2 -> "星期二";
    case 3 -> "星期三";
    default -> "其他";
};

// 多值匹配（Java 14+）
int month = 2;
int days = switch (month) {
    case 1, 3, 5, 7, 8, 10, 12 -> 31;
    case 4, 6, 9, 11 -> 30;
    case 2 -> 28;
    default -> 0;
};
```

### for 循环

```java
// 传统 for 循环
for (int i = 0; i < 10; i++) {
    System.out.println(i);
}

// 增强 for 循环（for-each）
int[] numbers = {1, 2, 3, 4, 5};
for (int num : numbers) {
    System.out.println(num);
}

// 嵌套循环
for (int i = 1; i <= 3; i++) {
    for (int j = 1; j <= 3; j++) {
        System.out.println(i + " x " + j + " = " + (i * j));
    }
}
```

### while 循环

```java
// while 循环
int i = 0;
while (i < 10) {
    System.out.println(i);
    i++;
}

// do-while 循环（至少执行一次）
int j = 0;
do {
    System.out.println(j);
    j++;
} while (j < 10);
```

### break 和 continue

```java
// break：跳出循环
for (int i = 0; i < 10; i++) {
    if (i == 5) {
        break;  // 跳出循环
    }
    System.out.println(i);  // 输出：0, 1, 2, 3, 4
}

// continue：跳过本次循环
for (int i = 0; i < 10; i++) {
    if (i % 2 == 0) {
        continue;  // 跳过偶数
    }
    System.out.println(i);  // 输出：1, 3, 5, 7, 9
}

// 标签 break（跳出外层循环）
outer: for (int i = 0; i < 3; i++) {
    for (int j = 0; j < 3; j++) {
        if (i == 1 && j == 1) {
            break outer;  // 跳出外层循环
        }
        System.out.println(i + ", " + j);
    }
}
```

---

## 📊 数组

### 数组声明和初始化

```java
// 方式 1：声明后初始化
int[] numbers = new int[5];  // 创建长度为 5 的数组
numbers[0] = 1;
numbers[1] = 2;

// 方式 2：声明时初始化
int[] numbers = {1, 2, 3, 4, 5};

// 方式 3：使用 new 关键字
int[] numbers = new int[]{1, 2, 3, 4, 5};

// 多维数组
int[][] matrix = {
    {1, 2, 3},
    {4, 5, 6},
    {7, 8, 9}
};
```

### 数组操作

```java
int[] numbers = {1, 2, 3, 4, 5};

// 获取长度
int length = numbers.length;  // 5

// 访问元素
int first = numbers[0];  // 1
int last = numbers[numbers.length - 1];  // 5

// 遍历数组
for (int i = 0; i < numbers.length; i++) {
    System.out.println(numbers[i]);
}

// 增强 for 循环
for (int num : numbers) {
    System.out.println(num);
}
```

### 数组工具类（Arrays）

```java
import java.util.Arrays;

int[] numbers = {5, 2, 8, 1, 9};

// 排序
Arrays.sort(numbers);  // {1, 2, 5, 8, 9}

// 查找
int index = Arrays.binarySearch(numbers, 5);  // 返回索引

// 填充
Arrays.fill(numbers, 0);  // 所有元素设为 0

// 复制
int[] copy = Arrays.copyOf(numbers, numbers.length);

// 转换为字符串
String str = Arrays.toString(numbers);  // "[1, 2, 5, 8, 9]"
```

---

## 🏗️ 面向对象编程

### 类和对象

```java
// 定义类
public class Person {
    // 成员变量（属性）
    private String name;
    private int age;
    
    // 构造方法
    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    // 方法
    public void introduce() {
        System.out.println("我是 " + name + "，今年 " + age + " 岁");
    }
    
    // Getter 和 Setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
}

// 使用类
Person person = new Person("张三", 25);
person.introduce();  // 输出：我是 张三，今年 25 岁
```

### 访问修饰符

| 修饰符 | 同类 | 同包 | 子类 | 不同包 |
|--------|------|------|------|--------|
| `public` | ✅ | ✅ | ✅ | ✅ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `default`（无修饰符） | ✅ | ✅ | ❌ | ❌ |
| `private` | ✅ | ❌ | ❌ | ❌ |

```java
public class Example {
    public int publicVar = 1;        // 任何地方都可以访问
    protected int protectedVar = 2;  // 同包或子类可以访问
    int defaultVar = 3;              // 同包可以访问
    private int privateVar = 4;      // 只有同类可以访问
}
```

### 继承
```java
// 父类
public class Animal {
    protected String name;
    
    public Animal(String name) {
        this.name = name;
    }
    
    public void eat() {
        System.out.println(name + " 在吃东西");
    }
}

// 子类
public class Dog extends Animal {
    public Dog(String name) {
        super(name);  // 调用父类构造方法
    }
    
    public void bark() {
        System.out.println(name + " 在叫：汪汪汪");
    }
    
    // 方法重写
    @Override
    public void eat() {
        System.out.println(name + " 在吃狗粮");
    }
}

// 使用
Dog dog = new Dog("旺财");
dog.eat();   // 输出：旺财 在吃狗粮
dog.bark();  // 输出：旺财 在叫：汪汪汪
```

### 多态

```java
// 父类引用指向子类对象
Animal animal = new Dog("旺财");
animal.eat();  // 调用子类重写的方法

// 类型转换
if (animal instanceof Dog) {
    Dog dog = (Dog) animal;  // 向下转型
    dog.bark();
}
```

### 抽象类和接口

```java
// 抽象类
public abstract class Shape {
    protected String color;
    
    public abstract double area();  // 抽象方法
    
    public void setColor(String color) {
        this.color = color;
    }
}

// 实现抽象类
public class Circle extends Shape {
    private double radius;
    
    public Circle(double radius) {
        this.radius = radius;
    }
    
    @Override
    public double area() {
        return Math.PI * radius * radius;
    }
}

// 接口
public interface Drawable {
    void draw();  // 接口方法（默认 public abstract）
    
    // Java 8+ 默认方法
    default void print() {
        System.out.println("打印图形");
    }
    
    // Java 8+ 静态方法
    static void info() {
        System.out.println("这是一个可绘制的接口");
    }
}

// 实现接口
public class Rectangle implements Drawable {
    @Override
    public void draw() {
        System.out.println("绘制矩形");
    }
}
```

### 封装

```java
public class BankAccount {
    // 私有属性（封装）
    private double balance;
    private String accountNumber;
    
    // 公共方法（访问接口）
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
    
    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
        }
    }
    
    public double getBalance() {
        return balance;
    }
    
    // 私有方法（内部使用）
    private void logTransaction(String type, double amount) {
        System.out.println(type + ": " + amount);
    }
}
```

---

## 📚 集合框架

### List（列表）

```java
import java.util.ArrayList;
import java.util.List;

// ArrayList（动态数组）
List<String> list = new ArrayList<>();
list.add("苹果");
list.add("香蕉");
list.add("橙子");

// 访问元素
String first = list.get(0);  // "苹果"

// 遍历
for (String item : list) {
    System.out.println(item);
}

// 其他常用方法
list.size();           // 获取大小
list.contains("苹果");  // 是否包含
list.remove("香蕉");    // 删除元素
list.clear();          // 清空
```

### Set（集合）

```java
import java.util.HashSet;
import java.util.Set;

// HashSet（无序，不重复）
Set<String> set = new HashSet<>();
set.add("苹果");
set.add("香蕉");
set.add("苹果");  // 重复，不会添加

System.out.println(set.size());  // 2

// 遍历
for (String item : set) {
    System.out.println(item);
}
```

### Map（映射）

```java
import java.util.HashMap;
import java.util.Map;

// HashMap（键值对）
Map<String, Integer> map = new HashMap<>();
map.put("苹果", 10);
map.put("香蕉", 20);
map.put("橙子", 15);

// 访问
int count = map.get("苹果");  // 10

// 遍历
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// 其他常用方法
map.containsKey("苹果");  // 是否包含键
map.containsValue(10);    // 是否包含值
map.remove("香蕉");       // 删除
map.size();              // 大小
```

---

## ⚠️ 异常处理

### try-catch-finally

```java
try {
    int result = 10 / 0;  // 可能抛出异常
} catch (ArithmeticException e) {
    System.out.println("除零错误: " + e.getMessage());
} catch (Exception e) {
    System.out.println("其他错误: " + e.getMessage());
} finally {
    System.out.println("无论是否异常都会执行");
}
```

### 抛出异常

```java
public void checkAge(int age) throws IllegalArgumentException {
    if (age < 0) {
        throw new IllegalArgumentException("年龄不能为负数");
    }
}

// 调用
try {
    checkAge(-1);
} catch (IllegalArgumentException e) {
    System.out.println("错误: " + e.getMessage());
}
```

### 自定义异常

```java
// 自定义异常类
public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }
}

// 使用
public void method() throws CustomException {
    throw new CustomException("自定义异常");
}
```

---

## 🔄 Java vs JavaScript 对比

### 变量声明

```java
// Java：强类型，必须声明类型
int age = 25;
String name = "张三";
boolean isActive = true;

// JavaScript：弱类型，使用 var/let/const
let age = 25;
let name = "张三";
let isActive = true;
```

### 函数/方法

```java
// Java：方法必须属于类
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}

// JavaScript：函数是一等公民
function add(a, b) {
    return a + b;
}
// 或箭头函数
const add = (a, b) => a + b;
```

### 数组 vs 集合

```java
// Java：数组固定长度，或使用集合
int[] arr = {1, 2, 3};
List<Integer> list = new ArrayList<>();

// JavaScript：数组动态长度
let arr = [1, 2, 3];
arr.push(4);  // 动态添加
```

### 对象

```java
// Java：必须定义类
public class Person {
    private String name;
    private int age;
}

Person person = new Person();

// JavaScript：对象字面量
let person = {
    name: "张三",
    age: 25
};
```

### null vs undefined

```java
// Java：只有 null
String str = null;

// JavaScript：有 null 和 undefined
let str = null;
let str2;  // undefined
```

### 异步处理

```java
// Java：使用 Future、CompletableFuture
CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    return "结果";
});

// JavaScript：使用 Promise、async/await
const promise = new Promise((resolve) => {
    resolve("结果");
});
```

---

## 💡 最佳实践

### 1. 命名规范

```java
// 类名：PascalCase
public class UserService {}

// 方法名和变量名：camelCase
public void getUserName() {}
private int userId;

// 常量：UPPER_SNAKE_CASE
public static final int MAX_SIZE = 100;
```

### 2. 代码组织

```java
// 1. 包声明
package com.example.service;

// 2. 导入语句
import java.util.List;
import java.util.ArrayList;

// 3. 类声明
public class UserService {
    // 成员变量
    private List<User> users;
    
    // 构造方法
    public UserService() {
        this.users = new ArrayList<>();
    }
    
    // 方法
    public void addUser(User user) {
        users.add(user);
    }
}
```

### 3. 注释

```java
/**
 * 用户服务类
 * 提供用户相关的业务逻辑
 * 
 * @author 张三
 * @version 1.0
 */
public class UserService {
    /**
     * 添加用户
     * 
     * @param user 要添加的用户对象
     * @return 是否添加成功
     */
    public boolean addUser(User user) {
        // 单行注释
        return users.add(user);
    }
}
```

---

## 📖 总结

### Java 语法特点

1. **强类型**：变量必须声明类型
2. **面向对象**：一切皆对象（除了基本类型）
3. **编译型语言**：需要先编译后运行
4. **平台无关**：一次编译，到处运行（JVM）
5. **内存管理**：自动垃圾回收

### 学习建议

1. **从基础开始**：数据类型 → 控制流 → 面向对象
2. **多写代码**：理论结合实践
3. **理解概念**：面向对象、继承、多态
4. **熟悉 API**：集合框架、字符串处理
5. **阅读文档**：Java 官方文档是最好资源

---

**祝学习顺利！💪**