package hu.zsoki.houdinibot.app

object Invite {

    private const val inviteTemplate: String = "https://discordapp.com/oauth2/authorize?scope=bot&client_id=%s&permissions=2048"

    fun getUrl(clientId: String): String {
        return inviteTemplate.format(clientId)
    }

}