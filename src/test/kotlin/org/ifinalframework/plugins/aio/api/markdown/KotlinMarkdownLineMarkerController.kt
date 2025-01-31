package org.ifinalframework.plugins.aio.api.markdown;

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


/**
 * Markdown
 *
 * @author iimik
 * @since 0.0.1
 **/
@RequestMapping("/kotlin")
@RestController
class KotlinMarkdownLineMarkerController {

    /**
     * 注释
     */
    @GetMapping
    fun docs() {

    }
}