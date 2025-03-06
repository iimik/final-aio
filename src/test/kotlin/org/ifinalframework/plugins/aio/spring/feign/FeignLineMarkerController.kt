package org.ifinalframework.plugins.aio.spring.feign;

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * FeignLineMarkerController
 *
 * @author iimik
 * @since 0.0.5
 **/
@RestController
@RequestMapping("/api/feign")
class FeignLineMarkerController {
    @GetMapping("/get")
    fun getFeign() {
    }

    @PostMapping("/post")
    fun postFeign() {
    }
}