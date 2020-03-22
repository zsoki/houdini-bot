package hu.zsoki.houdinibot.app.domain

data class Quote(
    val id: Int,
    val author: String,
    val keyword: String,
    val text: String
)

fun String.truncate(maxLength: Int = 256): String {
    return when {
        length > maxLength -> take(maxLength - 4) + "..."
        else -> this
    }
}
