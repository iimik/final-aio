package org.ifinalframework.plugins.aio.api.service

import com.intellij.openapi.module.Module
import org.ifinalframework.plugins.aio.api.model.ApiMarker
import org.intellij.plugins.markdown.lang.psi.MarkdownPsiElement


/**
 * MarkdownService
 *
 * @author iimik
 * @since 0.0.6
 **/
interface MarkdownService {
    fun findMarkdownFile(module: Module, marker: ApiMarker): MarkdownPsiElement?
}