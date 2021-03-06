package hu.zsoki.houdinibot.app

import com.serebit.strife.BotBuilder
import com.serebit.strife.BotBuilderDsl
import com.serebit.strife.events.ReadyEvent
import com.serebit.strife.onReady
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import java.time.Duration
import kotlin.coroutines.coroutineContext

@BotBuilderDsl
fun BotBuilder.scheduledTask(taskName: String, duration: Duration, function: suspend ReadyEvent.() -> Unit) {
    val delayMillis = if (duration.seconds <= 5) 5000L else duration.toMillis()
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