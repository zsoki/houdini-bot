package hu.zsoki.houdinibot.app

import com.serebit.strife.BotBuilder
import com.serebit.strife.BotBuilderDsl
import com.serebit.strife.events.ReadyEvent
import com.serebit.strife.onReady
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.minutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

@BotBuilderDsl
fun BotBuilder.scheduledTask(taskName: String, timeSpan: TimeSpan, function: suspend ReadyEvent.() -> Unit) {
    val delayMillis = if (timeSpan <= 1.minutes) 1.minutes.millisecondsLong else timeSpan.millisecondsLong
    onReady {
        while(coroutineContext.isActive) {
            try {
                function()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    println("Exception during scheduled task \"$taskName\".")
                    e.printStackTrace()
                }
            } finally {
                delay(delayMillis)
            }
        }
    }
}