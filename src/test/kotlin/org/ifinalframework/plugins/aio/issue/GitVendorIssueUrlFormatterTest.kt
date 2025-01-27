package org.ifinalframework.plugins.aio.issue

import com.intellij.testFramework.LightPlatformTestCase
import org.ifinalframework.plugins.aio.git.GitRemote
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * GitVendorIssueUrlFormatterTest
 *
 * @author iimik
 * @since 0.0.1
 */
class GitVendorIssueUrlFormatterTest : LightPlatformTestCase() {

    private val formatter = GitVendorIssueUrlFormatter()

    @Test
    fun testFormat() {
        // github.com
        Assertions.assertEquals(
            "https://github.com/iimik/final-aio/issues/11",
            formatter.format(GitRemote("github", "https", "github.com", "iimik/final-aio"), Issue(IssueType.ISSUE, "11"))
        )
        // gitlab.com
        Assertions.assertEquals(
            "https://gitlab.com/iimik/final-aio/-/issues/1",
            formatter.format(GitRemote("gitlab", "https", "gitlab.com", "iimik/final-aio"), Issue(IssueType.ISSUE, "1"))

        )
    }
}