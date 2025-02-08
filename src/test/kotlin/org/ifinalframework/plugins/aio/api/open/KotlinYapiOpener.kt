package org.ifinalframework.plugins.aio.api.open

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/kotlin")
class KotlinYapiOpener {
    
    @GetMapping("/index")
    fun index(){

    }
}