package org.ifinalframework.plugins.aio.issue

import com.intellij.util.containers.stream
import org.ifinalframework.plugins.aio.resource.AllIcons
import javax.swing.Icon


/**
 * IssueType
 *
 * @author iimik
 * @since 0.0.1
 **/
enum class IssueType(
    val icon: Icon,
) {
    ISSUE(AllIcons.Issues.ISSUE),
    JIRA(AllIcons.Issues.JIRA);

    companion object {
        fun ofNullable(name: String?): IssueType? {
            return values().stream()
                .filter { v -> v.name.equals(name, ignoreCase = true) }
                .findFirst()
                .orElse(null)
        }
    }
}