package org.ifinalframework.plugins.aio.api.model


/**
 * ApiMarker
 *
 * @author iimik
 * @since 0.0.1
 **/
data class ApiMarker(
    /**
     * 类型
     */
    val type: Type,
    /**
     * 分类
     */
    val category: String,
    /**
     * 名称
     */
    val name: String,
    /**
     * 方法（HTTP）
     */
    val methods: List<String>,
    /**
     * 路径
     */
    val paths: List<String>,
) {
    enum class Type {
        CONTROLLER, METHOD
    }

    fun contains(marker: ApiMarker): Boolean {
        return marker.methods.any { methods.contains(it) } && marker.paths.any { paths.contains(it) }
    }
}
