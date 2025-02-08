package org.ifinalframework.plugins.aio.jvm.kotlin

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import junit.framework.TestCase
import org.ifinalframework.plugins.aio.R
import org.ifinalframework.plugins.aio.api.constans.SpringAnnotations
import org.ifinalframework.plugins.aio.jvm.AnnotationForResolver
import org.jetbrains.kotlin.idea.util.findAnnotation
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.KtClass
import org.springframework.web.bind.annotation.RequestMapping
import kotlin.reflect.KClass

/**
 * KotlinAnnotationResolverTest
 *
 * @author iimik
 * @since 0.0.2
 */
@AnnotationForResolver.StringValue("Hello World!")
@RequestMapping
class KotlinAnnotationResolverTest : BasePlatformTestCase() {

    private var resolver: KotlinAnnotationResolver? = null
    private val annotationFinder = KotlinAnnotationFinder()

    override fun setUp() {
        super.setUp()
        resolver = KotlinAnnotationResolver()
    }

    fun testResolve() {
        TestCase.assertEquals("Hello World!", getValue(AnnotationForResolver.StringValue::class))
    }

    private fun getValue(ann: KClass<out Annotation>): Any? {
        val ktClass = getKtClass()
        val annotation = R.computeInRead { annotationFinder.findAnnotation(ktClass, SpringAnnotations.REQUEST_MAPPING) }
        val ktAnnotationEntry = ktClass.findAnnotation(FqName(ann.simpleName.toString()!!))
        val map = resolver!!.resolve(ktAnnotationEntry!!)
        return map["value"]
    }

    private fun getKtClass(): KtClass {
        val psiFile = myFixture.configureByFile(this::class.simpleName + ".kt")
        val ktClass = psiFile.children.firstOrNull { it is KtClass }
        return ktClass as KtClass;
    }

    override fun getTestDataPath(): String {
        return "src/test/kotlin/" + this::class.java.packageName.replace('.', '/')
    }
}