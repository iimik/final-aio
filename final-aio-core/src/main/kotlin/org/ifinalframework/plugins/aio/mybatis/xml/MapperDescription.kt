package org.ifinalframework.plugins.aio.mybatis.xml;

import com.intellij.util.xml.DomFileDescription
import org.ifinalframework.plugins.aio.mybatis.xml.dom.Mapper


/**
 * MapperDescription
 *
 * ```xml
 * <?xml version="1.0" encoding="UTF-8"?>
 * <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
 * <mapper namespace="{MAPPER}">
 *
 * </mapper>
 * ```
 *
 * @author iimik
 * @since 0.0.4
 **/
class MapperDescription : DomFileDescription<Mapper>(Mapper::class.java, "mapper") {

}