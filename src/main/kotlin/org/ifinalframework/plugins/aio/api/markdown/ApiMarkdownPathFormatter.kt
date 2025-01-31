package org.ifinalframework.plugins.aio.api.markdown

import org.ifinalframework.plugins.aio.api.model.ApiMarker


/**
 * MarkdownService
 *
 * @author iimik
 * @since 0.0.1
 **/
@FunctionalInterface
interface ApiMarkdownPathFormatter {
    fun format(apiMarker: ApiMarker): String
}