package org.ifinalframework.plugins.aio.application;

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.R
import org.springframework.boot.env.PropertySourceLoader
import org.springframework.core.env.StandardEnvironment
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.SpringFactoriesLoader
import java.io.IOException
import java.util.*


/**
 * ElementEnvironment
 *
 * @author iimik
 * @since 0.0.1
 **/
class ElementEnvironment : StandardEnvironment(), ElementPropertySourcesLoader {
    override fun load(classLoader: ClassLoader, element: PsiElement) {
        val propertySources = propertySources

        val basePath: String = R.Read.compute { element.project.basePath }!!
        // load .final config file
        val configPaths: List<String> = R.Read.compute { DefaultConfigService().getConfigPaths(element) }!!
        Collections.sort(configPaths)

        val propertySourceLoaders = SpringFactoriesLoader.loadFactories(
            PropertySourceLoader::class.java, classLoader
        )

        for (configPath in configPaths) {
            for (propertySourceLoader in propertySourceLoaders) {
                for (extension in propertySourceLoader.fileExtensions) {
                    try {
                        val file = "$configPath/$CONFIG_FILE_NAME$extension"
                        val urlResource: Resource = FileSystemResource(file)
                        if (urlResource.exists()) {
                            logger.info("found config file: " +  file.substring(basePath.length + 1))
                            val sources = propertySourceLoader.load(file, urlResource)
                            for (source in sources) {
                                propertySources.addFirst(source)
                            }
                        }
                    } catch (e: IOException) {
                        throw RuntimeException(e)
                    }
                }
            }
        }
    }

    companion object {
        private const val CONFIG_FILE_NAME = ".final."
    }
}
