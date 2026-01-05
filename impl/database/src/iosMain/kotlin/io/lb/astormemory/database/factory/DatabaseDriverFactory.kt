package io.lb.astormemory.database.factory

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import io.lb.astormemory.database.AstorMemoryDatabase

actual class DatabaseDriverFactory {
    actual fun create(): SqlDriver {
        return NativeSqliteDriver(
            AstorMemoryDatabase.Schema,
            "astormemory.db"
        )
    }
}