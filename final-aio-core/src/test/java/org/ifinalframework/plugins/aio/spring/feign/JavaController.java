package org.ifinalframework.plugins.aio.spring.feign;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * JavaController
 *
 * @author iimik
 * @since 1.6.0
 **/
@RequestMapping("/api/feign/java")
@RestController
public class JavaController {
    @GetMapping("/index")
    public String get() {
        return "index";
    }
}
