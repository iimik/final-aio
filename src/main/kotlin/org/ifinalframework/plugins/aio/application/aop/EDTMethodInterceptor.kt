package org.ifinalframework.plugins.aio.application.aop;

import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation
import org.ifinalframework.plugins.aio.R


/**
 * EDTMethodInterceptor
 *
 * @author iimik
 * @since 0.0.2
 **/
class EDTMethodInterceptor : MethodInterceptor {
    @Throws(Throwable::class)
    override fun invoke(invocation: MethodInvocation): Any? {
        return R.dispatch { invocation.proceed() }
    }
}