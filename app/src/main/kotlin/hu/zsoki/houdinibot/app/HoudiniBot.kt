package hu.zsoki.houdinibot.app

import dev.kord.core.Kord
import dev.kord.core.behavior.reply
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import hu.zsoki.houdinibot.app.db.Db.driverClass
import hu.zsoki.houdinibot.app.db.Db.jdbcUrl
import hu.zsoki.houdinibot.app.db.QuoteTable
import hu.zsoki.houdinibot.app.domain.truncate
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.sql.Connection

private val token = System.getProperty("discord.token") ?: error("You need to define discord.token property.")

suspend fun main() {
    Database.connect(jdbcUrl, driverClass)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    newSuspendedTransaction {
        SchemaUtils.create(QuoteTable)
    }

    val client = Kord(token)

    client.on<MessageCreateEvent> {
        when (message.content) {
            ";invite" -> message.reply { content = Invite.getUrl(client.selfId.asString) }
            ";quotes" -> {
                val quotes = QuoteTable.getAllQuotes()

                val quotesReply = quotes.joinToString("\n") { quote ->
                    "`#${quote.id} ${quote.keyword}` 📢  ${quote.text.truncate()}"
                }

                message.reply { content = quotesReply.ifBlank { "No quotes yet." } }
            }
            ";help" -> message.reply { content = "No help ready. Currently migrating to Kord." }
        }
    }

    client.login()

//    bot(token) {
//        generateInviteUrl()
//
//        scheduledTask("updateUptime", Duration.ofMinutes(5)) {
//            context.updatePresence(OnlineStatus.ONLINE, Activity.Type.Playing to ";help | ${Uptime.formattedString}")
//        }
//
//        commandStore {
//            prefix(";") {
//                command("help") {
//                    message.reply {
//                        title("Commands")
//                        field("`;invite`") { "Posts the invite URL" }
//                        field("`.. <keyword> text to store as quote`") { "Store text of any length as a quote for <keyword>. The <keyword> doesn't need to exist. If you add it to an already existing one, the quote will be chosen randomly when recalling." }
//                        field("`... <keyword>`") { "Recall quote for <keyword>" }
//                        field("`-- <quote-id>`") { "Remove quote identified by the <quote-id>. You can get the ID when recalling a quote. E.g.: `#123`" }
//                        field("`;quotes <keyword (optional)>`") { "List quotes - if no <keyword> is provided, then list all quotes stored." }
//                    }
//                }
//            }
//
//            command("..") {
//                if (extra == null) return@command
//
//                val authorName = message.getAuthor()?.getUsername() ?: "UnknownUser"
//                val id = QuoteTable.addQuote(authorName, words[0], extra.removePrefix(words[0].trim()))
//                message.reply {
//                    description = """
//                        Quote added successfully.
//                        Type `... ${words[0]}` to recall, `-- $id` to remove.
//                        """.trimIndent()
//                }
//            }
//
//            command("...") {
//                val quote = QuoteTable.getRandomQuote(words[0])
//                message.reply("`#${quote.id}` 📢  ${quote.text}")
//            }
//
//            command("--") {
//                val quote = QuoteTable.removeQuote(words[0].toInt())
//                message.reply {
//                    description = "Removed quote `#${quote.id}` from `${quote.keyword}` pool."
//                }
//            }
//        }
//    }
}
