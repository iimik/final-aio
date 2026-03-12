package org.ifinalframework.plugins.aio.mybatis.xml.dom

import com.intellij.psi.PsiField
import com.intellij.util.xml.Attribute
import com.intellij.util.xml.GenericAttributeValue


/**
 * Insert
 *
 * ```dtd
 * <!ELEMENT insert (#PCDATA | selectKey | include | trim | where | set | foreach | choose | if | bind)*>
 * <!ATTLIST insert
 * id CDATA #REQUIRED
 * parameterMap CDATA #IMPLIED
 * parameterType CDATA #IMPLIED
 * timeout CDATA #IMPLIED
 * flushCache (true|false) #IMPLIED
 * statementType (STATEMENT|PREPARED|CALLABLE) #IMPLIED
 * keyProperty CDATA #IMPLIED
 * useGeneratedKeys (true|false) #IMPLIED
 * keyColumn CDATA #IMPLIED
 * databaseId CDATA #IMPLIED
 * lang CDATA #IMPLIED
 * >
 * ```
 *
 * @author iimik
 * @since 0.0.4
 **/
interface Insert:Statement {

    @Attribute("useGeneratedKeys")
    fun getUseGeneratedKeys():GenericAttributeValue<Boolean>

    @Attribute("keyProperty")
    fun getKeyProperty(): GenericAttributeValue<String>

    @Attribute("keyColumn")
    fun getKeyColumn():GenericAttributeValue<String>
}