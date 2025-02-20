package org.ifinalframework.plugins.aio.mybatis.xml;

import com.intellij.util.xml.DomFileDescription
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper


/**
 * MapperDescription
 *
 * @author iimik
 * @since 0.0.4
 **/
class MapperDescription : DomFileDescription<Mapper>(Mapper::class.java, "mapper") {

}