package hu.zsoki.houdinibot.app

import com.serebit.strife.bot
import com.serebit.strife.data.Activity
import com.serebit.strife.data.OnlineStatus
import com.serebit.strife.entities.field
import com.serebit.strife.entities.reply
import com.serebit.strife.entities.title
import hu.zsoki.houdinibot.app.Invite.generateInviteUrl
import hu.zsoki.houdinibot.app.command.commandStore
import hu.zsoki.houdinibot.app.db.*
import hu.zsoki.houdinibot.app.db.Db.driverClass
import hu.zsoki.houdinibot.app.db.Db.jdbcUrl
import hu.zsoki.houdinibot.app.domain.truncate
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection
import java.time.Duration

private val token = System.getProperty("discord.token") ?: error("You need to define discord.token property.")

suspend fun main() {
    Database.connect(jdbcUrl, driverClass)
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE

    bot(token) {
        generateInviteUrl()

        scheduledTask("updateUptime", Duration.ofMinutes(5)) {
            context.updatePresence(OnlineStatus.ONLINE, Activity.Type.Playing to ";help | ${Uptime.formattedString}")
        }

        commandStore {
            prefix(";") {
                command("help") {
                    message.reply {
                        title("Commands")
                        field("`;invite`") { "Posts the invite URL" }
                        field("`.. <keyword> text to store as quote`") { "Store text of any length as a quote for <keyword>. The <keyword> doesn't need to exist. If you add it to an already existing one, the quote will be chosen randomly when recalling." }
                        field("`... <keyword>`") { "Recall quote for <keyword>" }
                        field("`-- <quote-id>`") { "Remove quote identified by the <quote-id>. You can get the ID when recalling a quote. E.g.: `#123`" }
                        field("`;quotes <keyword (optional)>`") { "List quotes - if no <keyword> is provided, then list all quotes stored." }
                    }
                }

                command("invite") {
                    message.reply(Invite.url)
                }

                command("quotes") {
                    val quotes = if (words.isNullOrEmpty()) QuoteTable.getAllQuotes() else QuoteTable.getAllQuotesFor(words[0])

                    val quotesReply = quotes.joinToString("\n") { quote ->
                        "`#${quote.id} ${quote.keyword}` 📢  ${quote.text.truncate()}"
                    }

                    message.reply(quotesReply)
                }
            }

            command("..") {
                if (extra == null) return@command

                val authorName = message.getAuthor()?.getUsername() ?: "UnknownUser"
                val id = QuoteTable.addQuote(authorName, words[0], extra.removePrefix(words[0].trim()))
                message.reply {
                    description = """
                        Quote added successfully.
                        Type `... ${words[0]}` to recall, `-- $id` to remove.
                        """.trimIndent()
                }
            }

            command("...") {
                val quote = QuoteTable.getRandomQuote(words[0])
                message.reply("`#${quote.id}` 📢  ${quote.text}")
            }

            command("--") {
                val quote = QuoteTable.removeQuote(words[0].toInt())
                message.reply {
                    description = "Removed quote `#${quote.id}` from `${quote.keyword}` pool."
                }
            }
        }
    }
}
