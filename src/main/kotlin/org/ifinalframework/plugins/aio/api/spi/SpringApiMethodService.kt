package org.ifinalframework.plugins.aio.api.spi;

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.ifinalframework.plugins.aio.psi.service.DocService
import org.ifinalframework.plugins.aio.util.SpiUtil
import org.jetbrains.uast.*
import org.jetbrains.uast.kotlin.KotlinStringULiteralExpression
import org.jetbrains.uast.kotlin.KotlinUCollectionLiteralExpression
import org.jetbrains.uast.kotlin.KotlinUVarargExpression
import java.util.stream.Stream


/**
 * SpringApiMethodService
 * @issue 14
 * @author iimik
 * @since 0.0.2
 **/
class SpringApiMethodService : ApiMethodService {

    private val docService = SpiUtil.languageSpi(DocService::class)

    override fun getApiMarker(element: PsiElement): ApiMarker? {
        val uElement = element.toUElement() ?: return null
        if (uElement !is UIdentifier) return null

        val uParent = uElement.uastParent ?: return null

        return when (uParent) {
            is UMethod -> {
                val uClass = uParent.getContainingUClass() ?: return null
                val category = docService.getSummary(uClass.sourcePsi!!) ?: uClass.name ?: return null
                val name = docService.getSummary(uParent.sourcePsi!!) ?: uParent.name

                val clazzRequestAnnotation = uClass.findAnnotation(SpringAnnotations.REQUEST_MAPPING)
                val methodRequestMappingAnn =
                    SpringAnnotations.REQUEST_MAPPINGS.firstNotNullOfOrNull { uParent.findAnnotation(it) }
                        ?: return null

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


                val paths = getApiPaths(clazzRequestAnnotation, methodRequestMappingAnn)
                return ApiMarker(ApiMarker.Type.METHOD, category, name, methods, paths)
            }

            else -> null
        }


    }

    private fun getApiPaths(classAnnotation: UAnnotation?, methodAnnotation: UAnnotation): List<String> {
        val classPaths = classAnnotation?.let { getRequestMappingPath(classAnnotation) } ?: emptyList()
        val methodPaths = getRequestMappingPath(methodAnnotation)

        if (classPaths.isEmpty() && methodPaths.isEmpty()) return emptyList()

        if (classPaths.isEmpty() && methodPaths.isNotEmpty()) return methodPaths

        if (classPaths.isNotEmpty() && methodPaths.isEmpty()) return classPaths


        return classPaths.flatMap { prefix -> methodPaths.map { "$prefix/$it" } }.toList()

    }

    private fun getRequestMappingPath(annotation: UAnnotation): List<String> {

        val it = Stream.of("value", "path")
            .map { annotation.findAttributeValue(it) }
            .filter { it != null }
            .findFirst().orElse(null) ?: return emptyList()

        return when (it) {
            is KotlinUCollectionLiteralExpression -> {
                it.valueArguments.mapNotNull { valueArg -> valueArg.tryResolveNamed()?.name }.toList()
            }

            is KotlinUVarargExpression -> {
                it.valueArguments.mapNotNull { valueArg -> (valueArg as KotlinStringULiteralExpression)?.text }.toList()
            }

            is KotlinStringULiteralExpression -> {
                listOf(it.text)
            }

            else -> listOf()
        }

    }
}