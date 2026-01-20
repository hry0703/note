# Java 访问修饰符详解

## 📋 目录

- [访问修饰符概述](#访问修饰符概述)
- [private 详解](#private-详解)
- [protected 详解](#protected-详解)
- [四种访问修饰符对比](#四种访问修饰符对比)
- [实际应用示例](#实际应用示例)
- [最佳实践](#最佳实践)

---

## 🔐 访问修饰符概述

Java 有四种访问修饰符，控制类、方法、字段的访问权限：

| 修饰符 | 同类 | 同包 | 子类 | 不同包 |
|--------|------|------|------|--------|
| `public` | ✅ | ✅ | ✅ | ✅ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `default`（无修饰符） | ✅ | ✅ | ❌ | ❌ |
| `private` | ✅ | ❌ | ❌ | ❌ |

---

## 🔒 private 详解

### 含义

`private` = 私有的，只能在**同一个类内部**访问

### 访问范围

```
private 成员
    ↓
只能在同一个类中访问
    ↓
其他类、子类、不同包都无法访问
```

### 语法示例

```java
public class User {
    // private 字段（成员变量）
    private String name;
    private int age;
    private String password;
    
    // private 方法
    private void validatePassword(String pwd) {
        // 验证密码的逻辑
    }
    
    // private 构造方法
    private User() {
        // 私有构造方法，外部无法直接创建对象
    }
}
```

### 使用场景

#### 1. 封装数据（隐藏实现细节）

```java
public class BankAccount {
    // private 字段：外部无法直接访问
    private double balance;  // 余额
    private String accountNumber;  // 账号
    
    // public 方法：提供受控的访问
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;  // 只能在类内部修改
        }
    }
    
    public double getBalance() {
        return balance;  // 只能通过方法获取
    }
    
    // private 方法：内部使用
    private void logTransaction(String type, double amount) {
        System.out.println(type + ": " + amount);
    }
}
```

**优点**：
- ✅ 防止外部直接修改数据
- ✅ 可以在方法中添加验证逻辑
- ✅ 隐藏实现细节

#### 2. 私有方法（内部辅助方法）

```java
public class Calculator {
    public int calculate(int a, int b) {
        validateInput(a, b);  // 调用私有方法
        return a + b;
    }
    
    // private 方法：只在类内部使用
    private void validateInput(int a, int b) {
        if (a < 0 || b < 0) {
            throw new IllegalArgumentException("参数不能为负数");
        }
    }
}
```

#### 3. 私有构造方法（单例模式）

```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    
    // private 构造方法：防止外部创建对象
    private DatabaseConnection() {
        // 初始化数据库连接
    }
    
    // 提供获取实例的方法
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}
```

### private 的特点

**✅ 优点**：
- 数据封装，保护数据安全
- 隐藏实现细节
- 便于维护和修改

**❌ 限制**：
- 子类无法访问
- 同包其他类无法访问
- 只能在同一类中使用

---

## 🛡️ protected 详解

### 含义

`protected` = 受保护的，可以在**同类、同包、子类**中访问

### 访问范围

```
protected 成员
    ↓
可以在以下地方访问：
    ├── 同一个类中 ✅
    ├── 同一个包中 ✅
    └── 子类中 ✅（即使在不同包）
    ↓
不同包的非子类无法访问 ❌
```

### 语法示例

```java
package com.hry.model;

public class Animal {
    // protected 字段
    protected String name;
    protected int age;
    
    // protected 方法
    protected void makeSound() {
        System.out.println("动物发出声音");
    }
    
    // protected 构造方法
    protected Animal(String name) {
        this.name = name;
    }
}
```

### 使用场景

#### 1. 继承场景（子类访问父类成员）

```java
// 父类
package com.hry.model;

public class Animal {
    protected String name;  // 子类可以访问
    private String id;      // 子类无法访问
    
    protected void eat() {  // 子类可以访问
        System.out.println(name + " 在吃东西");
    }
}

// 子类（同包）
package com.hry.model;

public class Dog extends Animal {
    public void bark() {
        // ✅ 可以访问父类的 protected 成员
        System.out.println(name + " 在叫");  // 可以访问 name
        eat();  // 可以调用 eat()
    }
}

// 子类（不同包）
package com.hry.service;

import com.hry.model.Animal;

public class Cat extends Animal {
    public void meow() {
        // ✅ 可以访问父类的 protected 成员（即使在不同包）
        System.out.println(name + " 在叫");  // 可以访问 name
        eat();  // 可以调用 eat()
    }
}

// 非子类（不同包）
package com.hry.util;

import com.hry.model.Animal;

public class AnimalHelper {
    public void help(Animal animal) {
        // ❌ 无法访问 protected 成员（不是子类）
        // System.out.println(animal.name);  // 编译错误
        // animal.eat();  // 编译错误
    }
}
```

#### 2. 包内共享（同包访问）

```java
// 文件 1: User.java
package com.hry.model;

public class User {
    protected String username;  // 同包可以访问
    private String password;     // 同包也无法访问
}

// 文件 2: UserService.java（同包）
package com.hry.model;

public class UserService {
    public void processUser(User user) {
        // ✅ 可以访问 protected 成员（同包）
        System.out.println(user.username);
        
        // ❌ 无法访问 private 成员
        // System.out.println(user.password);  // 编译错误
    }
}
```

### protected 的特点

**✅ 优点**：
- 允许子类访问（支持继承）
- 同包内可以共享
- 比 public 更安全（不同包的非子类无法访问）

**⚠️ 注意**：
- 子类可以访问（即使在不同包）
- 同包可以访问
- 不同包的非子类无法访问

---

## 📊 四种访问修饰符对比

### 完整对比表

| 修饰符 | 同类 | 同包 | 子类（同包） | 子类（不同包） | 不同包（非子类） |
|--------|------|------|------------|--------------|----------------|
| `public` | ✅ | ✅ | ✅ | ✅ | ✅ |
| `protected` | ✅ | ✅ | ✅ | ✅ | ❌ |
| `default` | ✅ | ✅ | ✅ | ❌ | ❌ |
| `private` | ✅ | ❌ | ❌ | ❌ | ❌ |

### 访问范围图示

```
public:        [同类] [同包] [子类] [不同包]
protected:     [同类] [同包] [子类]
default:       [同类] [同包]
private:       [同类]
```

---

## 💡 实际应用示例

### 示例 1：完整的类设计

```java
package com.hry.model;

public class User {
    // private：完全私有，只能类内部访问
    private String password;
    private String email;
    
    // protected：子类和同包可以访问
    protected String username;
    protected int userId;
    
    // default：同包可以访问
    String nickname;  // 无修饰符 = default
    
    // public：任何地方都可以访问
    public String getName() {
        return username;
    }
    
    // private 方法：内部使用
    private boolean validatePassword(String pwd) {
        return pwd != null && pwd.length() >= 8;
    }
    
    // protected 方法：子类可以重写
    protected void initialize() {
        // 初始化逻辑
    }
    
    // public 方法：外部接口
    public void setPassword(String pwd) {
        if (validatePassword(pwd)) {  // 调用私有方法
            this.password = pwd;
        }
    }
}
```

### 示例 2：继承场景

```java
// 父类
package com.hry.model;

public class Vehicle {
    private String brand;        // 私有：子类无法访问
    protected int speed;          // 受保护：子类可以访问
    public String color;         // 公共：任何地方都可以访问
    
    protected void startEngine() {  // 子类可以重写
        System.out.println("启动引擎");
    }
    
    private void checkOil() {  // 私有：子类无法访问
        System.out.println("检查机油");
    }
}

// 子类
package com.hry.model;

public class Car extends Vehicle {
    public void drive() {
        // ✅ 可以访问 protected 成员
        speed = 60;
        startEngine();
        
        // ❌ 无法访问 private 成员
        // brand = "Toyota";  // 编译错误
        // checkOil();  // 编译错误
        
        // ✅ 可以访问 public 成员
        color = "红色";
    }
}
```

### 示例 3：封装示例

```java
public class BankAccount {
    // private：完全封装，外部无法直接访问
    private double balance;
    private String accountNumber;
    
    // public：提供受控的访问接口
    public void deposit(double amount) {
        if (validateAmount(amount)) {  // 调用私有方法验证
            balance += amount;
            logTransaction("存款", amount);  // 调用私有方法记录
        }
    }
    
    public void withdraw(double amount) {
        if (validateAmount(amount) && balance >= amount) {
            balance -= amount;
            logTransaction("取款", amount);
        }
    }
    
    public double getBalance() {
        return balance;  // 只读访问
    }
    
    // private：内部辅助方法，外部无法调用
    private boolean validateAmount(double amount) {
        return amount > 0;
    }
    
    private void logTransaction(String type, double amount) {
        System.out.println(type + ": " + amount);
    }
}
```

---

## 🎯 使用建议

### private 使用场景

✅ **推荐使用 private**：
- 字段（成员变量）- 保护数据
- 内部辅助方法 - 隐藏实现细节
- 单例模式的构造方法
- 不希望外部访问的任何成员

```java
public class MyClass {
    private String data;           // ✅ 字段用 private
    private void helperMethod() { } // ✅ 辅助方法用 private
}
```

### protected 使用场景

✅ **推荐使用 protected**：
- 希望子类可以访问的成员
- 同包内需要共享的成员
- 模板方法模式中的钩子方法

```java
public class BaseClass {
    protected void templateMethod() {  // ✅ 子类可以重写
        step1();
        step2();  // 子类可以重写这个方法
        step3();
    }
    
    protected void step2() {  // ✅ 子类可以重写
        // 默认实现
    }
}
```

### 访问修饰符选择指南

```
需要外部访问？
    ├─ 是 → public
    └─ 否 → 需要子类访问？
            ├─ 是 → protected
            └─ 否 → 需要同包访问？
                    ├─ 是 → default（无修饰符）
                    └─ 否 → private
```

---

## 📝 常见错误

### 错误 1：试图访问 private 成员

```java
public class User {
    private String name;
}

public class Test {
    public void test() {
        User user = new User();
        // ❌ 编译错误：name 是 private
        System.out.println(user.name);
    }
}
```

**解决**：提供 public 的 getter 方法

```java
public class User {
    private String name;
    
    // ✅ 提供 public 方法访问
    public String getName() {
        return name;
    }
}
```

### 错误 2：在不同包的非子类中访问 protected

```java
// 包 1
package com.hry.model;
public class Animal {
    protected String name;
}

// 包 2（非子类）
package com.hry.util;
import com.hry.model.Animal;

public class Helper {
    public void help(Animal animal) {
        // ❌ 编译错误：不是子类，无法访问 protected
        // System.out.println(animal.name);
    }
}
```

**解决**：如果是子类就可以访问

```java
// 包 2（子类）
package com.hry.service;
import com.hry.model.Animal;

public class Dog extends Animal {
    public void test() {
        // ✅ 可以访问：是子类
        System.out.println(name);
    }
}
```

---

## 🎓 总结

### private vs protected

| 特性 | private | protected |
|------|---------|-----------|
| **访问范围** | 只有同类 | 同类 + 同包 + 子类 |
| **子类访问** | ❌ 不能 | ✅ 可以 |
| **同包访问** | ❌ 不能 | ✅ 可以 |
| **使用场景** | 数据封装、内部方法 | 继承、子类访问 |
| **安全性** | 最高 | 中等 |

### 记忆口诀

- **private** = 私有的，只有自己能用
- **protected** = 受保护的，自己和家人（同包、子类）能用
- **public** = 公共的，谁都能用
- **default** = 默认的，同包能用

### 最佳实践

1. **字段通常用 private**：保护数据
2. **方法根据需求选择**：
   - 外部接口 → `public`
   - 子类需要 → `protected`
   - 内部使用 → `private`
3. **最小权限原则**：能用 private 就不用 protected，能用 protected 就不用 public

---

**祝学习顺利！💪**

*最后更新：2024年*
