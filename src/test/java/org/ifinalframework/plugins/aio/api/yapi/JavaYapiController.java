package org.ifinalframework.plugins.aio.api.yapi;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 测试Markdown目录
 *
 * @author iimik
 * @issue 17
 * @jira
 * @since 0.0.2
 **/
@RequestMapping({"/java"})
public class JavaYapiController {

    /**
     * 测试Markdown名称
     *
     * @return
     */
    @GetMapping
    public String index() {
        // # 17
        return "index";
    }
}
