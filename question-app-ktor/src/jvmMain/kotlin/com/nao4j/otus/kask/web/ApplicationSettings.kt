package com.nao4j.otus.kask.web

import com.nao4j.otus.kask.common.ContextSettings
import com.nao4j.otus.kask.log.LoggerProvider
import com.nao4j.otus.kask.web.biz.QuestionProcessor

data class ApplicationSettings(
    val serverUrls: List<String> = emptyList(),
    val contextSettings: ContextSettings = ContextSettings(),
    val processor: QuestionProcessor = QuestionProcessor(settings = contextSettings),
    val logger: LoggerProvider = LoggerProvider()
)
