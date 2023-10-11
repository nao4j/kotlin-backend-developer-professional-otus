package com.nao4j.otus.kask.web.biz.repo

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.repo.DbQuestionRequest
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker

fun CorChainDsl<QuestionContext>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == State.RUNNING }
    handle {
        questionRepositoryRead = questionValidated.copy()
        questionRepositoryPrepare = questionRepositoryRead
    }
}

fun CorChainDsl<QuestionContext>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление объявления в БД"
    on { state == State.RUNNING }
    handle {
        val request = DbQuestionRequest(questionRepositoryPrepare)
        val result = questionRepository.create(request)
        val resultQuestion = result.data
        if (result.isSuccess && resultQuestion != null) {
            questionRepositoryDone = resultQuestion
        } else {
            state = State.FAILING
            errors.addAll(result.errors)
        }
    }
}
