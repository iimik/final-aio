package org.ifinalframework.plugins.aio.mybatis.xml.generator.criterion

import com.intellij.openapi.components.service
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.xml.model.Criterion
import org.ifinalframework.plugins.aio.mybatis.xml.model.SimpleCriterion
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

private const val TEST_COMPLETION_PLACE_HOLDER = $$"${TARGET}"

/**
 * CommonCriterionGenerator
 *
 * ```xml
 *  <if test="{test}">
 *      {andOr} {column} {criterionType} #{param}
 *  </if>
 * ```
 * 
 * @author iimik
 * @see BetweenCriterionGenerator
 * @see InCriterionGenerator
 */
class CommonCriterionGenerator : AbstractCriterionGenerator(), CriterionGenerator<SimpleCriterion> {
    companion object {
        val INSTANCE = CommonCriterionGenerator() as CriterionGenerator<Criterion>
    }

    override fun generate(criterion: SimpleCriterion): String {
        val param = criterion.param
        val name = getName(param)
        val column = generateColumn(param)

        val project = param.project
        val myBatisProperties = project.service<MyBatisProperties>()
        val testCompletion = myBatisProperties.testCompletion
        val sqlParam = Stream.of(criterion.prefix, name).filter { Objects.nonNull(it) }.collect(Collectors.joining("."))
        val test = testCompletion.defaultType.replace(TEST_COMPLETION_PLACE_HOLDER, sqlParam)

        return if (criterion.required) {
            // 不需要<if test>
            """
                    ${criterion.andOr} $column ${criterion.type.operation} #{$sqlParam}
            """.trimIndent()
        } else {
            """
            <if test="$test">
                ${criterion.andOr} $column ${criterion.type.operation} #{$sqlParam}
            </if>
        """.trimIndent()
        }
    }
}