package net.demilich.kisslog

import java.util.*

class ConsoleLogger(level: LogLevel, pattern: EnumSet<LogPattern>) : AbstractLogger(level, pattern) {

    override fun trace(message: String, vararg params: String) {
        if (level > LogLevel.TRACE) {
            return
        }
        val processedMessage = insertParams(message, *params)
        val logMessage = buildLogMessage("TRACE", processedMessage)
        println(logMessage)
    }

    override fun debug(message: String, vararg params: String) {
        if (level > LogLevel.DEBUG) {
            return
        }
        val processedMessage = insertParams(message, *params)
        val logMessage = buildLogMessage("DEBUG", processedMessage)
        println(logMessage)
    }

    override fun info(message: String, vararg params: String) {
        if (level > LogLevel.INFO) {
            return
        }
        val processedMessage = insertParams(message, *params)
        val logMessage = buildLogMessage("INFO", processedMessage)
        println(logMessage)
    }

    override fun warn(message: String, vararg params: String) {
        if (level > LogLevel.WARN) {
            return
        }
        val processedMessage = insertParams(message, *params)
        val logMessage = buildLogMessage("WARN", processedMessage)
        println(logMessage)
    }

    override fun error(message: String, vararg params: String) {
        if (level > LogLevel.ERROR) {
            return
        }
        val processedMessage = insertParams(message, *params)
        val logMessage = buildLogMessage("ERROR", processedMessage)
        println(logMessage)
    }


}