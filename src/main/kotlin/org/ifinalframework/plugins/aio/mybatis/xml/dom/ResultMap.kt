package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiEnumConstant
import com.intellij.util.xml.*
import org.ifinalframework.plugins.aio.mybatis.xml.converter.*


/**
 * ResultMap
 *
 * ```xml
 * <resultMap id="resultMap" type="class" extends="resultMapId">
 *      <id column="id" jdbcType="INTEGER" property="id"/>
 *      <result column="type" jdbcType="VARCHAR" property="type"/>
 * </resultMap>
 * ```
 *
 * @author iimik
 * @since 0.0.4
 **/
interface ResultMap : IdDomElement {

    /**
     * @see [Select.getResultMap]
     */
    @Required
    @NameValue
    @Attribute("id")
    @Referencing(ResultMapIdReferenceConverter::class)
    override fun getId(): GenericAttributeValue<String>

    @Required
    @NameValue
    @Attribute("type")
    fun getType(): GenericAttributeValue<PsiClass>

    @NameValue
    @Attribute("extends")
    @Convert(ResultMapConverter::class)
    fun getExtends(): GenericAttributeValue<String>

    @SubTagsList("id", "result")
    fun getProperties(): List<Property>

    @SubTagList("id")
    fun getIds(): List<Id>

    @SubTagList("result")
    fun getResult(): List<Result>

    @SubTagList("id")
    fun addId(): Id
    @SubTagList("result")
    fun addResult(): Result


    /**
     * ```xml
     * <id column="id" jdbcType="INTEGER" property="id"/>
     * <result column="type" jdbcType="VARCHAR" property="type"/>
     * ```
     * @see [Id]
     * @see [Result]
     */
    interface Property : DomElement {
        @Required
        @NameValue
        @Attribute("column")
        fun getColumn(): GenericAttributeValue<String>

        @Required
        @NameValue
        @Attribute("property")
        @Convert(ResultMapPropertyConverter::class)
        fun getProperty(): GenericAttributeValue<String>

        @NameValue
        @Attribute("typeHandler")
        @Convert(TypeHandlerConverter::class)
        fun getTypeHandler(): GenericAttributeValue<PsiClass>

        @NameValue
        @Attribute("javaType")
        fun getJavaType(): GenericAttributeValue<PsiClass>

        @NameValue
        @Attribute("jdbcType")
        @Convert(JdbcTypeConverter::class)
        fun getJdbcType(): GenericAttributeValue<PsiEnumConstant>
    }

    /**
     * ```xml
     * <id column="id" jdbcType="INTEGER" property="id"/>
     * ```
     */
    interface Id : Property {}

    /**
     * ```xml
     * <result column="type" jdbcType="VARCHAR" property="type"/>
     * ```
     */
    interface Result : Property {}
}
