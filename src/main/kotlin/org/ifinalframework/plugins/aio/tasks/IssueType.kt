package org.ifinalframework.plugins.aio.tasks

import com.intellij.util.containers.stream
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.resource.I18N
import javax.swing.Icon


/**
 * IssueType
 *
 * @author iimik
 * @since 0.0.1
 **/
enum class IssueType(
    val icon: Icon,
    val toolTip: String,
) {
    ISSUE(AllIcons.Issues.ISSUE, I18N.message("Issue.JvmIssueLineMarkerProvider.issueTooltip")),
    JIRA(AllIcons.Issues.JIRA,I18N.message("Issue.JvmIssueLineMarkerProvider.jiraTooltip"));

    companion object {
        fun ofNullable(name: String?): IssueType? {
            return values().stream()
                .filter { v -> v.name.equals(name, ignoreCase = true) }
                .findFirst()
                .orElse(null)
        }
    }
}