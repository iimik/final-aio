package org.ifinalframework.plugins.aio.spring


/**
 * HttpEndPointIndex
 *
 * @author iimik
 * @since 0.0.25
 **/
data class HttpEndPointIndex(
    val feign: Map<HttpEndPointKey, List<HttpEndPoint>>,
    val controller: Map<HttpEndPointKey, List<HttpEndPoint>>
) {
    fun findControllerHttpEndPoint(key: HttpEndPointKey): List<HttpEndPoint> {
        return controller[key].orEmpty()
    }

    fun findFeignHttpEndPoint(key: HttpEndPointKey): List<HttpEndPoint> {
        return feign[key].orEmpty()
    }
}
