package org.ifinalframework.plugins.aio.application.aop;

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.ifinalframework.plugins.aio.R


/**
 * WriteActionMethodInterceptor
 *
 * @author iimik
 * @since 0.0.1
 * @see org.ifinalframework.plugins.aio.application.annotation.ReadAction
 **/
class WriteActionMethodInterceptor : MethodInterceptor {
    @Throws(Throwable::class)
    override fun invoke(invocation: MethodInvocation): Any? {
        val method = invocation.method
        val returnType = method.returnType

        if (Void.TYPE == returnType) {
            R.runInWrite { invocation.proceed() }
            return null
        } else {
            return R.computeInWrite { invocation.proceed() }
        }
    }
}