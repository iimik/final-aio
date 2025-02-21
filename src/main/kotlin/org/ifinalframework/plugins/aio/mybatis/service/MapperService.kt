package org.ifinalframework.plugins.aio.mybatis.service

import com.intellij.grazie.utils.orFalse
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiModifier
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.xml.DomService
import org.ifinalframework.plugins.aio.mybatis.MybatisConstants
import org.ifinalframework.plugins.aio.mybatis.MybatisMarker
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper
import org.jetbrains.uast.*


/**
 * MapperService
 *
 * @author iimik
 * @since 0.0.4
 **/
@Service(Service.Level.PROJECT)
class MapperService(
    private val project: Project,
) {

    fun findMappers(): Collection<Mapper> {
        return DomService.getInstance().getFileElements(Mapper::class.java, project, GlobalSearchScope.allScope(project))
            .map { it.rootElement }
    }

    fun process(element: Any, processor: Processor<MybatisMarker>) {

        val uElement = when (element) {
            is UElement -> element
            is PsiElement -> element.toUElement()
            else -> null
        } ?: return

        val uClass = uElement.getContainingUClass() ?: return

        // 以Mapper结尾的接口
        if (!uClass.isInterface || !uClass.name!!.endsWith("Mapper")) {
            return
        }

        when (uElement) {
            is UClass -> process(uElement, processor)
            is UMethod -> process(uElement, processor)
            else -> return
        }
    }




}