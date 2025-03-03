<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Final AIO 变更日志

## [Unreleased]

### 新增

- MyBatis
  - 🔥xml中支持`ResultMap`定义和引用之间的快速跳转。(#31)

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
