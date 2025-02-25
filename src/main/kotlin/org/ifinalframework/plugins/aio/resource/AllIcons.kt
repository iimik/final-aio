package org.ifinalframework.plugins.aio.resource

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon


/**
 * AllIcons
 *
 * @author iimik
 * @since 0.0.1
 **/
class AllIcons {
    class Issues {
        companion object {
            val ISSUE = load("assets/icons/git.svg")
            val JIRA = load("assets/icons/jira.svg")
        }
    }

    class Api{
        companion object {
            val MARKDOWN = load("assets/icons/markdown.svg")
            val VIEW = load("assets/icons/apiView.svg")
        }
    }

    class Mybatis {
        companion object {
            val JVM  = load("assets/icons/mybatis-jvm.svg")
            val XML  = load("assets/icons/mybatis-xml.svg")
        }
    }

    companion object {
        fun load(path: String): Icon {
            return IconLoader.getIcon(path, AllIcons::class.java)
        }
    }
}