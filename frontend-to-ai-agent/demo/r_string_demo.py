"""
r-string（原始字符串，Raw String）详解
r-string 是 Python 中用于禁用转义字符的特殊字符串格式
"""

# ========== 1. 什么是 r-string？ ==========
print("=" * 60)
print("1. 什么是 r-string？")
print("=" * 60)

# r-string = raw string（原始字符串）
# 在字符串前加 'r' 或 'R'，字符串中的反斜杠 \ 不会被转义

# 普通字符串：\n 会被转义为换行符
normal_str = "Line 1\nLine 2"
print("普通字符串:")
print(normal_str)
print(f"长度: {len(normal_str)}")  # 注意：\n 只算一个字符

# 原始字符串：\n 保持原样（两个字符：\ 和 n）
raw_str = r"Line 1\nLine 2"
print("\n原始字符串:")
print(raw_str)
print(f"长度: {len(raw_str)}")  # \n 算两个字符

# ========== 2. 为什么需要 r-string？ ==========
print("\n" + "=" * 60)
print("2. 为什么需要 r-string？（文件路径问题）")
print("=" * 60)

# 问题场景：Windows 文件路径
# ❌ 错误：反斜杠会被转义
try:
    # wrong_path = "C:\Users\Alice\note.txt"  # 语法错误！
    # \U 会被解释为 Unicode 转义序列
    pass
except:
    print("❌ 错误路径会导致语法错误或转义问题")

# 方法 1：使用双反斜杠转义（麻烦）
escaped_path = "C:\\Users\\Alice\\note.txt"
print(f"转义路径: {escaped_path}")

# 方法 2：使用正斜杠（跨平台，但 Windows 风格不一致）
forward_slash = "C:/Users/Alice/note.txt"
print(f"正斜杠路径: {forward_slash}")

# ✅ 方法 3：使用 r-string（最推荐）
raw_path = r"C:\Users\Alice\note.txt"
print(f"原始字符串路径: {raw_path}")

# ========== 3. r-string 基本用法 ==========
print("\n" + "=" * 60)
print("3. r-string 基本用法")
print("=" * 60)

# 语法：r"字符串" 或 R"字符串"（大小写都可以）

# 示例 1：文件路径
path1 = r"C:\Users\Alice\Documents\file.txt"
path2 = R"C:\Users\Alice\Documents\file.txt"
print(f"路径1: {path1}")
print(f"路径2: {path2}")
print(f"两者相等: {path1 == path2}")  # True

# 示例 2：正则表达式（常用场景）
import re

# 普通字符串：需要转义
pattern1 = "\\d+\\s+\\w+"  # 匹配：数字 + 空格 + 单词
print(f"普通字符串正则: {pattern1}")

# r-string：不需要转义
pattern2 = r"\d+\s+\w+"  # 更清晰
print(f"原始字符串正则: {pattern2}")

# 测试匹配
text = "123 hello"
match1 = re.search(pattern1, text)
match2 = re.search(pattern2, text)
print(f"匹配结果1: {match1.group() if match1 else None}")
print(f"匹配结果2: {match2.group() if match2 else None}")

# ========== 4. r-string vs 普通字符串对比 ==========
print("\n" + "=" * 60)
print("4. r-string vs 普通字符串对比")
print("=" * 60)

# 转义字符对比
print("转义字符对比：")
print("-" * 40)

# \n = 换行符
normal_newline = "Line 1\nLine 2"
raw_newline = r"Line 1\nLine 2"
print(f"普通字符串 \\n: {repr(normal_newline)}")
print(f"原始字符串 \\n: {repr(raw_newline)}")

# \t = 制表符
normal_tab = "Column1\tColumn2"
raw_tab = r"Column1\tColumn2"
print(f"\n普通字符串 \\t: {repr(normal_tab)}")
print(f"原始字符串 \\t: {repr(raw_tab)}")

# \\ = 反斜杠
normal_backslash = "Path: C:\\Users"
raw_backslash = r"Path: C:\Users"
print(f"\n普通字符串 \\\\: {repr(normal_backslash)}")
print(f"原始字符串 \\\\: {repr(raw_backslash)}")

# ========== 5. 实际应用场景 ==========
print("\n" + "=" * 60)
print("5. 实际应用场景")
print("=" * 60)

# 场景 1：Windows 文件路径
print("场景 1：Windows 文件路径")
windows_path = r"C:\Users\Alice\Documents\data.txt"
print(f"路径: {windows_path}")

# 场景 2：正则表达式（最常用）
print("\n场景 2：正则表达式")
import re

# 匹配邮箱（使用 r-string 更清晰）
email_pattern = r"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"
email = "alice@example.com"
is_valid = bool(re.match(email_pattern, email))
print(f"邮箱: {email}, 有效: {is_valid}")

# 匹配日期
date_pattern = r"\d{4}-\d{2}-\d{2}"
date_text = "Today is 2024-01-15"
match = re.search(date_pattern, date_text)
print(f"日期匹配: {match.group() if match else None}")

# 场景 3：SQL 查询中的字符串
print("\n场景 3：SQL 查询")
sql_query = r"SELECT * FROM users WHERE name LIKE '%Alice%'"
print(f"SQL: {sql_query}")

# 场景 4：JSON 字符串中的转义
print("\n场景 4：JSON 字符串")
json_str = r'{"path": "C:\Users\Alice"}'
print(f"JSON: {json_str}")

# 场景 5：命令行参数
print("\n场景 5：命令行参数")
cmd = r'python script.py --path "C:\Users\Alice\file.txt"'
print(f"命令: {cmd}")

# ========== 6. r-string 的限制 ==========
print("\n" + "=" * 60)
print("6. r-string 的限制和注意事项")
print("=" * 60)

# 限制 1：不能以奇数个反斜杠结尾
# wrong = r"C:\Users\"  # ❌ 语法错误
# 原因：最后一个反斜杠会转义结束引号

# 解决方案 1：使用双反斜杠
correct1 = r"C:\Users" + "\\"
print(f"方案1: {correct1}")

# 解决方案 2：使用普通字符串
correct2 = "C:\\Users\\"
print(f"方案2: {correct2}")

# 限制 2：引号仍然需要转义（但反斜杠不会转义）
# wrong = r'It's a path'  # ❌ 语法错误（引号冲突）

# 正确：使用不同的引号
correct3 = r"It's a path"  # ✅ 外层用双引号
correct4 = r'He said "Hello"'  # ✅ 外层用单引号
print(f"方案3: {correct3}")
print(f"方案4: {correct4}")

# 限制 3：r-string 中仍然可以使用转义引号
escaped_quote = r'It\'s a path'  # ✅ 可以，但 \' 会保持原样
print(f"转义引号: {escaped_quote}")  # 输出：It\'s a path（保持原样）

# ========== 7. r-string 与 f-string 结合 ==========
print("\n" + "=" * 60)
print("7. r-string 与 f-string 结合使用")
print("=" * 60)

# 可以同时使用 rf 或 fr（顺序不重要）
username = "Alice"
base_path = r"C:\Users"

# rf-string（推荐：先 r 后 f）
path1 = rf"{base_path}\{username}\file.txt"
print(f"rf-string: {path1}")

# fr-string（也可以：先 f 后 r）
path2 = fr"{base_path}\{username}\file.txt"
print(f"fr-string: {path2}")

# 注意：在 rf-string 中，变量插值仍然有效，但反斜杠不会转义
pattern = r"\d+"
text = "123"
rf_pattern = rf"Pattern: {pattern}, Text: {text}"
print(f"rf-string 正则: {rf_pattern}")

# ========== 8. 常见错误 ==========
print("\n" + "=" * 60)
print("8. 常见错误")
print("=" * 60)

# 错误 1：忘记 r-string 导致路径错误
print("错误 1：忘记使用 r-string")
try:
    # wrong = "C:\Users\Alice\note.txt"  # 会报错或产生意外结果
    print("❌ 路径中的 \\n 会被转义为换行符")
except:
    pass

# 错误 2：r-string 以奇数个反斜杠结尾
print("\n错误 2：r-string 以奇数个反斜杠结尾")
try:
    # wrong = r"C:\Users\"  # 语法错误
    print("❌ r-string 不能以奇数个反斜杠结尾")
except:
    pass

# 错误 3：在 r-string 中期望转义字符生效
print("\n错误 3：在 r-string 中期望转义字符生效")
raw_str = r"Line 1\nLine 2"
print(f"r-string: {raw_str}")  # 输出：Line 1\nLine 2（不会换行）
print("❌ 在 r-string 中，\\n 不会转义为换行符")

# ========== 9. 最佳实践 ==========
print("\n" + "=" * 60)
print("9. 最佳实践")
print("=" * 60)

print("""
✅ 推荐使用 r-string 的场景：

1. Windows 文件路径
   path = r"C:\\Users\\Alice\\file.txt"

2. 正则表达式（强烈推荐）
   pattern = r"\\d+\\.\\d+"

3. 包含大量反斜杠的字符串
   regex = r"^[a-z]+\\\\[0-9]+$"

4. SQL 查询中的字符串
   query = r"SELECT * FROM users WHERE name LIKE '%test%'"

5. 命令行参数
   cmd = r'python script.py --path "C:\\data"'

❌ 不推荐使用 r-string 的场景：

1. 需要转义字符时（如换行符）
   normal = "Line 1\\nLine 2"  # ✅ 使用普通字符串

2. 简单的字符串（没有反斜杠）
   simple = "Hello, World!"  # ✅ 不需要 r-string

3. 需要动态转义时
   # 使用普通字符串 + 转义
""")

# ========== 10. 对比其他语言 ==========
print("\n" + "=" * 60)
print("10. 对比其他语言")
print("=" * 60)

print("""
JavaScript:
- 没有原始字符串（ES6 之前）
- ES6+ 有模板字符串，但没有原始字符串
- 需要使用转义：path = "C:\\\\\\\\Users\\\\\\\\Alice"

Python:
- r-string 让路径更清晰
- path = r"C:\\\\Users\\\\Alice"

C#:
- 使用 @ 前缀：string path = @"C:\\\\Users\\\\Alice";

Go:
- 使用反引号：path := `C:\\\\Users\\\\Alice`
""")

# ========== 11. 完整示例 ==========
print("\n" + "=" * 60)
print("11. 完整示例：文件操作")
print("=" * 60)

import os

# 使用 r-string 定义路径
base_dir = r"C:\Users\Alice\Documents"
file_name = "data.txt"
full_path = os.path.join(base_dir, file_name)

print(f"基础目录: {base_dir}")
print(f"文件名: {file_name}")
print(f"完整路径: {full_path}")

# 正则表达式示例
import re

# 匹配文件路径中的文件名
path_pattern = r"([^\\]+)\.(\w+)$"
test_path = r"C:\Users\Alice\Documents\report.pdf"
match = re.search(path_pattern, test_path)
if match:
    filename = match.group(1)
    extension = match.group(2)
    print(f"\n文件路径: {test_path}")
    print(f"文件名: {filename}")
    print(f"扩展名: {extension}")

print("\n" + "=" * 60)
print("总结：r-string 用于禁用转义字符，特别适合文件路径和正则表达式！")
print("=" * 60)
