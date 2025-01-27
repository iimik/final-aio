package org.ifinalframework.plugins.aio.application.aop;

import com.intellij.openapi.application.ReadAction
import org.aopalliance.intercept.MethodInterceptor
import org.aopalliance.intercept.MethodInvocation


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
            ReadAction.run<Throwable> { invocation.proceed() }
            return null
        } else {
            return ReadAction.compute<Any?, Throwable> { invocation.proceed() }
        }
    }
}