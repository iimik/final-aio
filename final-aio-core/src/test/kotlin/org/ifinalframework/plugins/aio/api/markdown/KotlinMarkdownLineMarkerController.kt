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
@RequestMapping(value = ["/kotlin1","/kotlin2"])
@RestController
class KotlinMarkdownLineMarkerController {

    /**
     * 注释
     */
    @GetMapping(value = ["/a","/b"])
    fun docs() {

    }
}