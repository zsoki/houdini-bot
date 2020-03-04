package hu.zsoki.houdinibot.app

import com.serebit.strife.bot
import com.serebit.strife.entities.reply
import com.serebit.strife.onMessageCreate
import com.soywiz.klock.DateTime

private val token = System.getenv("DISCORD_TOKEN") ?: error("You need to define DISCORD_TOKEN environment variable.")
private val clientId = System.getenv("CLIENT_ID") ?: error("You need to define CLIENT_ID environment variable.")
private val inviteUrl = "https://discordapp.com/oauth2/authorize?scope=bot&client_id=$clientId&permissions=2048"

suspend fun main() {
    bot(token) {
        onMessageCreate {
            command(";","ping") {
                val ping = DateTime.now() - message.createdAt
                message.reply("Pong! - $ping")
            }
            command(";","invite") {
                message.reply(inviteUrl)
            }
            command("..") {
                addQuote(keyword, extra)
                message.reply("Mock addQuote() process finished: \"$keyword\" to \"$extra\"")
            }
        }
    }
}
