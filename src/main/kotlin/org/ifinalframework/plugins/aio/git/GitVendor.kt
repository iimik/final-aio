package org.ifinalframework.plugins.aio.git


/**
 * GitVendor
 * @author iimik
 * @since 0.0.1
 **/
enum class GitVendor(
    val vendor: String,
    val host: String,
    val issueFormat: String,
) {

    GITHUB("GitHub", "github.com", issueFormat = "https://github.com/\${path}/issues/\${issue}"),
    GITLAB("Gitlab", "gitlab.com", issueFormat = "https://gitlab.com/\${path}/-/issues/\${issue}"),
    DEFAULT("Gitlab", "*", issueFormat = "\${schema}://\${host}/\${path}/-/issues/\${issue}");

    companion object {
        fun getByHostOrDefault(host: String): GitVendor {
            return values().firstOrNull { it.host.equals(host, true) } ?: DEFAULT
        }
    }
}