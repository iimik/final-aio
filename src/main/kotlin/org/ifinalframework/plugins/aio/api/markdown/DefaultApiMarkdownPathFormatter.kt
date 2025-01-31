package org.ifinalframework.plugins.aio.api.markdown;

import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.springframework.stereotype.Component


/**
 * DefaultApiMarkdownPathFormatter
 *
 * @author iimik
 * @since 0.0.2
 **/
@Component
class DefaultApiMarkdownPathFormatter : ApiMarkdownPathFormatter {
    override fun format(apiMarker: ApiMarker): String {
        return "docs/api/${apiMarker.category.replace("-", "/")}/${apiMarker.name}.md"
    }
}