# 01 - Python 基础

> **适合对象**：有 JavaScript/TypeScript 基础的前端工程师  
> **学习时长**：2-3 周  
> **学习目标**：掌握 Python 核心语法，能够编写基础 Python 程序

---

## 📚 学习内容

### 1. Python 基础语法（对比 JavaScript）

#### 1.1 变量与数据类型

**JavaScript**:
```javascript
// 变量声明
const name = "Alice";
let age = 25;
const isStudent = true;

// 数组和对象
const numbers = [1, 2, 3];
const person = { name: "Bob", age: 30 };
```

**Python**:
```python
# 变量声明（无需 const/let/var）
name = "Alice"
age = 25
is_student = True  # 注意：True/False 首字母大写

# 列表和字典
numbers = [1, 2, 3]
person = {"name": "Bob", "age": 30}
```

**关键差异**：
- Python 使用 `True`/`False`（首字母大写）
- Python 使用下划线命名法（`is_student`），而非驼峰命名（`isStudent`）
- Python 字典使用花括号 `{}`，类似 JS 对象

---

#### 1.2 条件语句

**JavaScript**:
```javascript
if (age >= 18) {
    console.log("成年人");
} else if (age >= 13) {
    console.log("青少年");
} else {
    console.log("儿童");
}
```

**Python**:
```python
if age >= 18:
    print("成年人")
elif age >= 13:  # 注意：elif 而非 else if
    print("青少年")
else:
    print("儿童")
```

**关键差异**：
- Python 使用 `elif` 而非 `else if`
- Python 使用**缩进**表示代码块，而非花括号 `{}`
- 条件后使用冒号 `:`

---

#### 1.3 循环

**JavaScript**:
```javascript
// for 循环
for (let i = 0; i < 5; i++) {
    console.log(i);
}

// forEach
const fruits = ["apple", "banana", "orange"];
fruits.forEach(fruit => {
    console.log(fruit);
});
```

**Python**:
```python
# for 循环（使用 range）
for i in range(5):  # range(5) 生成 0-4
    print(i)

# for...in 循环
fruits = ["apple", "banana", "orange"]
for fruit in fruits:
    print(fruit)
```

**关键差异**：
- Python 的 `for` 循环更像 JS 的 `for...of`
- Python 使用 `range()` 生成数字序列

---

### 2. 数据结构

#### 2.1 列表（List）- 类似 JS 数组

```python
# 创建列表
numbers = [1, 2, 3, 4, 5]
mixed = [1, "hello", True, 3.14]

# 常用方法
numbers.append(6)          # 添加元素
numbers.extend([7, 8])     # 扩展列表
numbers.insert(0, 0)       # 插入元素
numbers.remove(3)          # 删除特定值
popped = numbers.pop()     # 删除并返回最后一个元素

# 列表切片（Python 特有）
first_three = numbers[0:3]  # 前 3 个元素
last_two = numbers[-2:]     # 最后 2 个元素
reversed_list = numbers[::-1]  # 反转列表

# 列表推导式（非常强大）
squares = [x**2 for x in range(10)]  # [0, 1, 4, 9, 16, ...]
evens = [x for x in range(10) if x % 2 == 0]  # [0, 2, 4, 6, 8]
```

**对比 JS**：
```javascript
// JS 等价操作
const squares = Array.from({length: 10}, (_, i) => i**2);
const evens = Array.from({length: 10}, (_, i) => i).filter(x => x % 2 === 0);

// 或使用 map/filter
const squares = [...Array(10).keys()].map(x => x**2);
```

---

#### 2.2 字典（Dict）- 类似 JS 对象

```python
# 创建字典
person = {
    "name": "Alice",
    "age": 25,
    "is_student": True
}

# 访问
name = person["name"]          # 直接访问
age = person.get("age", 0)     # 安全访问（带默认值）

# 修改
person["age"] = 26
person["email"] = "alice@example.com"  # 添加新键

# 删除
del person["is_student"]
email = person.pop("email")    # 删除并返回值

# 遍历
for key in person:
    print(f"{key}: {person[key]}")

for key, value in person.items():
    print(f"{key}: {value}")

# 字典推导式
squares_dict = {x: x**2 for x in range(5)}  # {0:0, 1:1, 2:4, 3:9, 4:16}
```

---

#### 2.3 元组（Tuple）- 不可变列表

```python
# 创建元组
point = (10, 20)
rgb = (255, 128, 0)

# 解包（类似 JS 解构）
x, y = point
r, g, b = rgb

# 元组不可修改（会报错）
# point[0] = 15  # TypeError
```

---

#### 2.4 集合（Set）- 去重集合

```python
# 创建集合
unique_numbers = {1, 2, 3, 3, 4}  # {1, 2, 3, 4}
fruits = set(["apple", "banana", "apple"])  # {'apple', 'banana'}

# 集合操作
set1 = {1, 2, 3}
set2 = {3, 4, 5}

union = set1 | set2        # 并集 {1, 2, 3, 4, 5}
intersection = set1 & set2  # 交集 {3}
difference = set1 - set2    # 差集 {1, 2}
```

---

### 3. 函数

#### 3.1 基础函数

**JavaScript**:
```javascript
function greet(name, greeting = "Hello") {
    return `${greeting}, ${name}!`;
}

const add = (a, b) => a + b;
```

**Python**:
```python
def greet(name, greeting="Hello"):
    return f"{greeting}, {name}!"

# Python 3.10+ 也支持 lambda（匿名函数）
add = lambda a, b: a + b
```

---

#### 3.2 高阶函数（对比 JS）

**JavaScript**:
```javascript
const numbers = [1, 2, 3, 4, 5];

const doubled = numbers.map(x => x * 2);
const evens = numbers.filter(x => x % 2 === 0);
const sum = numbers.reduce((acc, x) => acc + x, 0);
```

**Python**:
```python
numbers = [1, 2, 3, 4, 5]

# 使用 map/filter（返回迭代器）
doubled = list(map(lambda x: x * 2, numbers))
evens = list(filter(lambda x: x % 2 == 0, numbers))

# 使用列表推导式（更 Pythonic）
doubled = [x * 2 for x in numbers]
evens = [x for x in numbers if x % 2 == 0]

# reduce 需要导入
from functools import reduce
sum_result = reduce(lambda acc, x: acc + x, numbers, 0)

# 更简单的方式
sum_result = sum(numbers)
```

---

#### 3.3 函数参数技巧

```python
# 可变参数（*args）
def sum_all(*args):
    return sum(args)

print(sum_all(1, 2, 3, 4))  # 10

# 关键字参数（**kwargs）
def print_info(**kwargs):
    for key, value in kwargs.items():
        print(f"{key}: {value}")

print_info(name="Alice", age=25, city="NYC")

# 混合使用
def flexible_func(required, *args, default="default", **kwargs):
    print(f"Required: {required}")
    print(f"Args: {args}")
    print(f"Default: {default}")
    print(f"Kwargs: {kwargs}")

flexible_func(1, 2, 3, default="custom", extra="value")
```

---

### 4. 面向对象编程（OOP）

#### 4.1 类定义

**JavaScript**:
```javascript
class Person {
    constructor(name, age) {
        this.name = name;
        this.age = age;
    }

    greet() {
        return `Hello, I'm ${this.name}`;
    }
}

const alice = new Person("Alice", 25);
```

**Python**:
```python
class Person:
    def __init__(self, name, age):  # 构造函数
        self.name = name
        self.age = age
    
    def greet(self):  # 方法需要 self 参数
        return f"Hello, I'm {self.name}"

alice = Person("Alice", 25)  # 不需要 new 关键字
```

---

#### 4.2 继承

```python
class Student(Person):
    def __init__(self, name, age, student_id):
        super().__init__(name, age)  # 调用父类构造函数
        self.student_id = student_id
    
    def study(self):
        return f"{self.name} is studying"

student = Student("Bob", 20, "S12345")
print(student.greet())   # 继承的方法
print(student.study())   # 自己的方法
```

---

#### 4.3 特殊方法（魔术方法）

```python
class Point:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    # 字符串表示
    def __str__(self):
        return f"Point({self.x}, {self.y})"
    
    # 相等比较
    def __eq__(self, other):
        return self.x == other.x and self.y == other.y
    
    # 加法运算
    def __add__(self, other):
        return Point(self.x + other.x, self.y + other.y)

p1 = Point(1, 2)
p2 = Point(3, 4)
p3 = p1 + p2  # Point(4, 6)
print(p3)     # Point(4, 6)
```

---

### 5. 异步编程（async/await）

**对比 JavaScript**：

**JavaScript**:
```javascript
async function fetchData() {
    const response = await fetch('https://api.example.com/data');
    const data = await response.json();
    return data;
}
```

**Python**:
```python
import asyncio
import aiohttp

async def fetch_data():
    async with aiohttp.ClientSession() as session:
        async with session.get('https://api.example.com/data') as response:
            data = await response.json()
            return data

# 运行异步函数
asyncio.run(fetch_data())
```

---

### 6. 类型提示（TypeScript 用户的福音）

**TypeScript**:
```typescript
function greet(name: string, age: number): string {
    return `Hello, ${name}! You are ${age} years old.`;
}
```

**Python（类型提示）**:
```python
def greet(name: str, age: int) -> str:
    return f"Hello, {name}! You are {age} years old."

# 复杂类型
from typing import List, Dict, Optional, Union

def process_items(items: List[str]) -> Dict[str, int]:
    return {item: len(item) for item in items}

def find_user(user_id: int) -> Optional[Dict[str, str]]:
    # 可能返回 None
    pass

def format_value(value: Union[int, str]) -> str:
    return str(value)
```

---

### 7. 文件操作与 JSON

#### 7.1 文件读写

```python
# 读取文件
with open('data.txt', 'r', encoding='utf-8') as file:
    content = file.read()
    # 或按行读取
    lines = file.readlines()

# 写入文件
with open('output.txt', 'w', encoding='utf-8') as file:
    file.write("Hello, World!\n")
    file.writelines(["Line 1\n", "Line 2\n"])
```

---

#### 7.2 JSON 处理

**JavaScript**:
```javascript
const data = { name: "Alice", age: 25 };
const jsonString = JSON.stringify(data);
const parsed = JSON.parse(jsonString);
```

**Python**:
```python
import json

# ========== json.dumps() - 序列化（Python 对象 → JSON 字符串）==========
# 类比：JSON.stringify()

data = {"name": "Alice", "age": 25, "city": "NYC"}

# 基础用法
json_string = json.dumps(data)
print(json_string)  # {"name": "Alice", "age": 25, "city": "NYC"}

# 常用参数
# 1. indent - 格式化缩进（美化输出）
pretty_json = json.dumps(data, indent=2)
# 输出：
# {
#   "name": "Alice",
#   "age": 25,
#   "city": "NYC"
# }

# 2. ensure_ascii=False - 保留非 ASCII 字符（中文等）
chinese_data = {"name": "张三", "city": "北京"}
json_str = json.dumps(chinese_data, ensure_ascii=False)
print(json_str)  # {"name": "张三", "city": "北京"}

# 如果 ensure_ascii=True（默认），中文会被转成 Unicode
json_str_ascii = json.dumps(chinese_data)
print(json_str_ascii)  # {"name": "\u5f20\u4e09", "city": "\u5317\u4eac"}

# 3. sort_keys - 按键名排序
unsorted = {"c": 3, "a": 1, "b": 2}
sorted_json = json.dumps(unsorted, sort_keys=True)
print(sorted_json)  # {"a": 1, "b": 2, "c": 3}

# 4. separators - 自定义分隔符（紧凑格式）
compact = json.dumps(data, separators=(',', ':'))
print(compact)  # {"name":"Alice","age":25}  # 无空格

# ========== json.loads() - 反序列化（JSON 字符串 → Python 对象）==========
# 类比：JSON.parse()

json_string = '{"name": "Alice", "age": 25, "city": "NYC"}'
parsed = json.loads(json_string)

print(parsed)  # {'name': 'Alice', 'age': 25, 'city': 'NYC'}
print(type(parsed))  # <class 'dict'>

# 访问数据
print(parsed["name"])  # Alice
print(parsed["age"])   # 25

# 处理复杂 JSON
complex_json = '''
{
    "users": [
        {"name": "Alice", "age": 25},
        {"name": "Bob", "age": 30}
    ],
    "total": 2
}
'''
data = json.loads(complex_json)
print(data["users"][0]["name"])  # Alice

# ========== json.dump() 和 json.load() - 文件操作 ===========
# 注意：dump/load（无 's'）直接操作文件，dumps/loads（有 's'）操作字符串

# 写入 JSON 文件
data = {"name": "Alice", "age": 25}
with open('data.json', 'w', encoding='utf-8') as f:
    json.dump(data, f, ensure_ascii=False, indent=2)

# 读取 JSON 文件
with open('data.json', 'r', encoding='utf-8') as f:
    loaded_data = json.load(f)

print(loaded_data)  # {'name': 'Alice', 'age': 25}

# ========== 记忆技巧 ===========
# dumps = dump string（转成字符串）
# loads = load string（从字符串加载）
# dump = dump to file（写入文件）
# load = load from file（从文件读取）
```

**对比表**：

| Python | JavaScript | 作用 |
|--------|-----------|------|
| `json.dumps()` | `JSON.stringify()` | 对象 → JSON 字符串 |
| `json.loads()` | `JSON.parse()` | JSON 字符串 → 对象 |
| `json.dump()` | - | 对象 → 文件 |
| `json.load()` | - | 文件 → 对象 |

**常见错误处理**：

```python
import json

# 错误 1：JSON 格式错误（使用单引号）
bad_json = "{'name': 'Alice'}"  # ❌ JSON 必须用双引号

try:
    data = json.loads(bad_json)
except json.JSONDecodeError as e:
    print(f"JSON 解析错误: {e}")

# 正确格式
good_json = '{"name": "Alice"}'  # ✅ 使用双引号
data = json.loads(good_json)

# 错误 2：不可序列化的对象（如 datetime）
from datetime import datetime

data = {"date": datetime.now()}
try:
    json.dumps(data)
except TypeError as e:
    print(f"序列化错误: {e}")

# 解决方案：自定义序列化函数
def datetime_handler(obj):
    if isinstance(obj, datetime):
        return obj.isoformat()
    raise TypeError(f"Object {obj} is not JSON serializable")

data = {"date": datetime.now()}
json_str = json.dumps(data, default=datetime_handler)
print(json_str)  # {"date": "2024-01-01T12:00:00"}
```

---

## 🎯 实战练习

### 练习 1：列表操作
```python
# 创建一个函数，接收一个数字列表，返回所有偶数的平方
def even_squares(numbers):
    return [x**2 for x in numbers if x % 2 == 0]

print(even_squares([1, 2, 3, 4, 5, 6]))  # [4, 16, 36]
```

### 练习 2：字典操作
```python
# 统计字符串中每个字符出现的次数
def count_chars(text):
    counts = {}
    for char in text:
        counts[char] = counts.get(char, 0) + 1
    return counts

print(count_chars("hello"))  # {'h': 1, 'e': 1, 'l': 2, 'o': 1}
```

### 练习 3：类与对象
```python
# 创建一个简单的银行账户类
class BankAccount:
    def __init__(self, owner, balance=0):
        self.owner = owner
        self.balance = balance
    
    def deposit(self, amount):
        self.balance += amount
        return self.balance
    
    def withdraw(self, amount):
        if amount > self.balance:
            return "余额不足"
        self.balance -= amount
        return self.balance
    
    def __str__(self):
        return f"{self.owner}的账户余额：${self.balance}"

account = BankAccount("Alice", 1000)
account.deposit(500)
print(account)  # Alice的账户余额：$1500
```

---

## 📖 推荐资源

### 在线教程
- **官方文档**：https://docs.python.org/zh-cn/3/
- **Real Python**：https://realpython.com/
- **Python Tutor**：https://pythontutor.com/（可视化代码执行）

### 视频课程
- **freeCodeCamp**：Python for Beginners
- **Corey Schafer**：Python Tutorial for Beginners（YouTube）

### 练习平台
- **LeetCode**：刷算法题（选择 Python）
- **Exercism**：Python 练习题
- **HackerRank**：Python 挑战

---

## ✅ 学习检查清单

- [ ] 掌握 Python 基础语法（变量、条件、循环）
- [ ] 理解 Python 数据结构（列表、字典、集合、元组）
- [ ] 能够编写函数和使用高阶函数
- [ ] 理解面向对象编程（类、继承）
- [ ] 掌握异步编程基础（async/await）
- [ ] 能够使用类型提示
- [ ] 掌握文件操作和 JSON 处理
- [ ] 完成至少 10 道 LeetCode 简单题（Python）

---

**下一步**：学习 [02-FastAPI后端开发](./02-FastAPI后端开发.md)
