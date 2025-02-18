package org.ifinalframework.plugins.aio.mybatis;

import com.intellij.codeInsight.daemon.LineMarkerInfo
import com.intellij.codeInsight.daemon.LineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.resource.AllIcons
import org.ifinalframework.plugins.aio.util.SpiUtil
import java.awt.event.MouseEvent


/**
 * Mybatis 行标记
 *
 * - 类（接口）定义行添加标记，点击打开xml文件
 *
 * @issue 24
 * @author iimik
 * @since 0.0.4
 **/
class MybatisLineMarkerProvider : LineMarkerProvider {

    private val mybatisLineMarkerService = SpiUtil.languageSpi<MybatisLineMarkerService>()

    override fun getLineMarkerInfo(element: PsiElement): LineMarkerInfo<*>? {
        val mybatisMarker = mybatisLineMarkerService.getMarker(element) ?: return null
        val builder = NavigationGutterIconBuilder.create(AllIcons.Mybatis.RED)
        builder.setTargets(element)
        builder.setTooltipText("跳转到Mapper")
        return builder.createLineMarkerInfo(
            element
        ) { mouseEvent: MouseEvent?, element: PsiElement? ->
        }
    }


}