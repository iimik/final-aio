package org.ifinalframework.plugins.aio.application.aop;

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.ifinalframework.plugins.aio.R


/**
 * AsyncMethodInterceptor
 *
 * @author iimik
 * @since 0.0.2
 **/
class AsyncMethodInterceptor : MethodInterceptor {
    @Throws(Throwable::class)
    override fun invoke(invocation: MethodInvocation): Any? {
        return R.async { invocation.proceed() }
    }
}