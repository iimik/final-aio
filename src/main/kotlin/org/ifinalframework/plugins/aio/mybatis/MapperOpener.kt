package org.ifinalframework.plugins.aio.mybatis

import org.ifinalframework.plugins.aio.application.annotation.ImplementedBy


/**
 * MapperOpener
 *
 * @author iimik
 * @since 0.0.4
 **/
@ImplementedBy(DefaultMapperOpener::class)
interface MapperOpener {
    fun open(marker: MybatisMarker)
}