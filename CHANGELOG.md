<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Final AIO 变更日志

## [Unreleased]

## [0.0.15] - 2025-04-30

### 新增

- Api
  - Markdown支持模板，可在`Tools-Final AIO-Api-Markdown`中进行配置

## [0.0.14] - 2025-04-27

### 优化

- Spring
  - Spring MVC支持自定义`@ResultBody`注解。

### 修复

- 🐛修复了一些问题。

## [0.0.13] - 2025-04-15

### 优化

- MyBatis
  - 🔥Mapper xml 自动补全`test`属性，支持区间属性。

## [0.0.12] - 2025-04-08

### 新增

- MyBatis
  - 🔥Mapper xml 自动补全支持`test`属性

## [0.0.11] - 2025-04-07

### 新增

- MyBatis
  - Mapper xml 自动补全支持 `include.refid` 属性。
  - Mapper xml 生成支持Table Sql Fragment。

## [0.0.10] - 2025-03-27

### 新增

- MyBatis
  - 🔥检查Mapper是否有对应的XML文件并提供快速生成Mapper.xml的修复方式。
  - 🔥检查Mapper中的方法是否有定义statement并提供快速生成Statement的修复方式。

### 修复

- MyBatis
  - 🐛修复Statement行标记找不到方法时也显示问题

## [0.0.9] - 2025-03-07

### 优化

- 优化图标大小 (#35)

## [0.0.8] - 2025-03-07

### 优化

- 优化了部分功能性能

### 修复

- 🐛修复了一些问题（ProcessCanceledException）

## [0.0.7] - 2025-03-05

### 新增

- MyBatis
  - xml支持`resultMap`子标签中的`property`属性

### 修复

- Api
  - 🐛修复Markdown文档预览不能识别图片。(#30)

## [0.0.6] - 2025-03-03

### 新增

- MyBatis
  - 🔥xml中支持`ResultMap`定义和引用之间的快速跳转。(#31)
- Api
  - Markdown文档 (#30)

## [0.0.5] - 2025-02-28

### 新增

- Spring
  - 🔥添加Spring Cloud Feign 行标记，在Controller和FeignClient之间快速跳转。(#28)

### 修复

- Mybatis
  - 🐛修复Mapper Xml行标记不支持`<select>`标签问题

## [0.0.4] - 2025-02-23

### 新增

- Mybatis
  - 🔥添加Mybatis行标记，点击可在java(kotlin)和xml文件中互相跳转。(#24)

## [0.0.3] - 2025-02-18

### Feature

- Refactor ApiLineMarkerProvider.

### Fixed

- fix issue parse for markdown file. (#18)

## [0.0.2] - 2025-02-06

### 新增

- Api 管理
  - 🔥添加Api方法Markdown行标记，并可快速在编辑器中打开进行编辑。(#14) 
  - 🔥添加Api方法View行标记，可快速在浏览器中打开对应URL。(#16)
- Issue 管理
  - 🔥Issue行标记支持Markdown，格式`(#issue)` (#18)

## [0.0.1] - 2025-01-27

### 新增

- Issue 管理
  - 🔥添加Issue行标记，并可在浏览器中快速打开，支持Git Issue和Jira。(#10)

[Unreleased]: https://github.com/iimik/final-aio/compare/v0.0.15...HEAD
[0.0.15]: https://github.com/iimik/final-aio/compare/v0.0.14...v0.0.15
[0.0.14]: https://github.com/iimik/final-aio/compare/v0.0.13...v0.0.14
[0.0.13]: https://github.com/iimik/final-aio/compare/v0.0.12...v0.0.13
[0.0.12]: https://github.com/iimik/final-aio/compare/v0.0.11...v0.0.12
[0.0.11]: https://github.com/iimik/final-aio/compare/v0.0.10...v0.0.11
[0.0.10]: https://github.com/iimik/final-aio/compare/v0.0.9...v0.0.10
[0.0.9]: https://github.com/iimik/final-aio/compare/v0.0.8...v0.0.9
[0.0.8]: https://github.com/iimik/final-aio/compare/v0.0.7...v0.0.8
[0.0.7]: https://github.com/iimik/final-aio/compare/v0.0.6...v0.0.7
[0.0.6]: https://github.com/iimik/final-aio/compare/v0.0.5...v0.0.6
[0.0.5]: https://github.com/iimik/final-aio/compare/v0.0.4...v0.0.5
[0.0.4]: https://github.com/iimik/final-aio/compare/v0.0.3...v0.0.4
[0.0.3]: https://github.com/iimik/final-aio/compare/v0.0.2...v0.0.3
[0.0.2]: https://github.com/iimik/final-aio/compare/v0.0.1...v0.0.2
[0.0.1]: https://github.com/iimik/final-aio/commits/v0.0.1
