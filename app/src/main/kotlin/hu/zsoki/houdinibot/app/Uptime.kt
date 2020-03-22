package hu.zsoki.houdinibot.app

import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan

private val botLaunchedAt = DateTime.now()

fun buildUptimeString(): String {
    val uptime = calculateUptime()
    return "Uptime: " + when {
        uptime.hours <= 1.0 -> uptime.minutes.toInt().toString() + " minutes"
        uptime.days <= 1.0 -> uptime.hours.toInt().toString() + " hours"
        else -> uptime.days.toInt().toString() + " days"
    }
}

private fun calculateUptime(): TimeSpan = DateTime.now() - botLaunchedAt
