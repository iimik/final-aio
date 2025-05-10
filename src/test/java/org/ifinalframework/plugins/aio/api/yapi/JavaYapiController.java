package org.ifinalframework.plugins.aio.api.yapi;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试Markdown目录
 *
 * @author iimik
 * @issue 17
 * @jira 1
 * @since 0.0.2
 **/
@RestController
@RequestMapping({"/java"})
public class JavaYapiController {

    /**
     * 测试Markdown名称
     *
     * @return
     */
    @GetMapping("/value")
    public String index () {
        // # 17
        return "index";
    }
}
