package org.ifinalframework.plugins.aio.application.aop;

import com.intellij.openapi.application.ApplicationManager
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation


/**
 * AsyncMethodInterceptor
 *
 * @author iimik
 * @since 1.6.0
 **/
class AsyncMethodInterceptor : MethodInterceptor {
    @Throws(Throwable::class)
    override fun invoke(invocation: MethodInvocation): Any? {
        ApplicationManager.getApplication().executeOnPooledThread {
            try {
                invocation.proceed()
            } catch (e: Throwable) {
                throw RuntimeException(e)
            }
        }
        return null
    }
}