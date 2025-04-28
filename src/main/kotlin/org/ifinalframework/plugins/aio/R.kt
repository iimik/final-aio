package org.ifinalframework.plugins.aio

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.ReadAction
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.project.Project
import java.util.concurrent.CancellationException


/**
 * R
 *
 * @author iimik
 * @since 0.0.1
 **/

object R {

    fun async(action: () -> Unit) {
        ApplicationManager.getApplication().executeOnPooledThread(action)
    }

    fun <T> computeInRead(action: () -> T): T? {
        return try {
            ReadAction.compute<T, Throwable>(action)
        } catch (_: CancellationException) {
            null
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun runInRead(action: () -> Unit) {
        try {
            ReadAction.run<Throwable>(action)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun <T> computeInWrite(action: () -> T): T? {
        try {
            return WriteAction.compute<T, Throwable>(action)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun runInWrite(action: () -> Unit) {
        try {
            WriteAction.run<Throwable>(action)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    fun runInWriteAction(project: Project, action: () -> Unit) {
        WriteCommandAction.runWriteCommandAction(project, action)
    }

    fun dispatch(action: () -> Unit) {
        try {
            ApplicationManager.getApplication().invokeLater(action)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}