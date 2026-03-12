package org.ifinalframework.plugins.aio.application.aop;

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.ifinalframework.plugins.aio.R


/**
 * ReadActionMethodInterceptor
 *
 * @author iimik
 * @since 0.0.1
 **/
class ReadActionMethodInterceptor : MethodInterceptor {
    @Throws(Throwable::class)
    override fun invoke(invocation: MethodInvocation): Any? {
        val method = invocation.method
        val returnType = method.returnType

        if (Void.TYPE == returnType) {
            R.runInRead { invocation.proceed() }
            return null
        } else {
            return R.computeInRead { invocation.proceed() }
        }
    }
}