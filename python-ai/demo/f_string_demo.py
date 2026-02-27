"""
f-string（格式化字符串字面量）详解
f-string 是 Python 3.6+ 引入的字符串格式化方法，是最现代、最推荐的方式
"""

# ========== 1. 什么是 f-string？ ==========
print("=" * 60)
print("1. 什么是 f-string？")
print("=" * 60)

# f-string = formatted string literal（格式化字符串字面量）
# 在字符串前加 'f' 或 'F'，然后在字符串中使用 {变量名} 来插入变量

name = "Alice"
age = 25

# ✅ f-string 写法（最简洁）
message = f"My name is {name}, I'm {age} years old."
print(f"f-string: {message}")

# 对比：传统方法（不推荐）
message_old = "My name is " + name + ", I'm " + str(age) + " years old."
print(f"字符串拼接: {message_old}")

# ========== 2. f-string 基本语法 ==========
print("\n" + "=" * 60)
print("2. f-string 基本语法")
print("=" * 60)

# 语法：f"字符串 {变量} 字符串"
# 或者：F"字符串 {变量} 字符串"（大小写都可以）

name = "Bob"
age = 30
city = "NYC"

# 基本用法
greeting = f"Hello, {name}!"
print(f"基本用法: {greeting}")

# 多个变量
info = f"{name} is {age} years old and lives in {city}."
print(f"多个变量: {info}")

# 表达式计算
result = f"{name} will be {age + 1} years old next year."
print(f"表达式: {result}")

# 调用函数
length = f"The name '{name}' has {len(name)} letters."
print(f"函数调用: {length}")

# ========== 3. f-string vs 其他格式化方法 ==========
print("\n" + "=" * 60)
print("3. f-string vs 其他格式化方法（对比）")
print("=" * 60)

name = "Alice"
age = 25
score = 95.5

# 方法 1：f-string（✅ 最推荐，Python 3.6+）
msg1 = f"{name} is {age} years old, score: {score}"
print(f"f-string: {msg1}")

# 方法 2：.format()（旧方法）
msg2 = "{} is {} years old, score: {}".format(name, age, score)
print(f".format(): {msg2}")

# 方法 3：% 格式化（最旧的方法，不推荐）
msg3 = "%s is %d years old, score: %.1f" % (name, age, score)
print(f"% 格式化: {msg3}")

# 方法 4：字符串拼接（不推荐）
msg4 = name + " is " + str(age) + " years old, score: " + str(score)
print(f"字符串拼接: {msg4}")

print("\n✅ 推荐：使用 f-string（最简洁、最易读、性能最好）")

# ========== 4. f-string 高级用法 ==========
print("\n" + "=" * 60)
print("4. f-string 高级用法")
print("=" * 60)

# 4.1 表达式和计算
x = 10
y = 20
calc = f"{x} + {y} = {x + y}"
print(f"表达式计算: {calc}")

# 4.2 调用方法
text = "hello world"
formatted = f"Original: {text}, Upper: {text.upper()}, Title: {text.title()}"
print(f"方法调用: {formatted}")

# 4.3 条件表达式（三元运算符）
age = 25
status = f"Age: {age}, Status: {'Adult' if age >= 18 else 'Minor'}"
print(f"条件表达式: {status}")

# 4.4 格式化数字
pi = 3.14159265359
price = 99.9

# 保留小数位数
pi_formatted = f"π = {pi:.2f}"  # 保留 2 位小数
print(f"保留小数: {pi_formatted}")

# 千位分隔符
big_number = 1234567
formatted_number = f"Number: {big_number:,}"
print(f"千位分隔符: {formatted_number}")

# 百分比
ratio = 0.85
percentage = f"Success rate: {ratio:.1%}"
print(f"百分比: {percentage}")

# 对齐和填充
name = "Alice"
# 左对齐，总宽度 10
left = f"|{name:<10}|"
# 右对齐，总宽度 10
right = f"|{name:>10}|"
# 居中对齐，总宽度 10
center = f"|{name:^10}|"
print(f"左对齐: {left}")
print(f"右对齐: {right}")
print(f"居中: {center}")

# 4.5 日期时间格式化
from datetime import datetime
now = datetime.now()
date_str = f"Today is {now:%Y-%m-%d %H:%M:%S}"
print(f"日期格式化: {date_str}")

# ========== 5. f-string 格式化选项 ==========
print("\n" + "=" * 60)
print("5. f-string 格式化选项")
print("=" * 60)

number = 123.456789

# 数字格式化
print(f"原始数字: {number}")
print(f"保留 2 位小数: {number:.2f}")
print(f"保留 4 位小数: {number:.4f}")
print(f"科学计数法: {number:.2e}")
print(f"整数: {number:.0f}")

# 宽度和对齐
text = "Hello"
print(f"左对齐（宽度10）: |{text:<10}|")
print(f"右对齐（宽度10）: |{text:>10}|")
print(f"居中（宽度10）: |{text:^10}|")
print(f"填充字符: |{text:*^10}|")

# 数字格式化组合
num = 42
print(f"二进制: {num:b}")
print(f"八进制: {num:o}")
print(f"十六进制: {num:x}")
print(f"十六进制（大写）: {num:X}")

# ========== 6. f-string 嵌套 ==========
print("\n" + "=" * 60)
print("6. f-string 嵌套和复杂表达式")
print("=" * 60)

# 嵌套 f-string
name = "Alice"
age = 25
info = f"User: {name}, Age: {age}, Status: {'Active' if age >= 18 else 'Inactive'}"
print(f"嵌套条件: {info}")

# 列表和字典
items = ["apple", "banana", "orange"]
count = len(items)
list_str = f"Items ({count}): {', '.join(items)}"
print(f"列表处理: {list_str}")

# 字典
person = {"name": "Bob", "age": 30}
dict_str = f"Person: {person['name']}, Age: {person['age']}"
print(f"字典访问: {dict_str}")

# ========== 7. f-string 与引号 ==========
print("\n" + "=" * 60)
print("7. f-string 与引号的使用")
print("=" * 60)

name = "Alice"

# 单引号 f-string
fstr1 = f'Hello, {name}!'
print(f"单引号: {fstr1}")

# 双引号 f-string
fstr2 = f"Hello, {name}!"
print(f"双引号: {fstr2}")

# 嵌套引号
quote1 = f'He said "Hello, {name}!"'
quote2 = f"It's {name}'s birthday"
print(f"嵌套双引号: {quote1}")
print(f"嵌套单引号: {quote2}")

# 三引号 f-string（多行）
multi_line = f"""
Name: {name}
Age: {age}
City: NYC
"""
print(f"多行 f-string: {multi_line}")

# ========== 8. f-string 性能优势 ==========
print("\n" + "=" * 60)
print("8. f-string 性能优势")
print("=" * 60)

print("""
f-string 的优势：
1. ✅ 性能最好（编译时优化）
2. ✅ 语法最简洁
3. ✅ 可读性最强
4. ✅ 支持表达式和方法调用
5. ✅ 类型安全（编译时检查）

性能对比（从快到慢）：
1. f-string（最快）
2. .format()
3. % 格式化
4. 字符串拼接（最慢）
""")

# ========== 9. 常见错误 ==========
print("\n" + "=" * 60)
print("9. 常见错误和注意事项")
print("=" * 60)

# 错误 1：忘记加 'f' 前缀
name = "Alice"
# wrong = "My name is {name}"  # ❌ 不会格式化，会原样输出
wrong = "My name is {name}"
right = f"My name is {name}"
print(f"❌ 忘记 f: {wrong}")
print(f"✅ 正确: {right}")

# 错误 2：在 f-string 中使用引号冲突
name = "Alice"
# wrong = f'It's {name}'  # ❌ 语法错误（引号冲突）
right1 = f"It's {name}"  # ✅ 外层双引号
right2 = f'It\'s {name}'  # ✅ 转义单引号
print(f"正确写法1: {right1}")
print(f"正确写法2: {right2}")

# 错误 3：在 f-string 中使用反斜杠
# wrong = f"Path: C:\Users\{name}"  # ❌ 语法错误
right = f"Path: C:\\Users\\{name}"  # ✅ 转义反斜杠
print(f"路径处理: {right}")

# 注意：f-string 中不能直接使用反斜杠，需要转义或使用变量
path = r"C:\Users"
user = "Alice"
correct = f"{path}\\{user}"
print(f"路径拼接: {correct}")

# ========== 10. 实际应用示例 ==========
print("\n" + "=" * 60)
print("10. 实际应用示例")
print("=" * 60)

# 示例 1：API 响应格式化
user_id = 123
username = "alice"
email = "alice@example.com"
response = f"""
{{
    "id": {user_id},
    "username": "{username}",
    "email": "{email}",
    "status": "active"
}}
"""
print("API 响应示例:")
print(response)

# 示例 2：日志消息
import logging
level = "INFO"
message = "User logged in"
log_msg = f"[{level}] {message} - User: {username}"
print(f"日志消息: {log_msg}")

# 示例 3：SQL 查询（注意：实际应用中要防止 SQL 注入）
table = "users"
column = "name"
value = "Alice"
# 注意：这只是示例，实际应用要使用参数化查询
sql = f"SELECT * FROM {table} WHERE {column} = '{value}'"
print(f"SQL 查询示例: {sql}")

# 示例 4：文件路径
base_path = "/Users"
username = "alice"
filename = "data.txt"
file_path = f"{base_path}/{username}/{filename}"
print(f"文件路径: {file_path}")

# 示例 5：错误消息
error_code = 404
resource = "user"
error_msg = f"Error {error_code}: {resource.capitalize()} not found"
print(f"错误消息: {error_msg}")

# ========== 11. 对比 JavaScript 模板字符串 ==========
print("\n" + "=" * 60)
print("11. 对比 JavaScript 模板字符串")
print("=" * 60)

print("""
JavaScript 模板字符串：
```javascript
const name = "Alice";
const age = 25;
const message = `My name is ${name}, I'm ${age} years old.`;
```

Python f-string：
```python
name = "Alice"
age = 25
message = f"My name is {name}, I'm {age} years old."
```

相似点：
- 都使用变量插值
- 都支持表达式
- 都支持多行字符串

区别：
- JavaScript 使用反引号 ` 和 ${}
- Python 使用 f 前缀和 {}
- Python 支持更多格式化选项（如 :.2f）
""")

print("\n" + "=" * 60)
print("总结：f-string 是 Python 最现代、最推荐的字符串格式化方法！")
print("=" * 60)
