package hu.zsoki.houdinibot.app

import com.serebit.strife.BotBuilder
import com.serebit.strife.BotBuilderDsl
import com.serebit.strife.events.ReadyEvent
import com.serebit.strife.onReady
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

@BotBuilderDsl
fun BotBuilder.scheduledTask(taskName: String, delayMillis: Long, function: suspend ReadyEvent.() -> Unit) {
    val delayMillisNormalized = if (delayMillis <= 5000L) 5000L else delayMillis
    onReady {
        while(coroutineContext.isActive) {
            try {
                supervisorScope { function() }
            } catch (e: Exception) {
                println("Exception during scheduled task \"$taskName\".")
                e.printStackTrace()
            } finally {
                delay(delayMillisNormalized)
            }
        }
    }
}