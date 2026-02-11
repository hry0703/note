# 微前端框架对比：Wujie vs Qiankun

> **文档目的**：深入对比 Wujie（无界）和 Qiankun 两个主流微前端框架，帮助开发者根据项目需求选择合适的技术方案

---

## 📋 目录

- [框架简介](#框架简介)
- [核心架构对比](#核心架构对比)
- [功能特性对比](#功能特性对比)
- [性能对比](#性能对比)
- [实际使用场景](#实际使用场景)
- [快速上手](#快速上手)
- [最佳实践](#最佳实践)
- [总结与建议](#总结与建议)

---

## 框架简介

### Wujie（无界）

**Wujie** 是由腾讯团队开发的微前端框架，采用 **Web Components + iframe** 的技术方案。

**核心特点**：
- ✅ **原生隔离**：基于 iframe 实现真正的样式和 JS 隔离
- ✅ **性能优秀**：无 JS 沙箱损耗，运行速度快
- ✅ **零侵入**：子应用无需改造，可直接接入
- ✅ **功能强大**：支持预加载、保活、通信等完整功能
- 📦 **体积小**：核心代码精简，打包体积小

**GitHub**：https://github.com/Tencent/wujie  
**文档**：https://wujie-micro.github.io/doc/

---

### Qiankun

**Qiankun** 是由蚂蚁金服（Ant Design 团队）开发的微前端解决方案，基于 **single-spa** 框架封装。

**核心特点**：
- ✅ **成熟稳定**：已服务 2000+ 线上应用，生产环境验证充分
- ✅ **生态完善**：文档齐全，社区活跃，问题解决方案多
- ✅ **功能完整**：样式隔离、JS 沙箱、预加载、通信等一应俱全
- ✅ **简单易用**：API 设计简洁，上手门槛低
- 📦 **社区支持**：有大量实践案例和最佳实践

**GitHub**：https://github.com/umijs/qiankun  
**文档**：https://qiankun.umijs.org/

---

## 核心架构对比

### 技术架构

| 对比项 | Wujie | Qiankun |
|--------|-------|---------|
| **底层技术** | Web Components + iframe | single-spa |
| **样式隔离** | iframe 原生隔离 | Shadow DOM / CSS Scope |
| **JS 隔离** | iframe 原生隔离 | Proxy 沙箱 / 快照沙箱 |
| **DOM 隔离** | iframe 原生隔离 | 无原生隔离 |
| **通信机制** | props + eventBus | props + globalState |

### 架构原理

#### Wujie 架构

```
主应用 (Main App)
    ↓
Wujie 容器
    ↓
Web Components (Custom Element)
    ↓
iframe (子应用运行环境)
    ↓
子应用 (Sub App)
```

**优势**：
- iframe 提供原生隔离，样式和 JS 完全隔离
- 无需额外的沙箱机制，性能损耗小
- 子应用可以独立运行，互不干扰

**劣势**：
- iframe 通信需要 postMessage，相对复杂
- 某些浏览器 API 在 iframe 中受限

#### Qiankun 架构

```
主应用 (Main App)
    ↓
Qiankun 容器
    ↓
single-spa 生命周期管理
    ↓
JS 沙箱 (Proxy/Snapshot)
    ↓
样式隔离 (Shadow DOM/CSS Scope)
    ↓
子应用 (Sub App)
```

**优势**：
- 基于 single-spa，架构清晰
- 灵活的沙箱机制，可配置
- 通信方式多样，支持全局状态管理

**劣势**：
- 沙箱机制有性能损耗
- 样式隔离需要额外处理，可能不够彻底

---

## 功能特性对比

### 详细功能对比表

| 功能特性 | Wujie | Qiankun | 说明 |
|---------|-------|---------|------|
| **样式隔离** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Wujie 基于 iframe 原生隔离更彻底 |
| **JS 隔离** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Wujie 基于 iframe，Qiankun 需要沙箱 |
| **性能** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Wujie 无沙箱损耗，性能更优 |
| **预加载** | ✅ | ✅ | 两者都支持 |
| **保活** | ✅ | ✅ | 两者都支持 |
| **通信** | ✅ | ✅ | 两者都支持多种通信方式 |
| **路由同步** | ✅ | ✅ | 两者都支持 |
| **子应用嵌套** | ✅ | ✅ | 两者都支持 |
| **TypeScript** | ✅ | ✅ | 两者都有完整类型定义 |
| **Vue 支持** | ✅ | ✅ | 两者都完美支持 |
| **React 支持** | ✅ | ✅ | 两者都完美支持 |
| **Angular 支持** | ✅ | ✅ | 两者都支持 |
| **零改造接入** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | Wujie 支持零改造，Qiankun 需要少量改造 |
| **文档完善度** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Qiankun 文档更完善 |
| **社区活跃度** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Qiankun 社区更活跃 |

---

## 性能对比

### 性能指标

| 指标 | Wujie | Qiankun | 说明 |
|------|-------|---------|------|
| **首屏加载** | 快 | 中等 | Wujie 无沙箱初始化时间 |
| **运行时性能** | 优秀 | 良好 | Wujie 无沙箱代理损耗 |
| **内存占用** | 中等 | 中等 | iframe 会占用额外内存 |
| **沙箱损耗** | 无 | 有 | Qiankun 的 Proxy 沙箱有性能损耗 |
| **打包体积** | 小 (~10KB) | 中等 (~20KB) | Wujie 更轻量 |

### 性能测试场景

#### 场景 1：多子应用同时运行

```
Wujie：
- 每个子应用在独立 iframe 中运行
- 无沙箱性能损耗
- 内存占用：主应用 + N × iframe

Qiankun：
- 所有子应用在主应用上下文中运行
- 需要 Proxy 沙箱保护
- 内存占用：主应用 + 沙箱开销
```

#### 场景 2：频繁切换子应用

```
Wujie：
- 支持保活，切换无重新加载
- iframe 切换开销小

Qiankun：
- 支持保活，切换需要重新挂载
- 沙箱重新初始化有开销
```

---

## 实际使用场景

### 适合使用 Wujie 的场景

1. **需要完全隔离的项目**
   - 多个团队独立开发，需要严格的代码隔离
   - 第三方应用集成，需要安全隔离
   - 样式冲突严重的遗留系统改造

2. **性能要求高的项目**
   - 对运行时性能要求严格
   - 需要同时运行多个子应用
   - 频繁切换子应用的场景

3. **快速接入遗留系统**
   - 需要零改造接入现有系统
   - 不想修改子应用代码
   - 需要快速集成多个独立系统

4. **需要原生隔离的场景**
   - 需要 iframe 的原生安全隔离
   - 需要完全独立的运行环境

### 适合使用 Qiankun 的场景

1. **大型企业级应用**
   - 需要成熟的解决方案
   - 需要完善的文档和社区支持
   - 需要经过生产验证的框架

2. **需要灵活配置的项目**
   - 需要自定义沙箱策略
   - 需要灵活的样式隔离方案
   - 需要复杂的通信机制

3. **团队技术栈统一**
   - 团队熟悉 Ant Design 生态
   - 需要与 umi 等框架深度集成
   - 需要统一的开发规范

4. **需要丰富实践案例**
   - 需要参考大量最佳实践
   - 需要社区问题解决方案
   - 需要完善的工具链支持

---

## 快速上手

### Wujie 快速开始

#### 1. 安装

```bash
npm install wujie
# 或
yarn add wujie
```

#### 2. 主应用配置

```javascript
// main.js
import { startApp } from 'wujie';

// 启动子应用
startApp({
  name: 'vue3-app',
  url: 'http://localhost:5173',
  el: '#subapp-container',
  sync: true, // 路由同步
  alive: true, // 保活
  props: {
    // 传递给子应用的数据
    message: 'Hello from main app'
  }
});
```

#### 3. 子应用配置（可选）

```javascript
// 子应用 main.js
if (window.__WUJIE__) {
  // 在 Wujie 环境中运行
  window.__WUJIE__.mount();
} else {
  // 独立运行
  app.mount('#app');
}
```

#### 4. Vue 组件方式使用

```vue
<template>
  <WujieVue
    name="vue3-app"
    url="http://localhost:5173"
    :sync="true"
    :alive="true"
    :props="{ message: 'Hello' }"
  />
</template>

<script setup>
import { WujieVue } from 'wujie-vue3';
</script>
```

---

### Qiankun 快速开始

#### 1. 安装

```bash
npm install qiankun
# 或
yarn add qiankun
```

#### 2. 主应用配置

```javascript
// main.js
import { registerMicroApps, start } from 'qiankun';

// 注册子应用
registerMicroApps([
  {
    name: 'vue3-app',
    entry: 'http://localhost:5173',
    container: '#subapp-container',
    activeRule: '/vue3',
    props: {
      message: 'Hello from main app'
    }
  }
]);

// 启动 qiankun
start({
  sandbox: {
    strictStyleIsolation: true, // 严格样式隔离
    experimentalStyleIsolation: true
  }
});
```

#### 3. 子应用配置

```javascript
// 子应用 main.js
import { render } from './main';

let app = null;

// 导出生命周期函数
export async function bootstrap() {
  console.log('子应用启动');
}

export async function mount(props) {
  app = render(props);
}

export async function unmount() {
  if (app) {
    app.unmount();
    app = null;
  }
}

// 独立运行
if (!window.__POWERED_BY_QIANKUN__) {
  render();
}
```

#### 4. 子应用 webpack 配置

```javascript
// vue.config.js 或 webpack.config.js
module.exports = {
  configureWebpack: {
    output: {
      library: `${name}-[name]`,
      libraryTarget: 'umd',
      jsonpFunction: `webpackJsonp_${name}`
    }
  },
  devServer: {
    headers: {
      'Access-Control-Allow-Origin': '*'
    }
  }
};
```

---

## 最佳实践

### Wujie 最佳实践

#### 1. 通信最佳实践

```javascript
// 主应用发送数据
import { bus } from 'wujie';

bus.$emit('event-name', { data: 'value' });

// 子应用接收数据
window.$wujie?.bus.$on('event-name', (data) => {
  console.log(data);
});
```

#### 2. 预加载配置

```javascript
import { preloadApp } from 'wujie';

// 预加载子应用
preloadApp({
  name: 'vue3-app',
  url: 'http://localhost:5173'
});
```

#### 3. 保活配置

```javascript
startApp({
  name: 'vue3-app',
  url: 'http://localhost:5173',
  alive: true, // 开启保活
  // 保活时，子应用不会卸载，切换回来时直接显示
});
```

#### 4. 路由同步

```javascript
startApp({
  name: 'vue3-app',
  url: 'http://localhost:5173',
  sync: true, // 开启路由同步
  // 主应用路由变化时，子应用路由也会同步
});
```

---

### Qiankun 最佳实践

#### 1. 全局状态管理

```javascript
import { initGlobalState } from 'qiankun';

// 初始化全局状态
const actions = initGlobalState({
  user: { name: 'John' }
});

// 主应用监听状态变化
actions.onGlobalStateChange((state, prev) => {
  console.log('状态变化', state, prev);
});

// 子应用获取全局状态
export async function mount(props) {
  props.onGlobalStateChange((state, prev) => {
    console.log('子应用收到状态变化', state);
  });
  
  props.setGlobalState({ user: { name: 'Jane' } });
}
```

#### 2. 样式隔离配置

```javascript
start({
  sandbox: {
    // 严格样式隔离（Shadow DOM）
    strictStyleIsolation: true,
    // 实验性样式隔离（CSS Scope）
    experimentalStyleIsolation: true
  }
});
```

#### 3. 预加载配置

```javascript
import { prefetchApps } from 'qiankun';

// 预加载子应用
prefetchApps([
  {
    name: 'vue3-app',
    entry: 'http://localhost:5173'
  }
]);
```

#### 4. 错误处理

```javascript
import { addGlobalUncaughtErrorHandler } from 'qiankun';

// 全局错误处理
addGlobalUncaughtErrorHandler((event) => {
  console.error('子应用错误', event);
  // 上报错误
});
```

---

## 总结与建议

### 选择建议

#### 选择 Wujie 的情况

✅ **推荐使用 Wujie** 如果：
- 需要完全的原生隔离（样式、JS、DOM）
- 对性能要求高，需要无沙箱损耗
- 需要零改造接入遗留系统
- 需要快速集成多个独立系统
- 项目对体积敏感，需要轻量级方案

#### 选择 Qiankun 的情况

✅ **推荐使用 Qiankun** 如果：
- 需要成熟的、经过生产验证的解决方案
- 需要完善的文档和社区支持
- 团队熟悉 Ant Design 生态
- 需要灵活的配置和自定义能力
- 需要丰富的实践案例和最佳实践

### 综合对比总结

| 维度 | Wujie | Qiankun | 推荐 |
|------|-------|---------|------|
| **隔离性** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Wujie |
| **性能** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | Wujie |
| **易用性** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Qiankun |
| **生态** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Qiankun |
| **稳定性** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Qiankun |
| **灵活性** | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | Qiankun |

### 最终建议

1. **新项目**：如果对隔离和性能要求高，优先考虑 **Wujie**
2. **企业级项目**：如果需要成熟稳定的方案，优先考虑 **Qiankun**
3. **遗留系统改造**：如果需要零改造接入，优先考虑 **Wujie**
4. **团队技术栈**：如果团队熟悉 Ant Design 生态，优先考虑 **Qiankun**

### 混合使用

在实际项目中，也可以考虑混合使用：
- 主框架使用 **Qiankun**（稳定可靠）
- 需要完全隔离的子应用使用 **Wujie**（性能优先）

---

## 参考资料

- [Wujie 官方文档](https://wujie-micro.github.io/doc/)
- [Wujie GitHub](https://github.com/Tencent/wujie)
- [Qiankun 官方文档](https://qiankun.umijs.org/)
- [Qiankun GitHub](https://github.com/umijs/qiankun)
- [single-spa 文档](https://single-spa.js.org/)

---

**最后更新**：2024年  
**文档维护**：建议根据框架版本更新及时调整内容
