package hu.zsoki.houdinibot.app

import com.serebit.strife.BotBuilderDsl
import com.serebit.strife.events.MessageCreateEvent

@BotBuilderDsl
suspend fun MessageCreateEvent.command(prefix: String, keyword: String? = null, function: suspend CommandBuilder.() -> Unit) {
    val content = message.content
    val keywordLowerCase = keyword?.toLowerCase()

    if (!content.startsWith(prefix)) return

    val words = content.drop(prefix.length).trim().toLowerCase().split(" ")

    if (keywordLowerCase != null && (words.isEmpty() || words.first() != keywordLowerCase)) return

    function.invoke(
        CommandBuilder(
            event = this,
            prefix = prefix,
            keyword = keywordLowerCase ?: words.first(),
            params = words.drop(1)
        )
    )
}

class CommandBuilder(
    event: MessageCreateEvent,
    val prefix: String,
    val keyword: String,
    val params: List<String> = listOf()
) {
    val extra = event.message.content.removePrefix("$prefix$keyword ")
}
