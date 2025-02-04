package org.ifinalframework.plugins.aio.application;

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.R
import org.springframework.boot.env.PropertiesPropertySourceLoader
import org.springframework.boot.env.PropertySourceLoader
import org.springframework.core.env.MutablePropertySources
import org.springframework.core.env.StandardEnvironment
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.SpringFactoriesLoader
import java.io.IOException
import java.util.*


/**
 * ElementEnvironment
 *
 * - `.final.properties`
 * - `.final.local.properties`
 * - `.final.yml`
 * - `.final.local.yml`
 * - `.final.yaml`
 * - `.final.local.yaml`
 * - `.env`
 * - `.env.local`
 *
 *
 * @author iimik
 * @since 0.0.1
 **/
class ElementEnvironment : StandardEnvironment(), ElementPropertySourcesLoader {

    private val configFileNames = arrayOf(".final", ".final.local")
    private val envConfigFileName = arrayOf(".env", ".env.local")

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
                if (propertySourceLoader is PropertiesPropertySourceLoader) {
                    for (fileName in envConfigFileName) {
                        val file = "$configPath/$fileName"
                        doLoad(file, basePath, propertySourceLoader, propertySources)
                    }
                }

                for (extension in propertySourceLoader.fileExtensions) {
                    for (fileName in configFileNames) {
                        val file = "$configPath/${fileName.trimEnd('.')}.$extension"
                        doLoad(file, basePath, propertySourceLoader, propertySources)
                    }
                }

            }
        }
    }

    private fun doLoad(
        file: String,
        basePath: String,
        propertySourceLoader: PropertySourceLoader,
        propertySources: MutablePropertySources
    ) {
        try {
            val urlResource: Resource = FileSystemResource(file)
            if (urlResource.exists()) {
                logger.info("found config file: " + file.substring(basePath.length + 1))
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
