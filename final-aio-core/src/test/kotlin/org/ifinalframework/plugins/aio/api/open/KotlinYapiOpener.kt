package org.ifinalframework.plugins.aio.api.open

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * IDEA
 */
@RestController
@RequestMapping("/kotlin")
class KotlinYapiOpener {
    
    @GetMapping("/index")
    fun index(){
    }

    @GetMapping("/index2")
    fun index2(){
    }

}