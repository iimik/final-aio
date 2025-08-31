package org.ifinalframework.plugins.aio.util

import com.google.common.base.CaseFormat

/**
 * NameUtils
 *
 * @author iimik
 */
object CaseFormatUtils {

    fun upperCamel2LowerUnderscore(text: String): String{
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, text)
    }

    fun lowerCamel2LowerUnderscore(text: String): String{
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, text)
    }
}