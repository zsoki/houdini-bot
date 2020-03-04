package hu.zsoki.houdinibot.app

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object QuoteRepository {

    suspend fun persist(keyword: String, quote: String) {
        delay(1000)
    }

}

suspend fun addQuote(keyword: String, quote: String) {
    withContext(Dispatchers.IO) {
        QuoteRepository.persist(keyword, quote)
    }
}