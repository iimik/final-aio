# 补全

提供MyBatis代码补全功能，如：



## Property

`<resultMap>`子标签`<id>`和`<result>`中`property`属性

![MyBatis completion property](mybatis-completion-property.png)

## JdbcType

`<resultMap>`子标签`<id>`和`<result>`中`jdbcType`属性

![MyBatis completion jdbcType](mybatis-completion-jdbcType.png)

## Test

`<if>`和`<when>`标签中的 `test` 属性

* 扩展
  * 时间字段且以 `start` 开始: `null != {startProperty} and null != {endProperty}`
  * 枚举字段：`null != {property} and {property} == @{enumClass}@{enumConstant}` (#112)

![MyBatis completion test](mybatis-completion-test.png)
