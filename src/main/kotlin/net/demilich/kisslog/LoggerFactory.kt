package net.demilich.kisslog

import java.util.*

object LoggerFactory {
    fun getDefaultLogger(): Logger {
        return ConsoleLogger(LogLevel.DEBUG, EnumSet.of(LogPattern.TIME, LogPattern.LEVEL))
    }
}