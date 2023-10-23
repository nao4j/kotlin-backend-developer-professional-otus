package com.nao4j.otus.kask.common

import com.nao4j.otus.kask.common.repo.QuestionRepository
import com.nao4j.otus.kask.log.LoggerProvider

data class ContextSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val repoStub: QuestionRepository = QuestionRepository.NONE,
    val repoTest: QuestionRepository = QuestionRepository.NONE,
    val repoProd: QuestionRepository = QuestionRepository.NONE
) {

    companion object {

        val NONE = ContextSettings()
    }
}
