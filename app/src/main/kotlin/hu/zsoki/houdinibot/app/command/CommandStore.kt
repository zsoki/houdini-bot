package hu.zsoki.houdinibot.app.command

import com.serebit.strife.BotBuilder
import com.serebit.strife.BotBuilderDsl
import com.serebit.strife.onMessageCreate

private object CommandStore {
    val commandFunctions = mutableMapOf<String, CommandFunction>()

    fun addCommandFunctions(commandMap: Map<String, CommandFunction>) {
        commandFunctions.putAll(commandMap)
    }
}

@BotBuilderDsl
fun BotBuilder.commandStore(function: SimpleCommandMapBuilder.() -> Unit) {
    CommandStore.addCommandFunctions(SimpleCommandMapBuilder().apply(function).build())
    onMessageCreate {
        val keyword = message.content.trim().toLowerCase().split(" ").first()
        val commandFunction = CommandStore.commandFunctions[keyword] ?: return@onMessageCreate
        commandFunction.invoke(CommandFunctionBuilder(this, keyword))
    }
}