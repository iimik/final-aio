package org.ifinalframework.plugins.aio.mybatis.service

import com.intellij.psi.PsiElement
import com.intellij.util.xml.DomElement


/**
 * JvmService
 *
 * @author iimik
 * @since 0.0.4
 **/
interface JvmService {

    fun findMapperOrStatement(element: PsiElement): DomElement?

}