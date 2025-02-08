package org.ifinalframework.plugins.aio.api.spi;

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.jvm.AnnotationResolver
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.util.SpiUtil
import org.jetbrains.uast.*
import org.jetbrains.uast.kotlin.KotlinUCollectionLiteralExpression
import java.util.stream.Stream


/**
 * SpringApiMethodService
 * @issue 14
 * @author iimik
 * @since 0.0.2
 **/
class SpringApiMethodService : ApiMethodService {
    private val annotationResolver = SpiUtil.languageSpi<AnnotationResolver<PsiElement>>()
    private val docService = SpiUtil.languageSpi<DocService>()

    override fun getApiMarker(element: PsiElement): ApiMarker? {

        val uElement = R.computeInRead{ element.toUElement()} ?: return null
        if (uElement !is UIdentifier) return null

        val uParent = R.computeInRead{ uElement.uastParent } ?: return null

        return when (uParent) {
            is UMethod -> {
                val uClass = R.computeInRead{ uParent.getContainingUClass()} ?: return null
                val category = docService.getSummary(uClass.sourcePsi!!) ?: uClass.name ?: return null
                val name = docService.getSummary(uParent.sourcePsi!!) ?: uParent.name

                val clazzRequestAnnotation = R.computeInRead{ uClass.findAnnotation(SpringAnnotations.REQUEST_MAPPING) }
                val methodRequestMappingAnn =
                    SpringAnnotations.REQUEST_MAPPINGS.firstNotNullOfOrNull { R.computeInRead{uParent.findAnnotation(it)} } ?: return null

                val qualifiedName = methodRequestMappingAnn.qualifiedName

                val methods = when (qualifiedName) {
                    SpringAnnotations.REQUEST_MAPPING -> {
                        val findAttributeValue = methodRequestMappingAnn.findAttributeValue("method")
                        val list =
                            (findAttributeValue as KotlinUCollectionLiteralExpression).valueArguments.map { it.tryResolveNamed()?.name }
                                .filterNotNull()
                                .toList()
                        list
                    }

                    SpringAnnotations.GET_MAPPING -> listOf("GET")
                    SpringAnnotations.POST_MAPPING -> listOf("POST")
                    SpringAnnotations.PUT_MAPPING -> listOf("PUT")
                    SpringAnnotations.PATCH_MAPPING -> listOf("PATCH")
                    SpringAnnotations.DELETE_MAPPING -> listOf("DELETE")
                    else -> listOf("UNKNOWN")
                }


                val classRequestAnnotationMap = clazzRequestAnnotation?.let { annotationResolver.resolve(it.sourcePsi!!) }
                val methodRequestAnnotationMap = annotationResolver.resolve(methodRequestMappingAnn.sourcePsi!!)
                val paths = getApiPaths(classRequestAnnotationMap, methodRequestAnnotationMap)
                return ApiMarker(ApiMarker.Type.METHOD, category, name, methods, paths)
            }

            else -> null
        }


    }

    private fun getApiPaths(classAnnotation: Map<String, Any?>?, methodAnnotation: Map<String, Any?>): List<String> {
        val classPaths = classAnnotation?.let { getRequestMappingPath(classAnnotation) } ?: emptyList()
        val methodPaths = getRequestMappingPath(methodAnnotation)

        if (classPaths.isEmpty() && methodPaths.isEmpty()) return emptyList()

        if (classPaths.isEmpty() && methodPaths.isNotEmpty()) return methodPaths

        if (classPaths.isNotEmpty() && methodPaths.isEmpty()) return classPaths


        return classPaths.flatMap { prefix -> methodPaths.map { "${prefix.trimEnd('/')}/${it.trimStart('/')}" } }.toList()

    }

    private fun getRequestMappingPath(annotation: Map<String, Any?>): List<String> {
        val paths =  Stream.of("value", "path")
            .map { annotation[it] }
            .filter { it != null }
            .findFirst().orElse(null) ?: return emptyList()

        if(paths is List<*>) {
            return paths as List<String>
        }

        return listOf(paths as String)
    }
}