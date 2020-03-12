package hu.zsoki.houdinibot.app

import com.serebit.strife.bot
import com.serebit.strife.entities.reply
import com.serebit.strife.onMessageCreate
import com.soywiz.klock.DateTime
import hu.zsoki.houdinibot.app.db.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

private val token = System.getenv("DISCORD_TOKEN") ?: error("You need to define DISCORD_TOKEN environment variable.")
private val clientId = System.getenv("CLIENT_ID") ?: error("You need to define CLIENT_ID environment variable.")

private val databaseUrl =
    System.getenv("DATABASE_URL") ?: error("You need to define DATABASE_URL environment variable.")
private const val driverClass = "org.postgresql.Driver"

private val inviteUrl = "https://discordapp.com/oauth2/authorize?scope=bot&client_id=$clientId&permissions=2048"

suspend fun main() {
    Database.connect(databaseUrl, driverClass)

    newSuspendedTransaction {
        SchemaUtils.create(QuoteTable)
    }

    bot(token) {
        onMessageCreate {
            command(";", "ping") {
                val ping = DateTime.now() - message.createdAt
                message.reply("Pong! - $ping")
            }
            command(";", "invite") {
                message.reply(inviteUrl)
            }
            command(".. ") {
                addQuote(keyword, extra)
                message.reply("Added new quote for $keyword.")
            }
            command("... ") {
                val quote = getRandomQuote(keyword)
                message.reply("`#${quote.id}` 💬 ${quote.quote}")
            }
            command("-- ") {
                removeQuote(keyword.toInt())
                message.reply("Deleted quote `#$keyword`")
            }
            command(";", "quotes") {
                val keyword = params[0]
                val quotesReply = getAllQuotes(keyword).joinToString("\n") { quote ->
                    val truncQuote = if (quote.quote.length > 64) quote.quote.take(64) + "..." else quote.quote
                    "`#${quote.id}` - $truncQuote"
                }
                message.reply(quotesReply)
            }
        }
    }
}
