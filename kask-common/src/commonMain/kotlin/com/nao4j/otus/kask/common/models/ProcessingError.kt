package com.nao4j.otus.kask.common.models

data class ProcessingError(
    val code: String = "",
    val level: Level = Level.ERROR,
    val group: String = "",
    val field: String = "",
    val message: String = "",
    val exception: Throwable? = null
) {

    enum class Level {
        ERROR,
        WARN,
        INFO
    }
}

fun Throwable.asProcessingError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: ""
) = ProcessingError(code = code, group = group, field = "", message = message, exception = this)
