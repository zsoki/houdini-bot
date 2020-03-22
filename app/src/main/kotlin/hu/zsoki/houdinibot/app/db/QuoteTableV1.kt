package hu.zsoki.houdinibot.app.db

import hu.zsoki.houdinibot.app.domain.Quote
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

object QuoteTableV1 : Table("quote_v1") {
    val id: Column<Int> = integer("id").autoIncrement()
    val createdAt: Column<LocalDateTime> = datetime("created_at").default(LocalDateTime.now())
    val author: Column<String> = varchar("author", 64)
    val keyword: Column<String> = varchar("keyword", 32)
    val text: Column<String> = text("text")
}

suspend fun addQuote(user: String, keyword: String, text: String): Int = newSuspendedTransaction(Dispatchers.IO) {
    val insertStatement = QuoteTableV1.insert {
        it[this.author] = user
        it[this.keyword] = keyword
        it[this.text] = text
    }
    insertStatement[QuoteTableV1.id]
}

suspend fun removeQuote(id: Int) = newSuspendedTransaction(Dispatchers.IO) {
    val deletedQuote = QuoteTableV1.select { QuoteTableV1.id eq id }.map {quoteRow ->
        Quote(
            id = quoteRow[QuoteTableV1.id],
            author = quoteRow[QuoteTableV1.author],
            keyword = quoteRow[QuoteTableV1.keyword],
            text = quoteRow[QuoteTableV1.text]
        )
    }.first()
    QuoteTableV1.deleteWhere { QuoteTableV1.id eq id }
    deletedQuote
}

suspend fun getRandomQuote(keyword: String): Quote = newSuspendedTransaction(Dispatchers.IO) {
    getAllQuotesForKeywordInternal(keyword).random()
}

suspend fun getAllQuotesFor(keyword: String): List<Quote> = newSuspendedTransaction(Dispatchers.IO) {
    getAllQuotesForKeywordInternal(keyword)
}

suspend fun getAllQuotes(): List<Quote> = newSuspendedTransaction(Dispatchers.IO) {
    QuoteTableV1
        .selectAll()
        .map { quoteRow ->
            Quote(
                id = quoteRow[QuoteTableV1.id],
                author = quoteRow[QuoteTableV1.author],
                keyword = quoteRow[QuoteTableV1.keyword],
                text = quoteRow[QuoteTableV1.text]
            )
        }
}

private fun getAllQuotesForKeywordInternal(keyword: String): List<Quote> {
    return QuoteTableV1
        .select { QuoteTableV1.keyword eq keyword }
        .map { quoteRow ->
            Quote(
                id = quoteRow[QuoteTableV1.id],
                author = quoteRow[QuoteTableV1.author],
                keyword = quoteRow[QuoteTableV1.keyword],
                text = quoteRow[QuoteTableV1.text]
            )
        }
}