package org.ifinalframework.plugins.aio.jvm

import com.intellij.psi.PsiJvmModifiersOwner
import org.ifinalframework.plugins.aio.core.annotation.AnnotationAttributes

/**
 * AnnotationService
 *
 * @author iimik
 */
interface AnnotationService {

    fun findAnnotationAttributes(element: PsiJvmModifiersOwner, fqn: String): AnnotationAttributes?

}