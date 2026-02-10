# Transformer 架构基础

## 📋 什么是 Transformer？

**Transformer** 是一种基于注意力机制的神经网络架构，由 Google 在 2017 年提出，是现代 NLP 和 AI 的基础架构。

## 🎯 核心创新

### 传统架构的问题
- ❌ RNN/LSTM：难以并行化，训练慢
- ❌ 长距离依赖：信息传递困难
- ❌ 序列处理：必须按顺序处理

### Transformer 的优势
- ✅ **并行化**：可以并行处理所有位置
- ✅ **注意力机制**：直接建模长距离依赖
- ✅ **高效训练**：训练速度快

## 🏗️ Transformer 架构

### 整体结构

```
输入序列
    ↓
嵌入层（Embedding）
    ↓
位置编码（Positional Encoding）
    ↓
编码器（Encoder）× N 层
    ├── 多头注意力（Multi-Head Attention）
    ├── 前馈网络（Feed Forward）
    └── 残差连接 + 层归一化
    ↓
解码器（Decoder）× N 层
    ├── 掩码多头注意力
    ├── 编码器-解码器注意力
    ├── 前馈网络
    └── 残差连接 + 层归一化
    ↓
输出层（Output Layer）
    ↓
输出序列
```

### 关键组件

#### 1. 自注意力机制（Self-Attention）

```python
# 注意力计算公式
Attention(Q, K, V) = softmax(QK^T / √d_k) V

# Q: Query（查询）
# K: Key（键）
# V: Value（值）
```

#### 2. 多头注意力（Multi-Head Attention）

```python
# 多个注意力头并行计算
MultiHead(Q, K, V) = Concat(head_1, ..., head_h) W^O

# 每个头：
head_i = Attention(QW_i^Q, KW_i^K, VW_i^V)
```

#### 3. 位置编码（Positional Encoding）

```python
# 正弦位置编码
PE(pos, 2i) = sin(pos / 10000^(2i/d_model))
PE(pos, 2i+1) = cos(pos / 10000^(2i/d_model))
```

#### 4. 前馈网络（Feed Forward）

```python
FFN(x) = max(0, xW_1 + b_1)W_2 + b_2
```

## 💡 编码器（Encoder）

### 结构
```
输入 → 嵌入 + 位置编码
    ↓
多头自注意力
    ↓
残差连接 + 层归一化
    ↓
前馈网络
    ↓
残差连接 + 层归一化
    ↓
输出
```

### 特点
- 双向上下文：可以看到整个输入序列
- 并行处理：所有位置同时处理
- 深度堆叠：通常 6-12 层

## 💡 解码器（Decoder）

### 结构
```
输入 → 嵌入 + 位置编码
    ↓
掩码多头自注意力（只能看到前面的位置）
    ↓
残差连接 + 层归一化
    ↓
编码器-解码器注意力（关注编码器输出）
    ↓
残差连接 + 层归一化
    ↓
前馈网络
    ↓
残差连接 + 层归一化
    ↓
输出
```

### 特点
- 自回归：逐个生成输出
- 掩码机制：防止看到未来信息
- 交叉注意力：关注编码器输出

## 🔧 关键机制

### 1. 注意力机制

**作用**：决定模型应该关注输入的哪些部分

**计算过程**：
1. 计算 Query、Key、Value
2. 计算注意力分数：Q·K^T
3. 缩放：除以 √d_k
4. Softmax：归一化
5. 加权求和：与 Value 相乘

### 2. 残差连接

```python
# 残差连接
output = LayerNorm(x + Sublayer(x))
```

**作用**：
- 缓解梯度消失
- 允许训练更深的网络

### 3. 层归一化

```python
# 层归一化
LayerNorm(x) = γ * (x - μ) / √(σ² + ε) + β
```

**作用**：
- 稳定训练
- 加速收敛

## 📝 代码示例

```python
import torch
import torch.nn as nn
import math

class MultiHeadAttention(nn.Module):
    def __init__(self, d_model, num_heads):
        super().__init__()
        self.d_model = d_model
        self.num_heads = num_heads
        self.d_k = d_model // num_heads
        
        self.W_q = nn.Linear(d_model, d_model)
        self.W_k = nn.Linear(d_model, d_model)
        self.W_v = nn.Linear(d_model, d_model)
        self.W_o = nn.Linear(d_model, d_model)
    
    def forward(self, Q, K, V, mask=None):
        batch_size = Q.size(0)
        
        # 线性变换并分头
        Q = self.W_q(Q).view(batch_size, -1, self.num_heads, self.d_k).transpose(1, 2)
        K = self.W_k(K).view(batch_size, -1, self.num_heads, self.d_k).transpose(1, 2)
        V = self.W_v(V).view(batch_size, -1, self.num_heads, self.d_k).transpose(1, 2)
        
        # 计算注意力
        scores = torch.matmul(Q, K.transpose(-2, -1)) / math.sqrt(self.d_k)
        if mask is not None:
            scores = scores.masked_fill(mask == 0, -1e9)
        attention_weights = torch.softmax(scores, dim=-1)
        output = torch.matmul(attention_weights, V)
        
        # 合并多头
        output = output.transpose(1, 2).contiguous().view(
            batch_size, -1, self.d_model
        )
        return self.W_o(output)
```

## 🎯 应用

### 1. 语言模型
- **GPT**：仅解码器架构
- **BERT**：仅编码器架构
- **T5**：编码器-解码器架构

### 2. 机器翻译
- 序列到序列任务
- 多语言翻译

### 3. 文本生成
- 对话系统
- 内容创作
- 代码生成

## ⚡ 优势

- ✅ **并行化**：训练和推理速度快
- ✅ **长距离依赖**：注意力机制直接建模
- ✅ **可扩展性**：可以堆叠更多层
- ✅ **通用性**：适用于多种任务

## ⚠️ 挑战

- ❌ **计算复杂度**：O(n²) 的注意力计算
- ❌ **内存消耗**：需要存储注意力矩阵
- ❌ **长序列**：处理长序列时效率低

## 🔗 相关概念

- **Self-Attention**：自注意力
- **Cross-Attention**：交叉注意力
- **Positional Encoding**：位置编码
- **Layer Normalization**：层归一化
- **Residual Connection**：残差连接

---

*最后更新：2024年*
