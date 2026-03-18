# Task Mention｜final-aio-tasks

> Final AIO Tasks Module  
> Official Task Plugin Enhanced Extension

---

## 📌 模块介绍 | Introduction

**final-aio-tasks** 是 **Final AIO** 插件体系中的任务增强模块，  
基于官方 Task 插件进行扩展，用于实现 **Task 与代码 / 文档的深度绑定**。

**final-aio-tasks** is an enhanced task module in the **Final AIO** ecosystem.  
It extends the official Task plugin and enables **deep integration between tasks and source code / documents**.

---

## ✨ 功能特性 | Features

### 🔗 Task Reference 自动识别 | Automatic Task Reference Detection

- 支持在源码注释与文档中直接引用 Task
- 自动解析 Task ID 并映射为对应的 Task URL
- 无需手动维护完整链接

- Supports referencing tasks directly in source code comments and documents
- Automatically resolves Task IDs and maps them to Task URLs
- No need to manually maintain full URLs

---

### 📍 Line Marker 支持 | Line Marker Support

- 在编辑器左侧显示 Task Line Marker
- 清晰标识当前代码或文档关联的 Task
- 点击即可执行 Task 跳转

- Displays Task Line Markers in the editor gutter
- Clearly indicates which code or document lines are linked to tasks
- Click to navigate directly to the task

---

### 🌐 一键打开 Task | One-Click Task Navigation

- 支持通过 Line Marker 或 Reference 点击
- 自动在浏览器中打开 Task 对应页面
- 减少 IDE 与任务系统之间的切换成本

- Supports clicking via Line Marker or Task Reference
- Automatically opens the corresponding task page in the browser
- Reduces context switching between IDE and task systems

---

### ⚙ 自定义 Reference | Custom Reference Rules

- 支持自定义 Task Reference 规则
- 可适配不同的 Task / Issue 管理系统
- 满足团队多样化任务标识规范

- Allows custom task reference formats
- Compatible with various task / issue management systems
- Adapts to different team conventions

---

### 🚀 快捷链支持 | Shortcut Link Support

- 提供快捷方式快速打开 Task
- 适用于高频 Task 操作场景
- 显著提升开发效率

- Provides shortcuts to quickly open tasks
- Designed for high-frequency task access
- Significantly improves developer productivity

---

### 🌍 多语言支持 | Multi-Language Support

Task Reference 与 Line Marker 在以下文件类型中生效：

Task references and line markers are supported in the following file types:

- ✅ Java
- ✅ Kotlin
- ✅ Markdown
- ✅ YAML

---

## 📝 使用示例 | Usage Examples

### Java / Kotlin

```java
// TASK-1234 修复用户登录失败问题
public void login() {
}
```

```kotlin
// TASK-5678 优化订单创建流程
fun createOrder() {
}
```

---

### Markdown

> **Markdown 中 Task 格式：`(#TASK-ID)`**  
> **Markdown Task format: `(#TASK-ID)`**

```md
## User Module

Fix login exception (#TASK-1234)
```

插件会自动识别 `(#TASK-1234)` 并显示 Task Line Marker。  
The plugin will automatically recognize `(#TASK-1234)` and display a task line marker.

---

### YAML

```yaml
# TASK-3456 灰度发布配置
feature:
  enabled: true
```

---

## 🎯 设计目标 | Design Goals

- ✅ Task 即上下文
- ✅ 代码即入口
- ✅ 注释即文档

- ✅ Tasks as context
- ✅ Code as entry point
- ✅ Comments as documentation

让 **Task / Issue / Requirement** 不再脱离代码而存在。  
Make tasks, issues, and requirements a natural part of the codebase.

---

## 📦 模块归属 | Module Scope

该模块属于 **Final AIO** 插件体系的一部分，  
可与 Spring 等模块协同使用。

This module is part of the **Final AIO** plugin ecosystem and works seamlessly with Spring and other modules.

---

## ✅ 总结 | Summary

**final-aio-tasks** 将 Task 能力真正融入 IDE：

**final-aio-tasks** brings task management directly into your IDE:

> 在注释中写 Task，在代码中看 Task，在 IDE 中打开 Task  
> Write tasks in comments, view them in code, and open them directly from the IDE
