package hu.zsoki.houdinibot.app.db

import hu.zsoki.houdinibot.domain.Quote
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

object QuoteTable : Table("quote") {
    private val id: Column<Int> = integer("id").autoIncrement()
    val createdAt: Column<LocalDateTime> = datetime("created_at").default(LocalDateTime.now())
    val author: Column<String> = varchar("author", 64)
    val keyword: Column<String> = varchar("keyword", 32)
    val text: Column<String> = text("text")

    override val primaryKey = PrimaryKey(id)

    suspend fun addQuote(user: String, keyword: String, text: String): Int = newSuspendedTransaction(Dispatchers.IO) {
        val insertStatement = insert {
            it[this.author] = user
            it[this.keyword] = keyword
            it[this.text] = text
        }
        insertStatement[QuoteTable.id]
    }

    suspend fun removeQuote(id: Int) = newSuspendedTransaction(Dispatchers.IO) {
        val deletedQuote = QuoteTable.select { QuoteTable.id eq id }.map { quoteRow ->
            Quote(
                id = quoteRow[QuoteTable.id],
                author = quoteRow[QuoteTable.author],
                keyword = quoteRow[QuoteTable.keyword],
                text = quoteRow[QuoteTable.text]
            )
        }.first()
        QuoteTable.deleteWhere { QuoteTable.id eq id }
        deletedQuote
    }

    suspend fun getRandomQuote(keyword: String): Quote = newSuspendedTransaction(Dispatchers.IO) {
        getAllQuotesForKeywordInternal(keyword).random()
    }

    suspend fun getAllQuotesFor(keyword: String): List<Quote> = newSuspendedTransaction(Dispatchers.IO) {
        getAllQuotesForKeywordInternal(keyword)
    }

    suspend fun getAllQuotes(): List<Quote> = newSuspendedTransaction(Dispatchers.IO) {
        QuoteTable
            .selectAll()
            .map { quoteRow ->
                Quote(
                    id = quoteRow[QuoteTable.id],
                    author = quoteRow[QuoteTable.author],
                    keyword = quoteRow[QuoteTable.keyword],
                    text = quoteRow[QuoteTable.text]
                )
            }
    }

    private fun getAllQuotesForKeywordInternal(keyword: String): List<Quote> {
        return QuoteTable
            .select { QuoteTable.keyword eq keyword }
            .map { quoteRow ->
                Quote(
                    id = quoteRow[QuoteTable.id],
                    author = quoteRow[QuoteTable.author],
                    keyword = quoteRow[QuoteTable.keyword],
                    text = quoteRow[QuoteTable.text]
                )
            }
    }
}
