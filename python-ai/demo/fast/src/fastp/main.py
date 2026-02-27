from fastapi import FastAPI
import logging

# 配置日志
logging.basicConfig(
    level=logging.DEBUG,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

app = FastAPI()


@app.get("/")
async def root():
    # 方法1: 使用 logging 调试
    logger.debug("收到根路径请求")
    
    # 方法2: 使用 print 调试（简单场景）
    print("处理根路径请求")
    
    # 方法3: 使用 breakpoint() 调试（取消注释以启用）
    # breakpoint()  # 会在这里暂停，进入调试器
    
    message = {"message": "Hello World"}
    logger.info(f"返回消息: {message}")
    return message


@app.get("/debug-example")
async def debug_example():
    """
    演示调试技巧的示例端点
    """
    # 模拟一些变量
    x = 10
    y = 20
    result = x + y
    
    # 使用 logging 查看变量值
    logger.debug(f"x = {x}, y = {y}, result = {result}")
    
    # 如果需要交互式调试，取消下面的注释
    # breakpoint()  # 在这里可以检查所有变量
    
    return {"x": x, "y": y, "result": result}


@app.get("/test-debug")
async def test_debug(name: str = "测试用户"):
    """
    专门用于测试调试功能的端点
    建议在这里设置断点进行测试
    """
    logger.info(f"收到测试请求，参数 name = {name}")
    
    # 在这里设置断点！程序会在这里暂停
    greeting = f"你好, {name}!"
    
    # 模拟一些计算
    numbers = [1, 2, 3, 4, 5]
    total = sum(numbers)
    average = total / len(numbers)
    
    logger.debug(f"计算结果: total={total}, average={average}")
    
    # 构建响应
    response = {
        "greeting": greeting,
        "numbers": numbers,
        "total": total,
        "average": average,
        "message": "调试测试成功！"
    }
    
    return response


@app.post("/test-hello")
async def test_hello():
    return {"message": "test hahah"}