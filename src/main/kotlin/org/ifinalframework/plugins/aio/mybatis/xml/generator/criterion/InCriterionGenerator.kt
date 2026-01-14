package org.ifinalframework.plugins.aio.mybatis.xml.generator.criterion

import com.intellij.openapi.components.service
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.xml.model.SimpleCriterion
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

private const val TEST_COMPLETION_PLACE_HOLDER = $$"${TARGET}"

/**
 * InCriterionGenerator
 * 
 * @author iimik
 * @see BetweenCriterionGenerator
 * @see CommonCriterionGenerator
 */
class InCriterionGenerator : AbstractCriterionGenerator(), CriterionGenerator<SimpleCriterion> {

    companion object {
        val INSTANCE = InCriterionGenerator()
    }

    override fun generate(criterion: SimpleCriterion): String {
        val param = criterion.param
        val name = getName(param)
        val column = generateColumn(param)
        val collection = Stream.of<String>(criterion.prefix, name).filter { Objects.nonNull(it) }.collect(Collectors.joining("."))

        val project = param.project
        val myBatisProperties = project.service<MyBatisProperties>()
        val testCompletion = myBatisProperties.testCompletion
        val sqlParam = Stream.of(criterion.prefix, name).filter { Objects.nonNull(it) }.collect(Collectors.joining("."))
        val test = testCompletion.collectionType.replace(TEST_COMPLETION_PLACE_HOLDER, sqlParam)

        return if (criterion.required) {
            // 不需要<if test>
            """
                    ${criterion.andOr} $column ${criterion.type.operation}
                <foreach collection="$collection" item="item" open="(" close=")" separator=",">
                    #{item}
                </foreach>
            """.trimIndent()
        } else {
            """
            <if test="$test">
                ${criterion.andOr} $column ${criterion.type.operation}
                <foreach collection="$collection" item="item" open="(" close=")" separator=",">
                    #{item}
                </foreach>
            </if>
        """.trimIndent()
        }
    }

}