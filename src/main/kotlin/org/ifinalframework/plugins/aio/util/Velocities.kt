package org.ifinalframework.plugins.aio.util

import org.apache.velocity.VelocityContext
import org.apache.velocity.app.Velocity
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.context.Context
import org.apache.velocity.exception.VelocityException
import org.apache.velocity.runtime.RuntimeConstants
import java.beans.Introspector
import java.io.StringReader
import java.io.StringWriter
import java.util.*

/**
 * Velocities
 *
 * @author iimik
 */
class Velocities {

    companion object {

        private val velocityEngine = VelocityEngine()

        init {
            //加载器名称
            velocityEngine.setProperty("resource.loader", "template")
            //配置加载器实现类
            velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER_INSTANCE, StringResourceLoader())
            velocityEngine.setProperty("input.encoding", "UTF-8")
            velocityEngine.setProperty("output.encoding", "UTF-8")
            velocityEngine.init()
        }

        private val contextFactory: ContextFactory = DefaultContextFactory()

        /**
         * return the eval value from `express` with `params`.
         *
         * @param express eval express.
         * @param params  eval params.
         * @since 1.2.4
         */
        fun eval(express: String, params: Any): String {
            val context: Context = contextFactory.create(params)
            return eval(express, context)
        }

        /**
         * return the eval value from `express` with [Context].
         *
         * @param express eval express.
         * @param context eval params.
         * @see Velocity.evaluate
         * @since 1.2.4
         */
        fun eval(express: String, context: Context): String {
            val writer = StringWriter()
            velocityEngine.evaluate(context, writer, "velocity", StringReader(express))
            return writer.toString()
        }
    }


    private interface ContextFactory {
        @Throws(VelocityException::class)
        fun create(param: Any): Context
    }

    private class DefaultContextFactory : ContextFactory {

        override fun create(param: Any): Context {

            if (Objects.isNull(param)) {
                return VelocityContext()
            }


            // Process map
            if (param is MutableMap<*, *>) {
                return fromMap(param)
            }


            // Process Bean
            return fromBean(param)
        }

        private fun fromMap(param: MutableMap<*, *>): Context {
            val context = VelocityContext()
            appendMarkdownHeader(context)
            param.entries.forEach {
                context.put(it.key.toString(), it.value)
            }
            return context
        }

        private fun fromBean(bean: Any): Context {
            val context = VelocityContext()
            appendMarkdownHeader(context)
            try {
                val beanInfo = Introspector.getBeanInfo(bean.javaClass)
                for (propertyDescriptor in beanInfo.propertyDescriptors) {
                    val readMethod = propertyDescriptor.getReadMethod()

                    if (Objects.isNull(readMethod)) {
                        continue
                    }

                    context.put(propertyDescriptor.name, readMethod!!.invoke(bean))
                }
            } catch (e: Exception) {
                throw VelocityException(e)
            }
            return context
        }

        private fun appendMarkdownHeader(context: VelocityContext) {
            context.put("H1","#")
            context.put("H2","##")
            context.put("H3","###")
            context.put("H4","####")
            context.put("H5","#####")
            context.put("H6","######")
        }
    }

}