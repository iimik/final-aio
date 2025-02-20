package org.ifinalframework.plugins.aio.mybatis.service

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.mybatis.MybatisMarker
import java.util.function.Function


/**
 * MapperLineMarkerService
 *
 * @author iimik
 * @since 0.0.4
 * @see [JvmMapperLineMarkerService]
 **/
interface MapperLineMarkerService : Function<PsiElement, MybatisMarker?> {
}