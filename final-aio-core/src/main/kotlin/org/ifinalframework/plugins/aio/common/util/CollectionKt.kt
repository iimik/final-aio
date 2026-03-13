package org.ifinalframework.plugins.aio.common.util


/**
 * CollectionKt
 *
 * @author iimik
 * @since 0.0.5
 **/
object CollectionKt {
    fun <T:Any> Collection<T>.containsAny(collection: Collection<T>): Boolean {
        return collection.any { this.contains(it) }
    }
}