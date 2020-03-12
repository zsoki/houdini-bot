package hu.zsoki.houdinibot.app.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

object QuoteTable : Table() {
    val id: Column<Int> = integer("id").autoIncrement()
    val timestamp: Column<LocalDateTime> = datetime("timestamp").default(LocalDateTime.now())
    val keyword: Column<String> = varchar("keyword", 32)
    val quote: Column<String> = text("quote")
}

data class Quote(
    val id: Int,
    val keyword: String,
    val quote: String
)

suspend fun addQuote(keyword: String, quote: String) = newSuspendedTransaction(Dispatchers.IO) {
    QuoteTable.insert { insertStatement ->
        insertStatement[this.keyword] = keyword
        insertStatement[this.quote] = quote
    }
}

suspend fun removeQuote(id: Int) = newSuspendedTransaction(Dispatchers.IO) {
    QuoteTable.deleteWhere { QuoteTable.id eq id }
}

suspend fun getRandomQuote(keyword: String): Quote = newSuspendedTransaction(Dispatchers.IO) {
    getAllQuotesInternal(keyword).random()
}

suspend fun getAllQuotes(keyword: String): List<Quote> = newSuspendedTransaction(Dispatchers.IO) {
    getAllQuotesInternal(keyword)
}

private fun getAllQuotesInternal(keyword: String): List<Quote> {
    return QuoteTable
        .select { QuoteTable.keyword eq keyword }
        .map { quoteRow ->
            Quote(
                id = quoteRow[QuoteTable.id],
                keyword = quoteRow[QuoteTable.keyword],
                quote = quoteRow[QuoteTable.quote]
            )
        }
}