package org.ifinalframework.plugins.aio.mybatis.xml.model

/**
 * CriterionType
 *
 * @author iimik
 */
enum class CriterionType(
    val operation: String
) {
    EQUAL("="),
    NOT_EQUAL("!="),
    IN("IN"),
    NOT_INT("NOT IN"),
    ;


}