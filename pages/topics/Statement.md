# Statement

## Insert

快速生成`insert`语句，支持单个和批量，生成的SQL模板如下：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="{MAPPER_CLASS_NAME}">
    <sql id="table">
        {MAPPER对应的数据表}
    </sql>
    <!-- 单个INSERT -->
    <insert id="singleInsert" useGeneratedKeys="true" keyProperty="id">
        INSERT INTO
        <include refid="table"/>
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="null != property1">
                `column1`,
            </if>
            <if test="null != property2">
                `column2`,
            </if>
            ...
        </trim>
        VALUES
        <trim prefix="(" suffix=")" suffixOverrides=",">
            <if test="null != property1">
                #{property1},
            </if>
            <if test="null != property2">
                #{property2, typeHandler=自定义typeHandler},
            </if>
        </trim>
    </insert>
    <!-- 批量Insert -->
    <insert id="batchInsert" useGeneratedKeys="true" keyProperty="list.id">
        INSERT INTO
        <include refid="table"/>
        <trim prefix="(" suffix=")" suffixOverrides=",">
            `column1`,
            `column2`,
            ...
        </trim>
        VALUES
        <foreach collection="list" item="item" separator=",">
            <trim prefix="(" suffix=")" suffixOverrides=",">
                #{item.property1},
                #{item.property2, typeHandler=自定义typeHandler},
                ...
            </trim>
        </foreach>
    </insert>
</mapper>
```