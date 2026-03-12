package org.ifinalframework.plugins.aio.spring.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.search.searches.AnnotationTargetsSearch
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.ifinalframework.plugins.aio.service.PsiService

/**
 * SpringService
 *
 * @author iimik
 */
@Service(Service.Level.PROJECT)
class SpringService(
    private val project: Project,
) {

    private val responseBodyAnnotations = mutableSetOf<PsiClass>()

    init {
        project.service<PsiService>().findClass(SpringAnnotations.RESPONSE_BODY)?.let { it ->
            responseBodyAnnotations.add(it)

            var hasResponseBodyAnnotations = responseBodyAnnotations.toSet()

            do {

                hasResponseBodyAnnotations = hasResponseBodyAnnotations.flatMap { ann ->
                    AnnotationTargetsSearch.search(ann).filterIsInstance<PsiClass>().filter { target ->  target.isAnnotationType }
                }.toSet()

                if (hasResponseBodyAnnotations.isNotEmpty()) {
                    responseBodyAnnotations.addAll(hasResponseBodyAnnotations)
                }


            }while (hasResponseBodyAnnotations.isNotEmpty())

        }

    }

    fun getResponseBodyAnnotations(): Set<PsiClass> {
        return responseBodyAnnotations.toSet()
    }

}