# Issue

## Issue行标记

### Java & Kotlin

给特定的注释添加Issue行标记。支持以下格式的注释：

* 块注释
```
/**
 * Issue 服务
 *
 * @jira 1
 * @issue 10
 * @author iimik
 * @since 0.0.1
 **/
```

* 行注释
```
// @issue 10 docTag issue
// @jira 1 docTag issue
// #10 line issue
```

![Issue Liner Marker](./images/issue-line-marker.png)

### Markdown

在Markdown文件中，特定的文本格式`(#18)`也会被标记为Issue。

## 打开 Issue

### 打开 Git Issue

![Open Git Issue](images/Open-Git-Issue.gif)

### 打开 Jira

![Open Jira Issue](images/Open-Jira-Issue.gif)

在使用Jira管理项目时，需要`Setting`-`Tools`-`Final AIO`-`Issue`添加【服务地址】和【项目编码】配置：

![Issue Jira Config](./images/issue-jira-config.png)


