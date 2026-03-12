package org.ifinalframework.plugins.aio.jvm;


import com.google.common.collect.Lists;
import com.intellij.psi.PsiAnnotation;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiFile;
import com.intellij.testFramework.fixtures.BasePlatformTestCase;

import org.ifinalframework.plugins.aio.jvm.AnnotationForResolver.IntArray;
import org.ifinalframework.plugins.aio.jvm.AnnotationForResolver.IntValue;
import org.ifinalframework.plugins.aio.jvm.AnnotationForResolver.StringArray;
import org.ifinalframework.plugins.aio.jvm.AnnotationForResolver.StringValue;
import org.ifinalframework.plugins.aio.jvm.AnnotationForResolver.StringVararg;
import org.ifinalframework.plugins.aio.jvm.java.JavaAnnotationResolver;

import org.junit.jupiter.api.Assertions;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Map;

/**
 * JavaAnnotationResolverTest
 *
 * @author iimik
 * @since 0.0.2
 **/
@StringValue("Hello Java")
@StringVararg({"a", "b"})
@StringArray({"a", "b"})
@IntValue(1)
@IntArray({1, 2})
public class JavaAnnotationResolverTest extends BasePlatformTestCase {

    private final AnnotationResolver resolver = new JavaAnnotationResolver();
    private PsiClass psiClass;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        final PsiFile psiFile = myFixture.configureByFile(getClass().getSimpleName() + ".java");
        psiClass = (PsiClass) Arrays.stream(psiFile.getChildren()).filter(it -> it instanceof PsiClass).findFirst().orElse(null);
    }

    public void testStringValue() {
        Assertions.assertEquals("Hello Java", getValue(StringValue.class));
    }

    public void testStringArray() {
        Assertions.assertIterableEquals(Lists.newArrayList("a", "b"), (Iterable<?>) getValue(StringVararg.class));
        Assertions.assertIterableEquals(Lists.newArrayList("a", "b"), (Iterable<?>) getValue(StringArray.class));
    }

    public void testIntValue() {
        Assertions.assertEquals(1, getValue(IntValue.class));
    }

    public void testIntArray() {
        Assertions.assertEquals(Lists.newArrayList(1, 2), getValue(IntArray.class));
    }

    private Object getValue(Class<? extends Annotation> ann) {
        final PsiAnnotation annotation = psiClass.getAnnotation(ann.getSimpleName());
        final Map<String, Object> map = resolver.resolve(annotation);
        return map.get("value");
    }

    @Override
    protected String getTestDataPath() {
        return "src/test/java/" + getClass().getPackageName().replace('.', '/');
    }
}
