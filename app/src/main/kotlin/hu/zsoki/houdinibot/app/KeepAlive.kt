package hu.zsoki.houdinibot.app

import com.serebit.strife.BotBuilder
import com.serebit.strife.BotBuilderDsl
import com.serebit.strife.data.Activity
import com.serebit.strife.data.OnlineStatus
import com.serebit.strife.onReady
import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan
import com.soywiz.klock.minutes
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.coroutines.coroutineContext

val botLaunchedAt = DateTime.now()

@BotBuilderDsl
fun BotBuilder.keepAlive(timeSpan: TimeSpan) {
    val delayMillis = if (timeSpan <= 1.minutes) 1.minutes.millisecondsLong else timeSpan.millisecondsLong
    onReady {
        while(coroutineContext.isActive) {
            val uptime = DateTime.now() - botLaunchedAt
            val uptimeString = buildUptimeString(uptime)
            context.updatePresence(OnlineStatus.ONLINE, Activity.Type.Playing to uptimeString)
            delay(delayMillis)
        }
    }
}

private fun buildUptimeString(uptime: TimeSpan): String {
    return "Uptime: " + when {
        uptime.hours <= 1.0 -> uptime.minutes.toInt().toString() + " minutes"
        uptime.days <= 1.0 -> uptime.hours.toInt().toString() + " hours"
        else -> uptime.days.toInt().toString() + " days"
    }
}