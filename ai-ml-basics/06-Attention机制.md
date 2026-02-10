# Attention 机制基础

## 📋 什么是 Attention？

**Attention（注意力机制）**是一种让模型能够关注输入序列中重要部分的技术，是现代 NLP 和 AI 的核心机制。

## 🎯 核心思想

**类比**：就像人类阅读时，会重点关注某些关键词或句子

**作用**：
- 决定模型应该关注输入的哪些部分
- 建立不同位置之间的依赖关系
- 提高模型对长距离依赖的建模能力

## 🏗️ Attention 类型

### 1. 自注意力（Self-Attention）

**定义**：序列中的每个位置都关注序列中的所有位置（包括自己）

**应用**：
- Transformer 编码器
- BERT

**计算**：
```python
Attention(Q, K, V) = softmax(QK^T / √d_k) V
```

### 2. 交叉注意力（Cross-Attention）

**定义**：一个序列关注另一个序列

**应用**：
- Transformer 解码器
- 机器翻译

**计算**：
```python
# Query 来自解码器，Key 和 Value 来自编码器
CrossAttention(Q_decoder, K_encoder, V_encoder)
```

### 3. 多头注意力（Multi-Head Attention）

**定义**：并行计算多个注意力头，每个头关注不同的方面

**优势**：
- 捕获不同类型的关系
- 提高模型表达能力

**计算**：
```python
MultiHead(Q, K, V) = Concat(head_1, ..., head_h) W^O

# 每个头独立计算
head_i = Attention(QW_i^Q, KW_i^K, VW_i^V)
```

## 💡 Attention 计算过程

### 步骤 1：计算 Query、Key、Value

```python
Q = X @ W_q  # Query：查询向量
K = X @ W_k  # Key：键向量
V = X @ W_v  # Value：值向量
```

### 步骤 2：计算注意力分数

```python
scores = Q @ K.T / sqrt(d_k)
# 分数表示每个位置对其他位置的关注程度
```

### 步骤 3：应用 Softmax

```python
attention_weights = softmax(scores)
# 归一化，得到注意力权重
```

### 步骤 4：加权求和

```python
output = attention_weights @ V
# 根据权重对 Value 进行加权求和
```

## 🔧 代码示例

```python
import torch
import torch.nn as nn
import math

class Attention(nn.Module):
    def __init__(self, d_model):
        super().__init__()
        self.d_model = d_model
        self.W_q = nn.Linear(d_model, d_model)
        self.W_k = nn.Linear(d_model, d_model)
        self.W_v = nn.Linear(d_model, d_model)
    
    def forward(self, x):
        Q = self.W_q(x)
        K = self.W_k(x)
        V = self.W_v(x)
        
        # 计算注意力分数
        scores = torch.matmul(Q, K.transpose(-2, -1)) / math.sqrt(self.d_model)
        
        # Softmax
        attention_weights = torch.softmax(scores, dim=-1)
        
        # 加权求和
        output = torch.matmul(attention_weights, V)
        
        return output, attention_weights
```

## 📊 Attention 可视化

### 注意力权重矩阵

```
       位置1  位置2  位置3  位置4
位置1  0.3   0.2   0.3   0.2
位置2  0.1   0.4   0.3   0.2
位置3  0.2   0.2   0.4   0.2
位置4  0.1   0.1   0.2   0.6
```

**解读**：
- 位置 1 最关注位置 1 和位置 3
- 位置 4 最关注自己（位置 4）

## 🎯 应用场景

### 1. 机器翻译

```
源语言：The cat sat on the mat
         ↓
注意力：关注 "cat" 和 "mat" 的关系
         ↓
目标语言：猫坐在垫子上
```

### 2. 文本摘要

```
原文：长文本...
      ↓
注意力：关注关键句子
      ↓
摘要：关键信息
```

### 3. 图像描述

```
图像：包含多个对象
      ↓
注意力：关注不同区域
      ↓
描述：生成文本描述
```

## ⚡ 优势

- ✅ **长距离依赖**：直接建模任意距离的依赖
- ✅ **可解释性**：注意力权重可以可视化
- ✅ **并行化**：可以并行计算
- ✅ **灵活性**：适用于多种任务

## ⚠️ 挑战

- ❌ **计算复杂度**：O(n²) 的复杂度
- ❌ **内存消耗**：需要存储注意力矩阵
- ❌ **长序列**：处理长序列时效率低

## 🔗 相关概念

- **Self-Attention**：自注意力
- **Cross-Attention**：交叉注意力
- **Scaled Dot-Product Attention**：缩放点积注意力
- **Multi-Head Attention**：多头注意力
- **Attention Mask**：注意力掩码

---

*最后更新：2024年*
