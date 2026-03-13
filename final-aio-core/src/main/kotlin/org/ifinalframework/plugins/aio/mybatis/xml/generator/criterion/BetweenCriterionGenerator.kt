package org.ifinalframework.plugins.aio.mybatis.xml.generator.criterion

import com.intellij.openapi.components.service
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties
import org.ifinalframework.plugins.aio.mybatis.xml.model.BetweenCriterion
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

private const val TEST_COMPLETION_START_PLACE_HOLDER = $$"${START_TARGET}"
private const val TEST_COMPLETION_END_PLACE_HOLDER = $$"${END_TARGET}"

/**
 *
 * 生成BETWEEN SQL
 *
 * ```xml
 * <if test="null != start and null != end">
 *      {column} {andOr} [NOT] BETWEEN {start} AND {end}
 * </if>
 * ```
 *
 * @author iimik
 * @see InCriterionGenerator
 * @see CommonCriterionGenerator
 */
class BetweenCriterionGenerator : AbstractCriterionGenerator(), CriterionGenerator<BetweenCriterion> {

    companion object {
        val INSTANCE = BetweenCriterionGenerator()
    }

    override fun generate(criterion: BetweenCriterion): String {
        val start = criterion.start
        val end = criterion.end

        val startName = getName(start)
        val endName = getName(end)

        val prefix = criterion.prefix

        val startParam = Stream.of(prefix, startName).filter { Objects.nonNull(it) }.collect(Collectors.joining("."))
        val endParam = Stream.of(prefix, endName).filter { Objects.nonNull(it) }.collect(Collectors.joining("."))

        val project = start.project
        val myBatisProperties = project.service<MyBatisProperties>()
        val testCompletion = myBatisProperties.testCompletion

        val test = testCompletion.betweenType.replace(TEST_COMPLETION_START_PLACE_HOLDER, startParam)
            .replace(TEST_COMPLETION_END_PLACE_HOLDER, endParam)

        val column = generateColumn(startName.substringAfter("start"))

        return if (criterion.required) {
            // 不需要<if test>
            "${criterion.andOr} $column ${criterion.type.operation} #{$startName} AND #{$endName}"
        } else {
            """
                <if test="$test">
                    ${criterion.andOr} $column ${criterion.type.operation} #{$startName} AND #{$endName}
                </if>
            """.trimIndent()
        }

    }
}