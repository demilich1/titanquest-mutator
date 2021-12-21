package net.demilich.kisslog

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

abstract class AbstractLogger(var level: LogLevel, val pattern: EnumSet<LogPattern>) : Logger {
    private val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS")

    protected fun insertParams(message: String, vararg params: String) : String {
        if (params.isEmpty()) {
            return message
        }

        var result = message
        //TODO: check performance, for large number of params it may make sense to switch to a StringBuilder
        for (param in params) {
            result = result.replaceFirst("{}", param)
        }
        return result
    }

    protected fun buildLogMessage(logLevelString: String, message: String): String {
        var logMessage = ""
        if (pattern.contains(LogPattern.TIME)) {
            val currentTime = LocalDateTime.now()
            val formattedTime = timeFormatter.format(currentTime)
            logMessage += "$formattedTime "
        }
        if (pattern.contains(LogPattern.LEVEL)) {
            logMessage += "[$logLevelString] "
        }
        if (logMessage.isNotEmpty()) {
            logMessage += "- "
        }
        logMessage += message
        return logMessage
    }
}