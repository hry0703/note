import json

# ========== 错误示例：JSON 格式错误 ==========
# ❌ 错误：JSON 标准要求使用双引号，不能使用单引号
bad_json = "{'name': 'Alice'}" 

print("尝试解析错误的 JSON（使用单引号）：")
try:
    data = json.loads(bad_json)
except json.JSONDecodeError as e:
    print(f"❌ JSON 解析错误: {e}")
    print("原因：JSON 标准要求属性名和字符串值必须使用双引号\n")

# ========== 正确示例 ==========
# ✅ 正确：使用双引号
good_json = '{"name": "Alice"}'  # 注意：外层用单引号，内层用双引号

print("解析正确的 JSON（使用双引号）：")
try:
    data = json.loads(good_json)
    print(f"✅ 解析成功: {data}")
    print(f"   类型: {type(data)}")
    print(f"   姓名: {data['name']}")
except json.JSONDecodeError as e:
    print(f"❌ 错误: {e}")

# ========== 说明 ==========
print("\n" + "="*50)
print("关键点：")
print("1. JSON 标准要求：属性名和字符串值必须使用双引号 \"")
print("2. Python 字符串可以用单引号或双引号包裹")
print("3. 推荐写法：外层用单引号，内层用双引号")
print("   例如：'{\"name\": \"Alice\"}'")
print("="*50)