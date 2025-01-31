package org.ifinalframework.plugins.aio

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.util.ThrowableComputable


/**
 * R
 *
 * @author iimik
 * @since 0.0.1
 **/

class R {
    class Read {
        companion object {
            fun <T> compute(action: ThrowableComputable<T, Throwable>): T? {
                try {
                    return ReadAction.compute(action)
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
    }

    class Write {
        companion object {
            fun <T> compute(action: ThrowableComputable<T, Throwable>): T? {
                try {
                    return WriteAction.compute(action)
                } catch (e: Exception) {
                    throw RuntimeException(e)
                }
            }
        }
    }

    class Async {
        companion object {
            fun run(action: Runnable) {
                ApplicationManager.getApplication().executeOnPooledThread(action)
            }
        }
    }
}