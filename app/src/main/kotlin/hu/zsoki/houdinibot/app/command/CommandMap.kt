package hu.zsoki.houdinibot.app.command

private interface CommandMapBuilder {
    val commands: MutableMap<String, CommandFunction>
    fun command(keyword: String, function: CommandFunction)
    fun build(): Map<String, CommandFunction> = commands
}

class SimpleCommandMapBuilder : CommandMapBuilder {
    override val commands = mutableMapOf<String, CommandFunction>()

    fun prefix(prefix: String, function: PrefixedCommandMapBuilder.() -> Unit) {
        commands.putAll(PrefixedCommandMapBuilder(prefix).apply(function).build())
    }

    override fun command(keyword: String, function: CommandFunction) {
        commands.putIfAbsent(keyword, function)
    }
}

class PrefixedCommandMapBuilder(private val prefix: String) : CommandMapBuilder {
    override val commands = mutableMapOf<String, CommandFunction>()

    override fun command(keyword: String, function: CommandFunction) {
        val concatKeyword = prefix + keyword
        commands.putIfAbsent(concatKeyword, function)
    }
}