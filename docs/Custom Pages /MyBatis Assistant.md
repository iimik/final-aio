# 🔥MyBatis Assistant

MyBatis Assistant 可以帮助开发者快速编写MyBatis相关代码，并检测代码是否正确。

## 行标记

添加相关行标记，点击该标记可快速在`Java/Kotlin`和`Mapper`文件中快速跳转。

* `Mapper` 和 `<mapper namespace={Mapper}>`
* `Data Class`和`<resultMap type={Data Class}`
* `Method`和`<insert/update/delete/select id={method}`

![MyBatis Go To](https://plugins.jetbrains.com/files/26415/61653-page/62245d9e-6a38-4c2d-a0f1-9b3243c39e51)


## 检查

### Mapper

检查`Mapper`接口是否的定义`Mapper.xml`文件，并提供快速生成`Mapper.xml`文件的方式(`Command+Enter`)，支持在源代码(`SOURCE`)和资源(`RESOURCE`)目录生成。

![Generate Mapper](https://plugins.jetbrains.com/files/26415/61653-page/a9371578-faf0-43a3-bc2f-297be99679fc)

### Statement是否存在