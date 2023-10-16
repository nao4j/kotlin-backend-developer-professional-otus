package com.nao4j.otus.kask.web.helper

import com.nao4j.otus.kask.common.ContextSettings
import com.nao4j.otus.kask.common.repo.QuestionRepository
import com.nao4j.otus.kask.repo.inmemory.QuestionRepoInMemory
import com.nao4j.otus.kask.repo.stub.QuestionRepositoryStub
import com.nao4j.otus.kask.web.ApplicationSettings

fun testSettings(repo: QuestionRepository? = null): ApplicationSettings =
    ApplicationSettings(
        contextSettings = ContextSettings(
            repoStub = QuestionRepositoryStub(),
            repoTest = repo ?: QuestionRepoInMemory(),
            repoProd = repo ?: QuestionRepoInMemory()
        )
    )
