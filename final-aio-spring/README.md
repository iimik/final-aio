# Spring Feign Navigator

**为 Spring Controller 与 Feign Client 提供双向跳转、Find Usages 与 Line Marker 支持**  
**Bidirectional navigation, Find Usages, and Line Markers for Spring Controllers and Feign Clients**

支持 **Java & Kotlin** · 零配置  
Supports **Java & Kotlin** · Zero configuration

---

## 🚀 插件简介 | Overview

**Spring Feign Navigator** 为 IntelliJ IDEA 增强了  
**Spring MVC Controller** 与 **Feign Client** 之间的关系感知能力。

插件提供以下核心功能：

- 🔁 **双向跳转（Controller ⇄ Feign）**
- 🔍 **Find Usages 集成**
- 📍 **Line Marker（行标记 / Gutter 图标）**

所有功能均基于 **静态代码分析**，无需运行项目，也无需任何额外配置。

---

**Spring Feign Navigator** enhances IntelliJ IDEA with deep awareness of the relationship between  
**Spring MVC Controllers** and **Feign Clients**.

It provides:

- 🔁 **Bidirectional navigation (Controller ⇄ Feign)**
- 🔍 **Find Usages integration**
- 📍 **Line Markers (Gutter icons)**

All features are based on **static code analysis** — no runtime, no configuration required.

---

## 🔁 双向跳转 | Bidirectional Navigation

支持在 Controller 与 Feign Client 之间快速跳转：

- **Ctrl + Click / ⌘ + Click**
- **Ctrl + B / ⌘ + B（跳转到声明）**

支持的跳转方向：

- 从 **Controller 方法** → 对应的 **Feign Client 方法**
- 从 **Feign Client 方法** → 实际的 **Controller 实现**

当存在多个匹配目标时，IDEA 会弹出选择窗口。

---

Navigate seamlessly between Controllers and Feign Clients using:

- **Ctrl + Click / ⌘ + Click**
- **Ctrl + B / ⌘ + B (Go to Declaration)**

Supported directions:

- **Controller method** → corresponding **Feign client method**
- **Feign client method** → actual **Controller implementation**

If multiple targets are found, IntelliJ IDEA shows a chooser popup.

---

## 📍 Line Marker（行标记） | Line Markers

插件会在代码左侧 **Gutter 区域** 显示行标记，用于直观提示  
Controller 与 Feign 之间的关联关系。

### Line Marker 表示的含义

- **Controller 方法上**  
  表示该接口被一个或多个 Feign Client 调用
- **Feign 方法上**  
  表示该方法对应的 Controller 实现

### 可执行操作

- 点击行标记可直接跳转
- 悬停可查看关系说明提示

> Line Marker 作为**辅助可视化提示**，  
> 核心导航方式仍然是快捷键跳转和 Find Usages。

---

The plugin adds **gutter icons** to visually indicate Controller ↔ Feign relationships.

### What Line Markers Show

- On **Controller methods**  
  Indicates one or more Feign clients calling this endpoint
- On **Feign methods**  
  Indicates the corresponding Controller implementation

### What You Can Do

- Click the gutter icon to navigate
- Hover to see a relationship tooltip

> Line Markers are **visual hints**, not the primary navigation mechanism.  
> Keyboard navigation and Find Usages remain the core workflows.

---

## 🔍 Find Usages 集成 | Find Usages Integration

Controller 与 Feign 的关联关系已完整接入 **Find Usages** 体系。

- 在 **Controller 方法** 上执行 **Find Usages（Alt + F7 / ⌥ + F7）**  
  → 查看所有关联的 Feign Client 方法
- 在 **Feign 方法** 上执行 **Find Usages（Alt + F7 / ⌥ + F7）**  
  → 定位实际的 Controller 实现

支持跨模块、跨服务、跨语言。

---

The Controller ↔ Feign relationship is fully integrated into **Find Usages**.

- Run **Find Usages (Alt + F7 / ⌥ + F7)** on a **Controller method**  
  → see all related Feign client methods
- Run **Find Usages (Alt + F7 / ⌥ + F7)** on a **Feign method**  
  → locate the actual Controller implementation

Works across modules, services, and languages.

---

## ☕ Java & 🧩 Kotlin 支持 | Java & Kotlin Support

- 同时支持 **Java 与 Kotlin**
- 适用于混合项目：
    - Java Controller ↔ Kotlin Feign
    - Kotlin Controller ↔ Java Feign
- 基于 **PSI / UAST** 实现，语言无关

---

- Full support for **Java and Kotlin**
- Works in mixed projects:
    - Java Controller ↔ Kotlin Feign
    - Kotlin Controller ↔ Java Feign
- Built on **PSI / UAST** for language‑agnostic analysis

---

## 🎯 接口匹配规则 | Endpoint Matching

插件通过以下信息进行接口匹配：

- 服务名（`@FeignClient`）
- 请求路径（类级 + 方法级）
- HTTP 方法（GET / POST / PUT / DELETE / PATCH）

以确保匹配结果**准确可靠**，避免误匹配。

---

Endpoints are matched using:

- Service name (`@FeignClient`)
- Request path (class‑level + method‑level)
- HTTP method (GET / POST / PUT / DELETE / PATCH)

This ensures **accurate matching** and avoids false positives.

---

## ✅ 支持的注解 | Supported Annotations

### Spring Controller

- `@RestController`, `@Controller`
- `@RequestMapping`
- `@GetMapping`, `@PostMapping`
- `@PutMapping`, `@DeleteMapping`, `@PatchMapping`

### Feign Client

- `@FeignClient`
- `@RequestMapping`
- `@GetMapping`, `@PostMapping`
- `@PutMapping`, `@DeleteMapping`, `@PatchMapping`

---

## ⌨️ 快捷键 | Keyboard Shortcuts

|            功能             | Windows / Linux |    macOS    |
|:-------------------------:|:---------------:|:-----------:|
| 跳转到声明 / Go to Declaration |   `Ctrl + B`    |   `⌘ + B`   |
| 点击跳转 / Click to Navigate  | `Ctrl + Click`  | `⌘ + Click` |
|    查找用法 / Find Usages     |   `Alt + F7`    |  `⌥ + F7`   |

> 所有快捷键均遵循 IntelliJ IDEA 默认 Keymap，并支持用户自定义。  
> All shortcuts follow IntelliJ IDEA default keymaps and respect user‑customized settings.

---

## ⚙️ 使用方式 | How to Use

无需任何配置。

1. 打开 Controller 或 Feign Client
2. 将光标放在方法名上
3. 使用以下方式之一：
    - **Ctrl + Click / ⌘ + Click**
    - **Ctrl + B / ⌘ + B**
    - **Find Usages（Alt + F7 / ⌥ + F7）**
    - 点击左侧 **Line Marker**

---

No configuration required.

1. Open a Controller or Feign Client
2. Place the caret on a method name
3. Use one of the following:
    - **Ctrl + Click / ⌘ + Click**
    - **Ctrl + B / ⌘ + B**
    - **Find Usages (Alt + F7 / ⌥ + F7)**
    - Click the **Line Marker** in the gutter

---

## 🧩 平台支持 | Platform Support

- IntelliJ IDEA Ultimate
- IntelliJ IDEA Community（需包含 Java 支持）

---

- IntelliJ IDEA Ultimate
- IntelliJ IDEA Community (Java support required)

---

## ⚠️ 当前限制 | Current Limitations

- 不支持运行期动态路径解析
- 不支持自定义 Feign Contract

---

- Runtime‑dynamic paths are not resolved
- Custom Feign contracts are not supported

---

## 🎯 适用人群 | Who Is This For?

- Spring Boot / Spring Cloud 微服务项目
- Feign 使用较多的中大型代码库
- Java / Kotlin 混合工程
- 希望在 IDE 内清晰掌握接口关系的开发者

---

- Spring Boot / Spring Cloud microservice projects
- Codebases with extensive Feign usage
- Java & Kotlin mixed‑language projects
- Developers who want **clear API relationships inside the IDE**

---

## ✅ 为什么选择 Spring Feign Navigator？ | Why Spring Feign Navigator?

- 原生 IntelliJ IDEA 导航体验
- 无侵入、零配置
- 无运行时开销
- 面向真实微服务项目设计

---

- Native IntelliJ IDEA navigation experience
- Non‑intrusive, zero configuration
- No runtime overhead
- Designed for real‑world microservice codebases

---
