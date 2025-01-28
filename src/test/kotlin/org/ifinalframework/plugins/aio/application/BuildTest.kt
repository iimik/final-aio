package org.ifinalframework.plugins.aio.application;

import com.intellij.testFramework.LightPlatformTestCase
import org.junit.jupiter.api.Test


/**
 * BuildTest
 *
 * @author iimik
 * @since 0.0.2
 **/
class BuildTest: LightPlatformTestCase() {

    @Test
    fun testReplaceIssueWithUrl(){
        val CHANGELOG = """
            <!-- Keep a Changelog guide -> https://keepachangelog.com -->

            # Final AIO 变更日志

            ## [Unreleased]

            ### 新增功能

            - Api 管理
              - 添加Api方法Markdown行标记，并可快速在编辑器中打开进行编辑。(#14)

            ## [0.0.1] - 2025-01-27

            ### 新增功能

            - Issue 管理
              - 添加Issue行标记，并可在浏览器中快速打开，支持Git Issue和Jira。(#10)

        """.trimIndent()

        val regex = Regex("""(#\d+)""")
        val foundMatches = regex.findAll(CHANGELOG)
        var formatIssues: String = CHANGELOG
        foundMatches.forEach { result ->
            formatIssues = formatIssues.replace("(${result.value})", "([${result.value}](https://github.com/iimik/final-aio/issues/${result.value.trimStart('#')}))")
        }

        println(formatIssues)

    }
}
