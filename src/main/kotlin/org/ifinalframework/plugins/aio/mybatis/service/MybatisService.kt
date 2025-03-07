package org.ifinalframework.plugins.aio.mybatis.service

import com.intellij.ide.highlighter.JavaFileType
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiClass
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.uast.UClass
import org.jetbrains.uast.toUElement

/**
 * MybatisService
 *
 * @author iimik
 */
@Service(Service.Level.PROJECT)
class MybatisService(
    private val project: Project,
) {

    private val typeHandlerSource = """
        /*
         *    Copyright 2009-2023 the original author or authors.
         *
         *    Licensed under the Apache License, Version 2.0 (the "License");
         *    you may not use this file except in compliance with the License.
         *    You may obtain a copy of the License at
         *
         *       https://www.apache.org/licenses/LICENSE-2.0
         *
         *    Unless required by applicable law or agreed to in writing, software
         *    distributed under the License is distributed on an "AS IS" BASIS,
         *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
         *    See the License for the specific language governing permissions and
         *    limitations under the License.
         */
        package org.apache.ibatis.type;

        import java.sql.CallableStatement;
        import java.sql.PreparedStatement;
        import java.sql.ResultSet;
        import java.sql.SQLException;

        /**
         * @author Clinton Begin
         */
        public interface TypeHandler<T> {

            void setParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException;

            /**
             * Gets the result.
             *
             * @param rs
             *          the rs
             * @param columnName
             *          Column name, when configuration <code>useColumnLabel</code> is <code>false</code>
             *
             * @return the result
             *
             * @throws SQLException
             *           the SQL exception
             */
            T getResult(ResultSet rs, String columnName) throws SQLException;

            T getResult(ResultSet rs, int columnIndex) throws SQLException;

            T getResult(CallableStatement cs, int columnIndex) throws SQLException;

        }
    """.trimIndent()

    private val typeHandler: PsiClass = PsiTreeUtil.findChildOfType(
        PsiFileFactory.getInstance(project).createFileFromText("TypeHandler", JavaFileType.INSTANCE, typeHandlerSource),
        PsiClass::class.java
    )!!

    fun isTypeHandler(clazz: PsiElement): Boolean {
        val uElement = clazz.toUElement() ?: return false
        return if (uElement is UClass) {
            uElement.isInheritor(typeHandler, true)
        } else false
    }

}