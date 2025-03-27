package org.ifinalframework.plugins.aio.setting

import com.intellij.util.xmlb.annotations.Tag
import org.ifinalframework.plugins.aio.mybatis.MyBatisProperties

/**
 * FinalProperties
 *
 * @author iimik
 */
data class FinalProperties(
    @Tag("mybatis")
    @JvmField
    var mybatis: MyBatisProperties = MyBatisProperties(),
) {
}