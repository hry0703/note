# Vue 面试题

---

## 1. Vue2 响应式原理（Object.defineProperty）

**问：Vue2 的响应式是如何实现的？有什么缺陷？**

**答：**

通过 **Object.defineProperty** 对 data 中的属性做 getter/setter 劫持，在 get 中收集依赖，在 set 中通知更新。

```javascript
function defineReactive(obj, key, val) {
  const dep = new Dep();
  Object.defineProperty(obj, key, {
    get() {
      Dep.target && dep.addDep(Dep.target);
      return val;
    },
    set(newVal) {
      if (newVal === val) return;
      val = newVal;
      dep.notify();
    }
  });
}
```

**缺陷**：
- 无法监听**数组索引**和**数组长度**变化
- 无法监听**对象新增/删除**属性
- 需要递归遍历，初始化较慢

**数组处理**：Vue2 对数组的 push、pop、shift、unshift、splice、sort、reverse 做了重写，在变更时触发更新。

---

## 2. Vue3 响应式原理（Proxy）

**问：Vue3 为什么用 Proxy 替代 Object.defineProperty？**

**答：**

**Proxy** 可以代理整个对象，监听增删改查，无需递归、无需对数组做特殊处理。

```javascript
const handler = {
  get(target, key) {
    track(target, key);
    return Reflect.get(target, key);
  },
  set(target, key, value) {
    const result = Reflect.set(target, key, value);
    trigger(target, key);
    return result;
  },
  deleteProperty(target, key) {
    const result = Reflect.deleteProperty(target, key);
    trigger(target, key);
    return result;
  }
};
const reactive = (obj) => new Proxy(obj, handler);
```

**优势**：
- 可监听数组索引、length、对象增删
- 按需代理，性能更好
- 支持 Map、Set 等

---

## 3. 虚拟 DOM 与 diff 算法

**问：Vue 的虚拟 DOM 和 diff 算法是怎样的？**

**答：**

**虚拟 DOM**：用 JS 对象描述真实 DOM，减少直接操作 DOM 的开销。

**diff 策略**：
- **同层比较**：只比较同一层级，不跨层
- **双端比较**：头头、尾尾、头尾、尾头 四种比较，减少移动
- **key 的作用**：帮助复用节点，避免就地复用导致的状态错乱

**流程**：数据变化 → 生成新 VNode → 与旧 VNode diff → 算出 patch → 更新真实 DOM

**Vue3 优化**：静态提升、patchFlag、事件缓存等，减少 diff 范围。

---

## 4. Vue3 Composition API

**问：Composition API 和 Options API 的区别？为什么用 Composition API？**

**答：**

| 对比 | Options API | Composition API |
|------|-------------|-----------------|
| 组织方式 | 按选项（data、methods、computed） | 按逻辑（setup 中按功能组织） |
| 逻辑复用 | mixin（易冲突） | 组合式函数（useXxx） |
| 类型推导 | 较弱 | 更好 |
| 代码组织 | 逻辑分散 | 相关逻辑集中 |

```javascript
// Composition API
function useCounter() {
  const count = ref(0);
  const increment = () => count.value++;
  return { count, increment };
}

export default {
  setup() {
    const { count, increment } = useCounter();
    return { count, increment };
  }
}
```

---

## 5. 生命周期

**问：Vue 的生命周期有哪些？**

**答：**

| 阶段 | 钩子 | 说明 |
|------|------|------|
| 创建 | beforeCreate | 实例初始化，data、methods 未创建 |
| 创建 | created | 实例创建完成，可访问 data、methods |
| 挂载 | beforeMount | 挂载前，DOM 未生成 |
| 挂载 | mounted | 挂载完成，可访问 DOM |
| 更新 | beforeUpdate | 数据更新前 |
| 更新 | updated | 数据更新后，DOM 已更新 |
| 销毁 | beforeUnmount | 销毁前（Vue3） |
| 销毁 | unmounted | 销毁后（Vue3） |

**Vue3 Composition API**：`onMounted`、`onUpdated`、`onUnmounted` 等，在 setup 中调用。

---

## 6. 组件通信方式

**问：Vue 组件间有哪些通信方式？**

**答：**

1. **props / emit**：父子组件
2. **v-model**：父子双向绑定（语法糖：props + emit）
3. **provide / inject**：祖先 → 后代
4. **ref**：父组件通过 ref 调用子组件方法
5. **Vuex / Pinia**：全局状态
6. **事件总线（EventBus）**：任意组件（Vue3 推荐用 mitt 等库）
7. **$attrs**：透传非 props 属性

```javascript
// provide / inject
provide('theme', 'dark');
const theme = inject('theme');
```

---

## 7. Vuex / Pinia

**问：Vuex 和 Pinia 的区别？**

**答：**

| 对比 | Vuex | Pinia |
|------|------|-------|
| 适用 | Vue2/3 | Vue3 |
| 模块 | 需要 modules | 天然多 store，无需 modules |
| 类型 | 需额外配置 | 天然支持 TS |
| API | mutation、action 分离 | 只有 action，可直接改 state |
| 体积 | 较大 | 更轻量 |

```javascript
// Pinia
const useUserStore = defineStore('user', {
  state: () => ({ name: '' }),
  actions: {
    setName(name) {
      this.name = name;  // 直接修改
    }
  }
});
```
