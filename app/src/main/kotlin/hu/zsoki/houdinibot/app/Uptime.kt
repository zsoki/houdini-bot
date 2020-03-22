package hu.zsoki.houdinibot.app

import com.soywiz.klock.DateTime
import com.soywiz.klock.TimeSpan

object Uptime {

    private val botLaunchedAt = DateTime.now()

    val formattedString: String
        get() {
            val uptime = calculateUptime()
            return "Up: " + when {
                uptime.hours <= 1.0 -> uptime.minutes.toInt().toString() + "m"
                uptime.days <= 1.0 -> uptime.hours.toInt().toString() + "h"
                else -> uptime.days.toInt().toString() + "d"
            }
        }

    private fun calculateUptime(): TimeSpan = DateTime.now() - botLaunchedAt
}
