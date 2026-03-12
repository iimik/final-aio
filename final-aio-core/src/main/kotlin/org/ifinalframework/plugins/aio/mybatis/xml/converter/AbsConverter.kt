package org.ifinalframework.plugins.aio.mybatis.xml.converter

import com.intellij.util.xml.ConvertContext
import com.intellij.util.xml.ResolvingConverter


/**
 * Abs
 *
 * @author iimik
 * @since 0.0.6
 **/
abstract class AbsConverter<T : Any> : ResolvingConverter<T>() {
    override fun toString(t: T?, context: ConvertContext): String? {
        return "HAHA"
    }

    override fun fromString(s: String?, context: ConvertContext): T? {
        return null
    }

    override fun getVariants(context: ConvertContext): MutableCollection<out T> {
        return mutableSetOf()
    }
}