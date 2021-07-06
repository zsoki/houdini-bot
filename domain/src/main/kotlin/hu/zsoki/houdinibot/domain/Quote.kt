package hu.zsoki.houdinibot.domain

data class Quote(
    val id: Int,
    val author: String,
    val keyword: String,
    val text: String
) {
    fun truncated(maxLength: Int = 256): String {
        return when {
            text.length > maxLength -> text.take(maxLength - 4) + "..."
            else -> text
        }
    }
}
