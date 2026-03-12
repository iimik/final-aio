package org.ifinalframework.plugins.aio.application.aop;

import org.aopalliance.intercept.MethodInterceptor
import org.ifinalframework.plugins.aio.application.annotation.Async
import org.ifinalframework.plugins.aio.application.annotation.EDT
import org.ifinalframework.plugins.aio.application.annotation.ReadAction
import org.ifinalframework.plugins.aio.application.annotation.WriteAction
import org.springframework.aop.Advisor
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.EnableAspectJAutoProxy
import kotlin.reflect.KClass


/**
 * AopConfig
 *
 * @author iimik
 * @since 0.0.1
 **/
@Configuration
@EnableAspectJAutoProxy
open class AopConfig {
    @Bean
    open fun readActionAdvisor(): Advisor {
        return buildAdvisor(ReadAction::class, ReadActionMethodInterceptor())
    }

    @Bean
    open fun writeActionAdvisor(): Advisor {
        return buildAdvisor(WriteAction::class, WriteActionMethodInterceptor())
    }

    @Bean
    open fun asyncActionAdvisor(): Advisor {
        return buildAdvisor(Async::class, AsyncMethodInterceptor())
    }

    @Bean
    open fun edtAdvisor(): Advisor {
        return buildAdvisor(EDT::class, EDTMethodInterceptor())
    }

    private fun buildAdvisor(annClass: KClass<out Annotation>, methodInterceptor: MethodInterceptor): Advisor {
        return DefaultPointcutAdvisor(AnnotationMatchingPointcut(null, annClass.java, true), methodInterceptor)
    }
}