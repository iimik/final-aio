package org.ifinalframework.plugins.aio.mybatis.service

import org.ifinalframework.plugins.aio.mybatis.MybatisMarker
import java.util.function.Function


/**
 * MapperLineMarkerService
 *
 * @author iimik
 * @since 0.0.4
 * @see [JvmMapperLineMarkerService]
 **/
interface MapperLineMarkerService<T : Any> : Function<T, MybatisMarker?> {
}