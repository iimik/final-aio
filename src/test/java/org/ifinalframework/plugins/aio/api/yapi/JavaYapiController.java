package org.ifinalframework.plugins.aio.api.yapi;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * JavaYapiController
 * @issue 17
 * @jira 
 * @author iimik
 * @since 0.0.2
 **/
@RequestMapping({"/java"})
public class JavaYapiController {

    @GetMapping
    public String index() {
        // # 17
        return "index";
    }
}
