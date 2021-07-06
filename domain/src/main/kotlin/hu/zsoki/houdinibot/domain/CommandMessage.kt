package hu.zsoki.houdinibot.domain

class CommandMessage(private val raw: String) {
    val words = raw.split(Regex("""[\s]+"""))

    /**
     * Drop the specified amount of words from the beginning of the raw text, and return the remainder.
     *
     * @param count The number of words you want to drop. If it's greater or equal to the number of words,
     * the method will return an empty string.
     */
    fun dropWords(count: Int): String {
        return if (words.size > count) raw.drop(raw.indexOf(words[count])) else ""
    }
}