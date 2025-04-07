# English

## Issue Management

- 🔥Add issue line marker, open it in browser when click it. support git and jira. [See Detail](/docs/issue.md)

## Api Management

- 🔥Add api method markdown line marker, open the markdown file when click it, auto create when file is not exists.
- 🔥Add api method view line marker, open api doc in browser when click it.


## MyBatis

- 🔥Add MyBatis line marker, quick skip between java and xml.
- 🔥Add quick skip in xml define and reference.

## Spring

- 🔥Add Spring line marker, quick skip between in controller and feign client.

---

## 中文 

## Issue 管理

- 🔥添加Issue行标记，点击行标记可在浏览器中打开对应的URL，支持Git Issue和Jira。[详情](/docs/issue.md)
    - 支持以下格式：
        ```java
        /**
         * 文档注释
         * @issue 编号 [描述]
         * @jira 编号 [描述]
         */
        // 行注释
        // #编号 [描述]
        // @issue 编号 [描述]
        // @jira 编号 [描述]
        ```
    - 点击行标记可在浏览器中打开对应的URL，其中Jira需要在配置文件`.final.yml`中添加以下配置：
        ```yaml
        final:
          issue:
            jira:
              # Jira 服务地址
              server-url: https://iimik.atlassian.net
              # Jira 项目编码
              project-code: AIO
        ```

## Api 管理

- 🔥添加Api Method Markdown行标记，点击行标记可快速打开Markdown文件，不存在时自动创建。
- 🔥添加Api Method View行标记，点击行标记可快速在浏览器中打开对应在线文档。 

## MyBatis

- 🔥添加 MyBatis 行标记，在Java/Kotlin和Xml文件中快速跳转。
- 🔥Mapper xml 文件中定义和引用之间跳转。
- 🔥快速生成Mapper xml和Statement。

## Spring

- 🔥添加 Spring 行标记，在Controller和Feign Client之间快速跳转。