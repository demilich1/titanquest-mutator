package net.demilich.kisslog

interface Logger {
    fun trace(message: String, vararg params: String)
    fun debug(message: String, vararg params: String)
    fun info(message: String, vararg params: String)
    fun warn(message: String, vararg params: String)
    fun error(message: String, vararg params: String)
}