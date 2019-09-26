/*
 * Copyright 2018-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.liuwj.ktorm.dsl

import java.sql.ResultSetMetaData
import java.sql.ResultSetMetaData.columnNullableUnknown
import java.sql.SQLException
import java.sql.Types

/**
 * Created by vince on Sep 03, 2019.
 */
internal class QueryRowSetMetadata(metadata: ResultSetMetaData) : ResultSetMetaData {
    private val columns = Array(metadata.columnCount) { index ->
        val i = index + 1

        @Suppress("SwallowedException")
        ColumnInfo(
            autoIncrement = try { metadata.isAutoIncrement(i) } catch (_: Throwable) { false },
            caseSensitive = try { metadata.isCaseSensitive(i) } catch (_: Throwable) { false },
            searchable = try { metadata.isSearchable(i) } catch (_: Throwable) { false },
            currency = try { metadata.isCurrency(i) } catch (_: Throwable) { false },
            nullable = try { metadata.isNullable(i) } catch (_: Throwable) { columnNullableUnknown },
            signed = try { metadata.isSigned(i) } catch (_: Throwable) { true },
            columnDisplaySize = try { metadata.getColumnDisplaySize(i) } catch (_: Throwable) { 0 },
            columnLabel = try { metadata.getColumnLabel(i).orEmpty() } catch (_: Throwable) { "" },
            columnName = try { metadata.getColumnName(i).orEmpty() } catch (_: Throwable) { "" },
            schemaName = try { metadata.getSchemaName(i).orEmpty() } catch (_: Throwable) { "" },
            precision = try { metadata.getPrecision(i) } catch (_: Throwable) { 0 },
            scale = try { metadata.getScale(i) } catch (_: Throwable) { 0 },
            tableName = try { metadata.getTableName(i).orEmpty() } catch (_: Throwable) { "" },
            catalogName = try { metadata.getCatalogName(i).orEmpty() } catch (_: Throwable) { "" },
            columnType = try { metadata.getColumnType(i) } catch (_: Throwable) { Types.VARCHAR },
            columnTypeName = try { metadata.getColumnTypeName(i).orEmpty() } catch (_: Throwable) { "" },
            columnClassName = try { metadata.getColumnClassName(i).orEmpty() } catch (_: Throwable) { "" },
            readonly = true,
            writable = false,
            definitelyWritable = false
        )
    }

    private operator fun get(col: Int): ColumnInfo {
        if (col <= 0 || col > columns.size) {
            throw SQLException("Invalid column index: $col")
        }

        return columns[col - 1]
    }

    private data class ColumnInfo(
        val autoIncrement: Boolean,
        val caseSensitive: Boolean,
        val searchable: Boolean,
        val currency: Boolean,
        val nullable: Int,
        val signed: Boolean,
        val columnDisplaySize: Int,
        val columnLabel: String,
        val columnName: String,
        val schemaName: String,
        val precision: Int,
        val scale: Int,
        val tableName: String,
        val catalogName: String,
        val columnType: Int,
        val columnTypeName: String,
        val columnClassName: String,
        val readonly: Boolean,
        val writable: Boolean,
        val definitelyWritable: Boolean
    )

    override fun getColumnCount(): Int {
        return columns.size
    }

    override fun isAutoIncrement(columnIndex: Int): Boolean {
        return this[columnIndex].autoIncrement
    }

    override fun isCaseSensitive(columnIndex: Int): Boolean {
        return this[columnIndex].caseSensitive
    }

    override fun isSearchable(columnIndex: Int): Boolean {
        return this[columnIndex].searchable
    }

    override fun isCurrency(columnIndex: Int): Boolean {
        return this[columnIndex].currency
    }

    override fun isNullable(columnIndex: Int): Int {
        return this[columnIndex].nullable
    }

    override fun isSigned(columnIndex: Int): Boolean {
        return this[columnIndex].signed
    }

    override fun getColumnDisplaySize(columnIndex: Int): Int {
        return this[columnIndex].columnDisplaySize
    }

    override fun getColumnLabel(columnIndex: Int): String {
        return this[columnIndex].columnLabel
    }

    override fun getColumnName(columnIndex: Int): String {
        return this[columnIndex].columnName
    }

    override fun getSchemaName(columnIndex: Int): String {
        return this[columnIndex].schemaName
    }

    override fun getPrecision(columnIndex: Int): Int {
        return this[columnIndex].precision
    }

    override fun getScale(columnIndex: Int): Int {
        return this[columnIndex].scale
    }

    override fun getTableName(columnIndex: Int): String {
        return this[columnIndex].tableName
    }

    override fun getCatalogName(columnIndex: Int): String {
        return this[columnIndex].catalogName
    }

    override fun getColumnType(columnIndex: Int): Int {
        return this[columnIndex].columnType
    }

    override fun getColumnTypeName(columnIndex: Int): String {
        return this[columnIndex].columnTypeName
    }

    override fun isReadOnly(columnIndex: Int): Boolean {
        return this[columnIndex].readonly
    }

    override fun isWritable(columnIndex: Int): Boolean {
        return this[columnIndex].writable
    }

    override fun isDefinitelyWritable(columnIndex: Int): Boolean {
        return this[columnIndex].definitelyWritable
    }

    override fun getColumnClassName(columnIndex: Int): String {
        return this[columnIndex].columnClassName
    }

    override fun <T : Any> unwrap(iface: Class<T>): T {
        return iface.cast(this)
    }

    override fun isWrapperFor(iface: Class<*>): Boolean {
        return iface.isInstance(this)
    }
}
