package hu.zsoki.houdinibot.app.db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.datetime
import java.time.LocalDateTime

object QuoteTableV0 : Table("quote") {
    val id: Column<Int> = integer("id").autoIncrement()
    val timestamp: Column<LocalDateTime> = datetime("timestamp").default(LocalDateTime.now())
    val keyword: Column<String> = varchar("keyword", 32)
    val quote: Column<String> = text("quote")
}

fun migrateFromV0() {
    try {
        val selectAllQuery = QuoteTableV0.selectAll()
        QuoteTableV1.batchInsert(selectAllQuery) { deprecatedRow ->
            this[QuoteTableV1.id] = deprecatedRow[QuoteTableV0.id]
            this[QuoteTableV1.createdAt] = deprecatedRow[QuoteTableV0.timestamp]
            this[QuoteTableV1.keyword] = deprecatedRow[QuoteTableV0.keyword]
            this[QuoteTableV1.text] = deprecatedRow[QuoteTableV0.quote]
            this[QuoteTableV1.author] = "Unknown"
        }
        SchemaUtils.drop(QuoteTableV0)
    } catch(ex: Throwable) {
        println("Exception during migration: ${ex.message}")
    } finally {
        println("Migration from QuoteTable_v0 to QuoteTable_v1 finished.")
    }
}
