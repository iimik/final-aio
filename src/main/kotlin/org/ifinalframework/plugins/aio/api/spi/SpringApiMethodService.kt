package org.ifinalframework.plugins.aio.api.spi

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import org.apache.commons.lang3.StringUtils
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.api.ApiProperties
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.common.util.UAnnotatedKt.findAnyAnnotation
import org.ifinalframework.plugins.aio.core.annotation.AnnotationAttributes
import org.ifinalframework.plugins.aio.jvm.AnnotationResolver
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.util.SpiUtil
import org.jetbrains.uast.UMethod
import org.jetbrains.uast.getContainingUClass
import org.jetbrains.uast.toUElement
import java.util.concurrent.CancellationException
import java.util.stream.Stream


/**
 * SpringApiMethodService
 * @issue 14
 * @author iimik
 * @since 0.0.2
 **/
class SpringApiMethodService : ApiMethodService {
    private val annotationResolver = SpiUtil.languageSpi<AnnotationResolver<PsiElement>>()
    private val docService = service<DocService>()

    override fun getApiMarker(element: PsiElement): ApiMarker? {

        try {

            val uElement = R.computeInRead { element.toUElement() } ?: return null

            return when (uElement) {
                is UMethod -> {
                    val uClass = R.computeInRead { uElement.getContainingUClass() } ?: return null
                    val category = docService.getSummary(uClass.sourcePsi!!) ?: uClass.name ?: return null
                    val name = docService.getSummary(uElement.sourcePsi!!) ?: uElement.name

                    val clazzRequestAnnotation =
                        R.computeInRead { uClass.findAnyAnnotation(SpringAnnotations.REQUEST_MAPPING, SpringAnnotations.FEIGN_CLIENT) }
                    val methodRequestMappingAnn =
                        SpringAnnotations.REQUEST_MAPPINGS.firstNotNullOfOrNull { R.computeInRead { uElement.findAnnotation(it) } }
                            ?: return null

                    val qualifiedName = R.computeInRead { methodRequestMappingAnn.qualifiedName }
                    val methodRequestAnnotationMap = annotationResolver.resolve(methodRequestMappingAnn.sourcePsi!!)

                    val methods = when (qualifiedName) {
                        SpringAnnotations.REQUEST_MAPPING -> methodRequestAnnotationMap.getList("method") ?: listOf("GET")
                        SpringAnnotations.GET_MAPPING -> listOf("GET")
                        SpringAnnotations.POST_MAPPING -> listOf("POST")
                        SpringAnnotations.PUT_MAPPING -> listOf("PUT")
                        SpringAnnotations.PATCH_MAPPING -> listOf("PATCH")
                        SpringAnnotations.DELETE_MAPPING -> listOf("DELETE")
                        else -> listOf("UNKNOWN")
                    }


                    val classRequestAnnotationMap = clazzRequestAnnotation?.let { annotationResolver.resolve(it.sourcePsi!!) }
                    val paths = getApiPaths(classRequestAnnotationMap, methodRequestAnnotationMap)
                    val securities = getSecurities(uElement, element.project)
                    return ApiMarker(ApiMarker.Type.METHOD, category, name, methods, paths, securities)
                }

                else -> null
            }
        } catch (_: CancellationException) {
            return null
        }


    }

    private fun getSecurities(method: UMethod, project: Project): List<String>? {
        val securityAnnotation = project.service<ApiProperties>().securityAnnotation
        if (StringUtils.isNotEmpty(securityAnnotation)) {
            val annClassName = StringUtils.substringBeforeLast(securityAnnotation, "#")
            val property = StringUtils.substringAfterLast(securityAnnotation, "#")

            val securityAnn = R.computeInRead { method.findAnnotation(annClassName) }
            if (securityAnn != null) {
                val annotationAttributes = annotationResolver.resolve(securityAnn.sourcePsi!!)
                return annotationAttributes.getList(property)
            }

        }

        return null
    }

    private fun getApiPaths(classAnnotation: AnnotationAttributes?, methodAnnotation: AnnotationAttributes): List<String> {
        val classPaths = classAnnotation?.let { getRequestMappingPath(classAnnotation) } ?: emptyList()
        val methodPaths = getRequestMappingPath(methodAnnotation)

        if (classPaths.isEmpty() && methodPaths.isEmpty()) return emptyList()

        if (classPaths.isEmpty() && methodPaths.isNotEmpty()) return methodPaths

        if (classPaths.isNotEmpty() && methodPaths.isEmpty()) return classPaths


        return classPaths.flatMap { prefix -> methodPaths.map { "${prefix.trimEnd('/')}/${it.trimStart('/')}" } }.toList()

    }

    private fun getRequestMappingPath(annotation: AnnotationAttributes): List<String> {
        return Stream.of("value", "path")
            .map { annotation.getList<String>(it) }
            .filter { it != null }
            .findFirst().orElse(null) ?: return emptyList()
    }
}