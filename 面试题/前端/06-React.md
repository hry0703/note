# React 面试题

---

## 1. JSX 原理

**问：JSX 是什么？如何转换成 JS？**

**答：**

- **JSX**：JavaScript 的语法扩展，类似 HTML 的写法，用于描述 UI
- **本质**：通过 Babel 等工具编译成 `React.createElement()` 调用

```javascript
// JSX
<div className="box" id="app">
  <span>hello</span>
</div>

// 编译后
React.createElement('div', { className: 'box', id: 'app' },
  React.createElement('span', null, 'hello')
);

// React 17+ 使用新的 JSX 转换，无需显式引入 React
import { jsx } from 'react/jsx-runtime';
jsx('div', { className: 'box', children: jsx('span', { children: 'hello' }) });
```

---

## 2. 虚拟 DOM 与 diff

**问：React 的 diff 算法是怎样的？**

**答：**

**策略**：
- **同层比较**：只比较同一层级
- **类型不同**：直接替换整棵子树
- **类型相同**：比较属性，更新变化的属性
- **列表**：通过 key 判断是否复用，key 要稳定唯一

**Fiber 下的 diff**：可中断、可恢复，将 diff 拆成多个小任务，配合调度器实现优先级。

---

## 3. Fiber 架构

**问：什么是 Fiber？解决了什么问题？**

**答：**

**Fiber**：React 16 引入的新的协调引擎，将虚拟 DOM 节点表示为 Fiber 节点（一种数据结构）。

**结构**：每个 Fiber 有 `child`、`sibling`、`return` 等指针，形成链表树。

**解决的问题**：
- **可中断**：React 15 的 Reconciler 是同步递归，无法中断
- **可恢复**：保存中断现场，之后恢复
- **优先级**：高优先级任务可打断低优先级

**流程**：Reconciliation（协调，可中断）→ Commit（提交，不可中断，更新 DOM）。

---

## 4. Hooks 原理（useState、useEffect 等）

**问：Hooks 的原理？为什么不能在条件里调用？**

**答：**

**原理**：函数组件内部维护一个 **Hooks 链表**，按调用顺序依次挂载。每次渲染按相同顺序遍历链表，取对应 hook 的状态。

```javascript
// 简化理解
let hooks = [];
let index = 0;

function useState(initial) {
  const state = hooks[index] ?? initial;
  const _index = index;
  const setState = (newVal) => {
    hooks[_index] = newVal;
    scheduleUpdate();
  };
  index++;
  return [state, setState];
}
```

**规则**：
- 只在**顶层**调用，不要在条件、循环中调用
- 只在**函数组件**或**自定义 Hook** 中调用

**原因**：顺序依赖调用顺序，条件调用会导致顺序变化，状态错乱。

---

## 5. 类组件 vs 函数组件

**问：类组件和函数组件的区别？**

**答：**

| 对比 | 类组件 | 函数组件 |
|------|--------|----------|
| 状态 | this.state | useState |
| 生命周期 | 生命周期方法 | useEffect 等 |
| this | 需要绑定 | 无 this |
| 逻辑复用 | HOC、render props | 自定义 Hooks |
| 代码量 | 较多 | 更简洁 |
| 未来 | 逐步被替代 | 推荐 |

函数组件 + Hooks 是 React 官方推荐写法，类组件主要用于兼容旧代码。

---

## 6. React Server Components（了解）

**问：React Server Components 是什么？**

**答：**

**RSC**：在服务端渲染的组件，不发送 JS 到客户端，减少包体积。

**特点**：
- 可直接访问服务端资源（数据库、文件系统等）
- 不参与客户端 bundle
- 与 Client Components 可混用（用 `'use client'` 标记）

**使用场景**：数据密集型组件、不依赖交互的静态内容等。

---

## 7. Redux / Zustand

**问：Redux 和 Zustand 的区别？**

**答：**

| 对比 | Redux | Zustand |
|------|-------|---------|
| 理念 | 单一 store、不可变、纯函数 | 简单、可变、直接改 |
| 样板代码 | 多（action、reducer） | 少 |
| 中间件 | 丰富 | 支持 subscribe 等 |
| 体积 | 较大 | 很小 |
| 适用 | 大型复杂应用 | 中小型、快速开发 |

```javascript
// Zustand
const useStore = create((set) => ({
  count: 0,
  increment: () => set((s) => ({ count: s.count + 1 })),
}));
```
