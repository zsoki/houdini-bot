package hu.zsoki.houdinibot.app

import com.serebit.strife.bot
import com.serebit.strife.data.Activity
import com.serebit.strife.data.OnlineStatus
import com.serebit.strife.entities.field
import com.serebit.strife.entities.reply
import com.serebit.strife.entities.title
import com.soywiz.klock.minutes
import hu.zsoki.houdinibot.app.Invite.generateInviteUrl
import hu.zsoki.houdinibot.app.command.commandStore
import hu.zsoki.houdinibot.app.db.*
import hu.zsoki.houdinibot.app.domain.truncate
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

private val token = System.getenv("DISCORD_TOKEN") ?: error("You need to define DISCORD_TOKEN environment variable.")

suspend fun main() {
    Database.connect(jdbcUrl, driverClass)

    newSuspendedTransaction {
        SchemaUtils.create(QuoteTable)
        if (QuoteTableDeprecated.exists()) migrateFromDeprecated()
    }

    bot(token) {
        generateInviteUrl()

        scheduledTask("updateUptime", 5.minutes) {
            context.updatePresence(OnlineStatus.ONLINE, Activity.Type.Listening to ";help | ${Uptime.formattedString}")
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
                    val quotes = if (words.isNullOrEmpty()) getAllQuotes() else getAllQuotesFor(words[0])

                    val quotesReply = quotes.joinToString("\n") { quote ->
                        "`#${quote.id} ${quote.keyword}` 📢  ${quote.text.truncate()}"
                    }

                    message.reply(quotesReply)
                }
            }

            command("..") {
                if (extra == null) return@command

                val authorName = message.author?.username ?: "UnknownUser"
                val id = addQuote(authorName, words[0], extra.removePrefix(words[0].trim()))
                message.reply {
                    description = """
                        Quote added successfully.
                        Type `... ${words[0]}` to recall, `-- $id` to remove.
                        """.trimIndent()
                }
            }

            command("...") {
                val quote = getRandomQuote(words[0])
                message.reply("`#${quote.id}` 📢  ${quote.text}")
            }

            command("--") {
                val quote = removeQuote(words[0].toInt())
                message.reply {
                    description = "Removed quote `#${quote.id}` from `${quote.keyword}` pool."
                }
            }
        }
    }
}
