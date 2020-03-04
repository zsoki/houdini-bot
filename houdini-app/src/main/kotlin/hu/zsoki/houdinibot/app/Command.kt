package hu.zsoki.houdinibot.app

import com.serebit.strife.BotBuilderDsl
import com.serebit.strife.events.MessageCreateEvent

@BotBuilderDsl
suspend fun MessageCreateEvent.command(prefix: String, keyword: String? = null, function: suspend CommandBuilder.() -> Unit) {
    val content = message.content

    if (!content.startsWith(prefix)) return

    val words = content.drop(prefix.length).trim().split(" ")

    if (keyword != null && (words.isEmpty() || words.first() != keyword)) return

    function.invoke(
        CommandBuilder(
            event = this,
            prefix = prefix,
            keyword = keyword ?: words.first(),
            params = words.drop(1)
        )
    )
}

class CommandBuilder(
    private val event: MessageCreateEvent,
    val prefix: String,
    val keyword: String,
    val params: List<String>
) {
    val extra = event.message.content.removePrefix("$prefix $keyword ")
}
