# HTML 与 CSS 面试题

---

## 1. 语义化 HTML

**问：什么是语义化 HTML？有什么好处？**

**答：**

使用具有**含义的标签**描述内容结构，而不是一味用 div/span。

**常见语义标签**：`<header>`、`<nav>`、`<main>`、`<article>`、`<section>`、`<aside>`、`<footer>`、`<figure>`、`<figcaption>`、`<time>`、`<mark>` 等。

**好处**：
- 利于 SEO
- 利于无障碍（屏幕阅读器）
- 代码可读性更好
- 结构清晰，便于维护

---

## 2. 盒模型（content、padding、border、margin）

**问：标准盒模型和 IE 盒模型的区别？box-sizing 的作用？**

**答：**

- **标准盒模型（content-box）**：`width/height` 只包含 content，不含 padding、border
- **IE 盒模型（border-box）**：`width/height` 包含 content + padding + border

```css
.box {
  width: 100px;
  padding: 10px;
  border: 5px solid;
}
/* content-box: 总宽度 = 100 + 20 + 10 = 130px */
/* border-box: 总宽度 = 100px，content = 70px */

box-sizing: border-box;  /* 常用，便于布局计算 */
```

---

## 3. display：block、inline、inline-block

**问：block、inline、inline-block 的区别？**

**答：**

| 类型 | 特点 | 常见元素 |
|------|------|----------|
| block | 独占一行，可设宽高、margin | div、p、h1、ul |
| inline | 不换行，宽高由内容决定，margin 仅左右有效 | span、a、strong |
| inline-block | 不换行，可设宽高，兼具两者特点 | img、input、button |

```css
span {
  display: inline-block;
  width: 100px;   /* inline 无法设置 */
  margin: 10px;   /* inline 上下 margin 无效，inline-block 有效 */
}
```

---

## 4. Flexbox 布局

**问：Flexbox 常用属性？如何实现常见布局？**

**答：**

### 容器属性

```css
.container {
  display: flex;
  flex-direction: row | column | row-reverse | column-reverse;
  flex-wrap: nowrap | wrap;
  justify-content: flex-start | center | flex-end | space-between | space-around;
  align-items: flex-start | center | flex-end | stretch | baseline;
  align-content: 多行时的垂直对齐;
  gap: 10px;
}
```

### 子项属性

```css
.item {
  flex-grow: 1;      /* 放大比例 */
  flex-shrink: 1;    /* 缩小比例 */
  flex-basis: 0;     /* 初始尺寸 */
  flex: 1 1 0;       /* 简写 */
  align-self: center;
  order: 1;
}
```

### 常见布局

```css
/* 水平垂直居中 */
.container {
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 两端对齐 */
.container {
  display: flex;
  justify-content: space-between;
}

/* 等分布局 */
.item { flex: 1; }
```

---

## 5. CSS Grid 布局

**问：Grid 和 Flexbox 的区别？Grid 常用属性？**

**答：**

- **Flexbox**：一维（行或列）
- **Grid**：二维（行+列）

```css
.container {
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;  /* 三列等分 */
  grid-template-rows: 100px auto;
  gap: 10px;
  grid-template-areas:
    "header header header"
    "sidebar main main"
    "footer footer footer";
}

.item {
  grid-column: 1 / 3;
  grid-row: 1;
  grid-area: header;
}
```

---

## 6. Flexbox vs Grid 区别与选型

**问：什么时候用 Flex，什么时候用 Grid？**

**答：**

| 场景 | 推荐 |
|------|------|
| 一维布局（横向或纵向） | Flexbox |
| 二维网格、整体页面布局 | Grid |
| 导航栏、卡片列表 | Flexbox |
| 复杂多列布局、杂志式排版 | Grid |
| 内容驱动（数量不定） | Flexbox |
| 结构固定、行列明确 | Grid |

---

## 7. 响应式设计（媒体查询、rem/em）

**问：如何实现响应式？rem 和 em 的区别？**

**答：**

### 媒体查询

```css
@media (max-width: 768px) {
  .sidebar { display: none; }
  .main { width: 100%; }
}

@media (min-width: 769px) and (max-width: 1024px) {
  .container { padding: 20px; }
}
```

### rem vs em

| 单位 | 参照 | 特点 |
|------|------|------|
| rem | 根元素 `html` 的 font-size | 全局统一，便于整体缩放 |
| em | 当前元素或父元素的 font-size | 会层层继承，易混乱 |

```css
html { font-size: 16px; }
.box { font-size: 2rem; }   /* 32px */
.child { width: 10em; }    /* 相对于 .box 的 font-size */
```

**移动端适配**：设置 `html { font-size: calc(100vw / 7.5) }` 等，配合 rem 实现等比缩放。

---

## 8. BFC 与清除浮动

**问：什么是 BFC？如何触发？能解决什么问题？**

**答：**

**BFC（块级格式化上下文）**：独立渲染区域，内部布局不影响外部。

**触发条件**：
- `overflow: hidden/auto/scroll`
- `float: left/right`
- `position: absolute/fixed`
- `display: inline-block/flex/grid`

**常见用途**：
1. **清除浮动**：父元素 `overflow: hidden` 或 `::after { clear: both }`
2. **防止 margin 重叠**：给其中一个包一层 BFC
3. **自适应两栏布局**：一侧 float，另一侧形成 BFC 避免重叠

```css
.clearfix::after {
  content: '';
  display: block;
  clear: both;
}
```

---

## 9. 居中方案

**问：列举几种水平垂直居中的方式？**

**答：**

```css
/* 1. Flex（最常用） */
.parent {
  display: flex;
  justify-content: center;
  align-items: center;
}

/* 2. Grid */
.parent {
  display: grid;
  place-items: center;
}

/* 3. 绝对定位 + transform */
.child {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

/* 4. 绝对定位 + margin（需已知宽高） */
.child {
  position: absolute;
  left: 0; right: 0; top: 0; bottom: 0;
  margin: auto;
  width: 100px;
  height: 100px;
}

/* 5. 行内元素：text-align + line-height */
.parent {
  text-align: center;
  line-height: 200px;
}
```
