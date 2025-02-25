package org.ifinalframework.plugins.aio.spring.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping


/**
 * FeignLineMarkerClient
 *
 * @author iimik
 * @since 0.0.5
 **/
@FeignClient(name = "nana", path = "/api/feign")
interface FeignLineMarkerClient {

    @GetMapping("/get")
    fun getFeign()
}