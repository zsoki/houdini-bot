package hu.zsoki.houdinibot.app.command

import com.serebit.strife.entities.Message
import com.serebit.strife.events.MessageCreateEvent
import com.serebit.strife.events.SingleMessageEvent
import kotlinx.coroutines.runBlocking

typealias CommandFunction = suspend CommandFunctionBuilder.() -> Unit

class CommandFunctionBuilder(
    event: MessageCreateEvent,
    keyword: String
) : SingleMessageEvent by event {

    override val message: Message = event.message

    val extra: String?
    val words: List<String>

    init {
        val extraTrimmed = runBlocking { message.getContent().removePrefix(keyword).trim() }
        extra = if (extraTrimmed.isBlank()) null else extraTrimmed
        words = extra?.toLowerCase()?.split(" ") ?: listOf()
    }
}
