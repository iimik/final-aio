package org.ifinalframework.plugins.aio.mybatis.xml.converter

import com.intellij.openapi.components.service
import com.intellij.psi.PsiEnumConstant
import com.intellij.util.xml.ConvertContext
import org.ifinalframework.plugins.aio.mybatis.service.MybatisService

/**
 * 将Mapper.xml中的jdbcType转换到MyBatis JdbcType定义。
 *
 * ```xml
 * <include refid={sqlid}/>
 * ```
 * @author iimik
 * @see [JdbcType](https://github.com/mybatis/mybatis-3/blob/master/src/main/java/org/apache/ibatis/type/JdbcType.java)
 */
class JdbcTypeConverter : AbsConverter<PsiEnumConstant>() {

    override fun fromString(s: String?, context: ConvertContext): PsiEnumConstant? {
        if (s == null) return null

        return context.project.service<MybatisService>().getJdbcType(s.trim())
    }

}