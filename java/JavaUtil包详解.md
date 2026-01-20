# java.util 包详解

## 📋 目录

- [java.util 包概述](#javautil-包概述)
- [常用类和方法](#常用类和方法)
- [集合框架](#集合框架)
- [日期时间](#日期时间)
- [其他工具类](#其他工具类)
- [实际使用示例](#实际使用示例)

---

## 📦 java.util 包概述

### 什么是 java.util？

`java.util` 是 Java 标准库中的一个**包（package）**，包含大量实用的类和接口。

**注意**：`java.util` 本身是一个包名，不是类，所以它没有方法。但包中的类有方法。

### 主要包含的内容

1. **集合框架**：List、Set、Map 等
2. **日期时间**：Date、Calendar（Java 8+ 推荐使用 java.time）
3. **工具类**：Scanner、Random、Arrays 等
4. **其他**：Properties、Timer 等

---

## 📚 常用类和方法

### 1. ArrayList（动态数组）

**导入**：
```java
import java.util.ArrayList;
import java.util.List;
```

**常用方法**：

```java
List<String> list = new ArrayList<>();

// 添加元素
list.add("苹果");           // 添加元素到末尾
list.add(0, "香蕉");        // 在指定位置插入
list.addAll(otherList);     // 添加另一个集合的所有元素

// 获取元素
String item = list.get(0);  // 获取指定位置的元素
int size = list.size();     // 获取元素个数

// 查找元素
boolean exists = list.contains("苹果");  // 是否包含
int index = list.indexOf("苹果");        // 查找索引
int lastIndex = list.lastIndexOf("苹果"); // 最后出现的索引

// 修改元素
list.set(0, "橙子");        // 替换指定位置的元素

// 删除元素
list.remove(0);            // 删除指定位置的元素
list.remove("苹果");       // 删除指定元素
list.clear();              // 清空所有元素

// 遍历
for (String item : list) {
    System.out.println(item);
}

// 转换为数组
String[] array = list.toArray(new String[0]);

// 判断是否为空
boolean isEmpty = list.isEmpty();
```

**完整示例**：
```java
import java.util.ArrayList;
import java.util.List;

List<String> fruits = new ArrayList<>();
fruits.add("苹果");
fruits.add("香蕉");
fruits.add("橙子");

System.out.println(fruits.size());      // 输出：3
System.out.println(fruits.get(0));      // 输出：苹果
System.out.println(fruits.contains("苹果"));  // 输出：true

fruits.remove("香蕉");
System.out.println(fruits);  // 输出：[苹果, 橙子]
```

---

### 2. HashMap（哈希映射/字典）

**导入**：
```java
import java.util.HashMap;
import java.util.Map;
```

**常用方法**：

```java
Map<String, Integer> map = new HashMap<>();

// 添加/更新元素
map.put("苹果", 10);        // 添加键值对
map.put("香蕉", 20);
map.putIfAbsent("苹果", 15); // 如果不存在才添加

// 获取元素
Integer count = map.get("苹果");        // 获取值
Integer count2 = map.getOrDefault("橙子", 0); // 获取值，不存在返回默认值

// 查找
boolean exists = map.containsKey("苹果");  // 是否包含键
boolean hasValue = map.containsValue(10);  // 是否包含值
int size = map.size();                     // 键值对数量

// 删除
map.remove("苹果");         // 删除指定键
map.remove("香蕉", 20);     // 删除键值对（键和值都匹配）
map.clear();                // 清空

// 遍历
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// 获取所有键
Set<String> keys = map.keySet();

// 获取所有值
Collection<Integer> values = map.values();

// 判断是否为空
boolean isEmpty = map.isEmpty();
```

**完整示例**：
```java
import java.util.HashMap;
import java.util.Map;

Map<String, Integer> scores = new HashMap<>();
scores.put("张三", 90);
scores.put("李四", 85);
scores.put("王五", 95);

System.out.println(scores.get("张三"));  // 输出：90
System.out.println(scores.containsKey("李四"));  // 输出：true

for (Map.Entry<String, Integer> entry : scores.entrySet()) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}
```

---

### 3. HashSet（集合，不重复）

**导入**：
```java
import java.util.HashSet;
import java.util.Set;
```

**常用方法**：

```java
Set<String> set = new HashSet<>();

// 添加元素
set.add("苹果");
set.add("香蕉");
set.add("苹果");  // 重复，不会添加

// 查找
boolean exists = set.contains("苹果");
int size = set.size();

// 删除
set.remove("苹果");
set.clear();

// 遍历
for (String item : set) {
    System.out.println(item);
}

// 判断是否为空
boolean isEmpty = set.isEmpty();
```

**完整示例**：
```java
import java.util.HashSet;
import java.util.Set;

Set<String> uniqueNames = new HashSet<>();
uniqueNames.add("张三");
uniqueNames.add("李四");
uniqueNames.add("张三");  // 重复，不会添加

System.out.println(uniqueNames.size());  // 输出：2
System.out.println(uniqueNames);  // 输出：[张三, 李四]（顺序可能不同）
```

---

### 4. Scanner（输入扫描器）

**导入**：
```java
import java.util.Scanner;
```

**常用方法**：

```java
Scanner scanner = new Scanner(System.in);

// 读取不同类型的数据
String line = scanner.nextLine();    // 读取一行
String word = scanner.next();        // 读取一个单词
int number = scanner.nextInt();      // 读取整数
double decimal = scanner.nextDouble(); // 读取浮点数
boolean flag = scanner.nextBoolean(); // 读取布尔值

// 判断是否有下一个
boolean hasNext = scanner.hasNext();      // 是否有下一个
boolean hasNextInt = scanner.hasNextInt(); // 是否有下一个整数

// 关闭
scanner.close();
```

**完整示例**：
```java
import java.util.Scanner;

Scanner scanner = new Scanner(System.in);

System.out.print("请输入你的名字: ");
String name = scanner.nextLine();

System.out.print("请输入你的年龄: ");
int age = scanner.nextInt();

System.out.println("你好, " + name + "，你今年 " + age + " 岁");

scanner.close();
```

---

### 5. Random（随机数生成器）

**导入**：
```java
import java.util.Random;
```

**常用方法**：

```java
Random random = new Random();

// 生成随机数
int num = random.nextInt();           // 随机整数
int numInRange = random.nextInt(100); // 0-99 的随机整数
double decimal = random.nextDouble(); // 0.0-1.0 的随机浮点数
boolean bool = random.nextBoolean();  // 随机布尔值
long longNum = random.nextLong();     // 随机长整数

// 设置种子（用于生成可重复的随机数序列）
Random seeded = new Random(12345);
```

**完整示例**：
```java
import java.util.Random;

Random random = new Random();

// 生成 1-100 的随机数
int number = random.nextInt(100) + 1;
System.out.println("随机数: " + number);

// 生成随机布尔值
boolean coin = random.nextBoolean();
System.out.println("抛硬币: " + (coin ? "正面" : "反面"));
```

---

### 6. Arrays（数组工具类）

**导入**：
```java
import java.util.Arrays;
```

**常用方法**：

```java
int[] numbers = {5, 2, 8, 1, 9};

// 排序
Arrays.sort(numbers);  // 原地排序

// 查找
int index = Arrays.binarySearch(numbers, 5);  // 二分查找（需要先排序）

// 填充
Arrays.fill(numbers, 0);  // 所有元素设为 0

// 复制
int[] copy = Arrays.copyOf(numbers, numbers.length);
int[] copyRange = Arrays.copyOfRange(numbers, 0, 3);  // 复制指定范围

// 比较
boolean equals = Arrays.equals(array1, array2);  // 比较两个数组

// 转换为字符串
String str = Arrays.toString(numbers);  // "[1, 2, 5, 8, 9]"

// 转换为列表
List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);
```

**完整示例**：
```java
import java.util.Arrays;

int[] numbers = {5, 2, 8, 1, 9};
System.out.println("原始: " + Arrays.toString(numbers));

Arrays.sort(numbers);
System.out.println("排序后: " + Arrays.toString(numbers));

int index = Arrays.binarySearch(numbers, 5);
System.out.println("5 的位置: " + index);
```

---

### 7. Collections（集合工具类）

**导入**：
```java
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
```

**常用方法**：

```java
List<Integer> list = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5));

// 排序
Collections.sort(list);  // 升序排序
Collections.sort(list, Collections.reverseOrder());  // 降序排序

// 反转
Collections.reverse(list);

// 打乱
Collections.shuffle(list);

// 查找
int index = Collections.binarySearch(list, 4);  // 二分查找

// 最值
Integer max = Collections.max(list);
Integer min = Collections.min(list);

// 替换
Collections.replaceAll(list, 1, 10);  // 替换所有 1 为 10

// 填充
Collections.fill(list, 0);  // 所有元素设为 0

// 频率
int frequency = Collections.frequency(list, 1);  // 元素 1 出现的次数

// 创建不可变集合
List<Integer> unmodifiable = Collections.unmodifiableList(list);
```

**完整示例**：
```java
import java.util.*;

List<Integer> numbers = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5));
System.out.println("原始: " + numbers);

Collections.sort(numbers);
System.out.println("排序: " + numbers);

System.out.println("最大值: " + Collections.max(numbers));
System.out.println("最小值: " + Collections.min(numbers));
```

---

### 8. Date 和 Calendar（日期时间，Java 8+ 推荐使用 java.time）

**导入**：
```java
import java.util.Date;
import java.util.Calendar;
```

**Date 常用方法**：

```java
// 创建日期
Date now = new Date();                    // 当前时间
Date date = new Date(1234567890L);        // 指定时间戳

// 获取时间戳
long timestamp = date.getTime();

// 比较
boolean after = date1.after(date2);       // date1 是否在 date2 之后
boolean before = date1.before(date2);     // date1 是否在 date2 之前
int compare = date1.compareTo(date2);     // 比较（-1, 0, 1）

// 转换为字符串
String str = date.toString();
```

**Calendar 常用方法**：

```java
Calendar cal = Calendar.getInstance();

// 获取日期时间
int year = cal.get(Calendar.YEAR);
int month = cal.get(Calendar.MONTH) + 1;  // 月份从 0 开始
int day = cal.get(Calendar.DAY_OF_MONTH);
int hour = cal.get(Calendar.HOUR_OF_DAY);
int minute = cal.get(Calendar.MINUTE);
int second = cal.get(Calendar.SECOND);

// 设置日期时间
cal.set(Calendar.YEAR, 2024);
cal.set(Calendar.MONTH, Calendar.JANUARY);
cal.set(Calendar.DAY_OF_MONTH, 1);

// 添加时间
cal.add(Calendar.DAY_OF_MONTH, 7);  // 加 7 天
cal.add(Calendar.MONTH, -1);        // 减 1 个月

// 转换为 Date
Date date = cal.getTime();
```

**注意**：Java 8+ 推荐使用 `java.time` 包（LocalDate、LocalTime、LocalDateTime）

---

## 🔄 集合框架总结

### List（列表）- 有序，可重复

| 实现类 | 特点 | 使用场景 |
|--------|------|---------|
| `ArrayList` | 动态数组，随机访问快 | 常用，适合查询多 |
| `LinkedList` | 链表，插入删除快 | 适合频繁插入删除 |
| `Vector` | 线程安全，已过时 | 不推荐使用 |

### Set（集合）- 无序，不重复

| 实现类 | 特点 | 使用场景 |
|--------|------|---------|
| `HashSet` | 哈希表，无序 | 常用，快速查找 |
| `TreeSet` | 红黑树，有序 | 需要排序时 |
| `LinkedHashSet` | 链表+哈希，保持插入顺序 | 需要保持顺序 |

### Map（映射）- 键值对

| 实现类 | 特点 | 使用场景 |
|--------|------|---------|
| `HashMap` | 哈希表，无序 | 常用，快速查找 |
| `TreeMap` | 红黑树，有序 | 需要排序时 |
| `LinkedHashMap` | 链表+哈希，保持插入顺序 | 需要保持顺序 |

---

## 💡 实际使用示例

### 示例 1：用户管理系统

```java
import java.util.*;

public class UserManager {
    private Map<String, User> users = new HashMap<>();
    private List<String> log = new ArrayList<>();
    
    public void addUser(String name, int age) {
        users.put(name, new User(name, age));
        log.add("添加用户: " + name);
    }
    
    public User getUser(String name) {
        return users.get(name);
    }
    
    public List<String> getAllUserNames() {
        return new ArrayList<>(users.keySet());
    }
    
    public void printLog() {
        for (String entry : log) {
            System.out.println(entry);
        }
    }
}
```

### 示例 2：去重和排序

```java
import java.util.*;

public class DataProcessor {
    public static void main(String[] args) {
        // 原始数据（有重复）
        List<String> data = Arrays.asList("苹果", "香蕉", "苹果", "橙子", "香蕉");
        
        // 去重
        Set<String> unique = new HashSet<>(data);
        System.out.println("去重后: " + unique);
        
        // 排序
        List<String> sorted = new ArrayList<>(unique);
        Collections.sort(sorted);
        System.out.println("排序后: " + sorted);
    }
}
```

### 示例 3：统计频率

```java
import java.util.*;

public class FrequencyCounter {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("apple", "banana", "apple", "orange", "banana", "apple");
        
        Map<String, Integer> frequency = new HashMap<>();
        for (String word : words) {
            frequency.put(word, frequency.getOrDefault(word, 0) + 1);
        }
        
        for (Map.Entry<String, Integer> entry : frequency.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
```

---

## 📚 常用导入语句

```java
// 集合
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

// 工具类
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;
import java.util.Random;

// 日期时间（不推荐，Java 8+ 用 java.time）
import java.util.Date;
import java.util.Calendar;
```

---

## 🎓 总结

### java.util 包中的常用类

1. **集合类**：ArrayList、HashMap、HashSet
2. **工具类**：Arrays、Collections、Scanner、Random
3. **日期类**：Date、Calendar（Java 8+ 推荐用 java.time）

### 快速参考

| 需求 | 使用的类 | 主要方法 |
|------|---------|---------|
| 动态数组 | `ArrayList` | add, get, remove, size |
| 键值对 | `HashMap` | put, get, remove, containsKey |
| 去重集合 | `HashSet` | add, contains, remove |
| 数组操作 | `Arrays` | sort, binarySearch, toString |
| 集合操作 | `Collections` | sort, reverse, max, min |
| 用户输入 | `Scanner` | nextLine, nextInt, next |
| 随机数 | `Random` | nextInt, nextDouble |

---

**祝学习顺利！💪**

*最后更新：2024年*
