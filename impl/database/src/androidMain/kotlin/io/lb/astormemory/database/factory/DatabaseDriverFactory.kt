package io.lb.astormemory.database.factory

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import io.lb.astormemory.database.AstorMemoryDatabase

actual class DatabaseDriverFactory(
    private val context: Context
) {
    actual fun create(): SqlDriver {
        return AndroidSqliteDriver(
            AstorMemoryDatabase.Schema,
            context,
            "astormemory.db"
        )
    }
}