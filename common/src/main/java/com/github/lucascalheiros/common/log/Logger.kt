package com.github.lucascalheiros.common.log

import java.util.logging.Level
import java.util.logging.Logger


fun Any.log(logLevel: LogLevel, message: String, throwable: Throwable? = null) {
    val tag = this::class.simpleName
    val logger = Logger.getLogger(tag ?: "NO TAG")

    return when (logLevel) {
        LogLevel.VERBOSE -> logger.log(Level.FINER, message, throwable)
        LogLevel.DEBUG -> logger.log(Level.FINE, message, throwable)
        LogLevel.INFO -> logger.log(Level.INFO, message, throwable)
        LogLevel.WARNING -> logger.log(Level.WARNING, message, throwable)
        LogLevel.ERROR -> logger.log(Level.SEVERE, message, throwable)
    }
}

fun Any.logVerbose(message: String, throwable: Throwable? = null) {
    log(LogLevel.VERBOSE, message, throwable)
}

fun Any.logDebug(message: String, throwable: Throwable? = null) {
    log(LogLevel.DEBUG, message, throwable)
}

fun Any.logInfo(message: String, throwable: Throwable? = null) {
    log(LogLevel.INFO, message, throwable)
}

fun Any.logWarn(message: String, throwable: Throwable? = null) {
    log(LogLevel.WARNING, message, throwable)
}

fun Any.logError(message: String, throwable: Throwable? = null) {
    log(LogLevel.ERROR, message, throwable)
}