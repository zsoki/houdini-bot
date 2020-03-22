package hu.zsoki.houdinibot.app

import com.serebit.strife.bot
import com.serebit.strife.data.Activity
import com.serebit.strife.data.OnlineStatus
import com.serebit.strife.entities.field
import com.serebit.strife.entities.reply
import com.serebit.strife.entities.title
import com.serebit.strife.onMessageCreate
import com.soywiz.klock.minutes
import hu.zsoki.houdinibot.app.db.*
import hu.zsoki.houdinibot.app.domain.truncate
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

private val token = System.getenv("DISCORD_TOKEN") ?: error("You need to define DISCORD_TOKEN environment variable.")
private val clientId = System.getenv("CLIENT_ID") ?: error("You need to define CLIENT_ID environment variable.")

private val inviteUrl = "https://discordapp.com/oauth2/authorize?scope=bot&client_id=$clientId&permissions=2048"

suspend fun main() {
    Database.connect(jdbcUrl, driverClass)

    newSuspendedTransaction {
        SchemaUtils.create(QuoteTableV1)
        if (QuoteTableV0.exists()) migrateFromV0()
    }

    bot(token) {
        scheduledTask("updateUptime", 5.minutes) {
            context.updatePresence(OnlineStatus.ONLINE, Activity.Type.Playing to buildUptimeString())
        }

        onMessageCreate {
            command(";", "help") {
                message.reply {
                    title("Commands")
                    field("`;invite`") { "Posts the invite URL" }
                    field("`.. <keyword> text to store as quote`") { "Store text of any length as a quote for <keyword>. The <keyword> doesn't need to exist. If you add it to an already existing one, the quote will be chosen randomly when recalling." }
                    field("`... <keyword>`") { "Recall quote for <keyword>" }
                    field("`-- <quote-id>`") { "Remove quote identified by the <quote-id>. You can get the ID when recalling a quote. E.g.: `#123`" }
                    field("`;quotes <keyword (optional)>`") { "List quotes - if no <keyword> is provided, then list all quotes stored." }
                }
            }
            command(";", "invite") {
                message.reply(inviteUrl)
            }
            command(".. ") {
                val authorName = message.author?.username ?: "UnknownUser"
                val id = addQuote(authorName, keyword, extra)
                message.reply {
                    title("$authorName added a new quote")
                    description = "Type `... $keyword` to recall, `-- $id` to remove"
                }
            }
            command("... ") {
                val quote = getRandomQuote(keyword)
                message.reply("`#${quote.id}` 📢  ${quote.text}")
            }
            command("-- ") {
                val quote = removeQuote(keyword.toInt())
                message.reply {
                    description = "Removed quote `#${quote.id}` from `${quote.keyword}`"
                }
            }
            command(";", "quotes") {
                val quotes = if (params.isNullOrEmpty()) getAllQuotes() else getAllQuotesFor(params[0])

                val quotesReply = quotes.joinToString("\n") { quote ->
                    "`#${quote.id} ${quote.keyword}` 📢  ${quote.text.truncate()}"
                }

                message.reply(quotesReply)
            }
        }
    }
}
