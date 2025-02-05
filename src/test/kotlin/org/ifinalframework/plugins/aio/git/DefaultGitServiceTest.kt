package org.ifinalframework.plugins.aio.git

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.apache.commons.lang3.StringUtils
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

/**
 * DefaultGitServiceTest
 *
 * @author iimik
 * @since 0.0.1
 */
class DefaultGitServiceTest : BasePlatformTestCase() {

    private val gitService = DefaultGitService(project)

    @Test
    fun testGetRemotes() {
        val remotes = gitService.getRemotes()
        Assertions.assertNotNull(remotes)
    }

    @Test
    fun testGetRemoteByNameOrDefault() {
        val remote = gitService.getRemoteByNameOrDefault("github")
        Assertions.assertNotNull(remote)
        Assertions.assertEquals("origin", remote.name )
    }

    override fun getTestDataPath(): String {
        return "src/main/kotlin/" + DefaultGitService::class.java.packageName.replace('.', '/')
    }


}