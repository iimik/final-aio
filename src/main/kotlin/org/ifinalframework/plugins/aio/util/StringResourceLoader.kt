package org.ifinalframework.plugins.aio.util

import org.apache.velocity.exception.ResourceNotFoundException
import org.apache.velocity.runtime.resource.Resource
import org.apache.velocity.runtime.resource.loader.ResourceLoader
import org.apache.velocity.util.ExtProperties
import java.io.Reader
import java.io.StringReader

/**
 * StringResourceLoader
 *
 * @author iimik
 */
class StringResourceLoader: ResourceLoader() {
    override fun init(configuration: ExtProperties?) {

    }

    override fun getResourceReader(source: String?, encoding: String?): Reader? {
        if (source == null || source.isEmpty()) {
            throw ResourceNotFoundException("Need to specify a template name!")
        }

        return StringReader(source)
    }

    override fun isSourceModified(resource: Resource?): Boolean {
        return false
    }

    override fun getLastModified(resource: Resource?): Long {
        return 0
    }
}