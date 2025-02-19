package org.ifinalframework.plugins.aio.mybatis

import com.intellij.psi.PsiElement
import org.ifinalframework.plugins.aio.mybatis.java.JavaMybatisLineMarkerService
import org.ifinalframework.plugins.aio.mybatis.kotlin.KotlinMybatisLineMarkerService
import org.ifinalframework.plugins.aio.spi.annotation.LanguageSpi


/**
 * MybatisLineMarkerService
 *
 * @author iimik
 * @since 0.0.4
 **/
@LanguageSpi<MybatisLineMarkerService>(
//    JavaMybatisLineMarkerService::class,
//    KotlinMybatisLineMarkerService::class,
    JvmMybatisLineMarkerService::class
)
@FunctionalInterface
interface MybatisLineMarkerService {
    fun getMarker(element: PsiElement): MybatisMarker?
}