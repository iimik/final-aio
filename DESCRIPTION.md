Final AIO(All In One)旨在打造一个为开发者提供快速开发的一站式插件。

## Issue 管理

- 🔥添加Issue行标记，点击行标记可在浏览器中打开对应的URL，支持Git Issue和Jira。[详情](https://github.com/iimik/final-aio/blob/main/docs/issue.md)
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