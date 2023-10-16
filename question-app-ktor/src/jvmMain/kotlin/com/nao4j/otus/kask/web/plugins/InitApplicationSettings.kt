package com.nao4j.otus.kask.web.plugins

import com.nao4j.otus.kask.common.ContextSettings
import com.nao4j.otus.kask.repo.stub.QuestionRepositoryStub
import com.nao4j.otus.kask.web.ApplicationSettings
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import io.ktor.server.application.Application

fun Application.initApplicationSettings(): ApplicationSettings {
    val contextSettings = ContextSettings(
        repoStub = QuestionRepositoryStub(),
        repoTest = initQuestionRepository(QuestionDbType.TEST),
        repoProd = initQuestionRepository(QuestionDbType.PROD)
    )
    return ApplicationSettings(
        serverUrls = environment.config.propertyOrNull("ktor.urls")?.getList() ?: emptyList(),
        processor = QuestionProcessor(contextSettings)
    )
}
