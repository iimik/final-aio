# 🔥Issue Management

通过特定的注释格式，您可以得到一个Issue行标记，通过该标记，可以快速在浏览器中打开对应的Issue链接。

## Git Issue

当注释符合以下格式时，可快速在浏览器中打开Git Issue链接。支持Github和Gitlab（含私服）

* 文档注释
```
/**
 * @issue 11 [Git Issue 文档注释]
 **/ 
```
* 行注释
```
// @issue 11 [Git Issue 行注释]
// #11 [Git Issue 行注释]
```

> [描述]为可选。

![Open Git Issue](https://plugins.jetbrains.com/files/26415/61572-page/0374c278-8092-4501-b29f-b779b17bde6a)

## Jira

* 文档注释
```
/**
 * @jira 1 [Jira 文档注释]
 **/ 
```
* 行注释
```
// @jira 1 [Jira 行注释]
```

> [描述]为可选。

![Open Jira](https://plugins.jetbrains.com/files/26415/61572-page/d4a12a6a-b0ce-4b4d-8cf0-1167f0a9524c)

在使用Jira管理项目时，需要在配置文件中添加以下配置：

```yaml
final:
  issue:
    jira:
      # Jira 服务地址
      server-url: https://iimik.atlassian.net
      # Jira 项目编码
      project-code: AIO
```





