# Api 查看行标记

在Api方法（Spring RequestMapping）行上添加查看标记(![Markdown](https://raw.githubusercontent.com/iimik/final-aio/refs/heads/main/src/main/resources/assets/icons/apiView.svg))，点击可在浏览器器中打开API文档。

## Yapi

在打开Yapi文档，需要在配置文件中添加以下配置：

```yaml
final:
  api:
    yapi:
      server-url: 您的YAPI服务地址
      token: 访问API的TOKEN
```