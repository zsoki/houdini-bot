package hu.zsoki.houdinibot.app.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object QuoteTableDeprecated : Table("quote_v1") {
    val id: Column<Int> = integer("id").autoIncrement()
    val createdAt: Column<LocalDateTime> = datetime("created_at").default(LocalDateTime.now())
    val author: Column<String> = varchar("author", 64)
    val keyword: Column<String> = varchar("keyword", 32)
    val text: Column<String> = text("text")
}

fun migrateFromDeprecated() {
    try {
        val selectAllQuery = QuoteTableDeprecated.selectAll()
        QuoteTable.batchInsert(selectAllQuery) { deprecatedRow ->
            this[QuoteTable.createdAt] = deprecatedRow[QuoteTableDeprecated.createdAt]
            this[QuoteTable.author] = deprecatedRow[QuoteTableDeprecated.author]
            this[QuoteTable.keyword] = deprecatedRow[QuoteTableDeprecated.keyword]
            this[QuoteTable.text] = deprecatedRow[QuoteTableDeprecated.text]
        }
        SchemaUtils.drop(QuoteTableDeprecated)
    } catch(ex: Throwable) {
        println("Exception during migration: ${ex.message}")
    } finally {
        println("Migration finished.")
    }
}
