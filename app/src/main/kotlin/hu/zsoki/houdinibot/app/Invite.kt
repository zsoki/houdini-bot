package hu.zsoki.houdinibot.app

import com.serebit.strife.BotBuilder
import com.serebit.strife.BotBuilderDsl
import com.serebit.strife.onReady

object Invite {

    private const val inviteTemplate: String = "https://discordapp.com/oauth2/authorize?scope=bot&client_id=%s&permissions=2048"

    private lateinit var clientId: String

    val url: String by lazy {
        inviteTemplate.format(clientId)
    }

    @BotBuilderDsl
    fun BotBuilder.generateInviteUrl() {
        onReady {
            clientId = context.selfUser.id.toString()
        }
    }
}