package hu.zsoki.houdinibot.app

import java.time.Duration
import java.time.LocalDateTime


object Uptime {

    private val botLaunchedAt = LocalDateTime.now()

    val formattedString: String
        get() {
            val uptime = calculateUptime()
            return "Up: " + when {
                uptime.toHours() <= 1.0 -> uptime.toMinutes().toInt().toString() + "m"
                uptime.toDays() <= 1.0 -> uptime.toHours().toInt().toString() + "h"
                else -> uptime.toDays().toInt().toString() + "d"
            }
        }

    private fun calculateUptime(): Duration = Duration.between(botLaunchedAt, LocalDateTime.now())
}
