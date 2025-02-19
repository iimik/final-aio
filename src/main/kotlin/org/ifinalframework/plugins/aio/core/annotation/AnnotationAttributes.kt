package org.ifinalframework.plugins.aio.core.annotation;


/**
 * AnnotationAttributes
 *
 * @author iimik
 * @since 0.0.2
 **/
class AnnotationAttributes : LinkedHashMap<String, Any?> {
    constructor()
    constructor(map: Map<String, Any?>) {
        putAll(map)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getList(attributeName: String): List<T>? {
        val value = get(attributeName) ?: return null
        return if (value is List<*>) {
            value as List<T>
        } else listOf(value as T);
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): AnnotationAttributes {
            return if (map is AnnotationAttributes) {
                map
            } else AnnotationAttributes(map)
        }
    }
}