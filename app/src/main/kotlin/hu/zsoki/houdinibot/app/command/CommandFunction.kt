package hu.zsoki.houdinibot.app.command

import com.serebit.strife.entities.Message
import com.serebit.strife.events.MessageCreateEvent
import com.serebit.strife.events.SingleMessageEvent

typealias CommandFunction = suspend CommandFunctionBuilder.() -> Unit

class CommandFunctionBuilder(
    event: MessageCreateEvent,
    keyword: String
) : SingleMessageEvent by event {

    override val message: Message = event.message

    val extra: String?
    val words: List<String>

    init {
        val extraTrimmed = message.content.removePrefix(keyword).trim()
        extra = if (extraTrimmed.isBlank()) null else extraTrimmed
        words = extra?.toLowerCase()?.split(" ") ?: listOf()
    }
}
