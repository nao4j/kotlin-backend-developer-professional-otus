package com.nao4j.otus.kask.web.biz.repo

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.repo.DbQuestionRequest
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker

fun CorChainDsl<QuestionContext>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, и данные, полученные от пользователя"
    on { state == State.RUNNING }
    handle {
        questionRepositoryPrepare = questionRepositoryRead.copy().apply {
            this.title = questionValidated.title
            description = questionValidated.description
        }
    }
}

fun CorChainDsl<QuestionContext>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        val request = DbQuestionRequest(questionRepositoryPrepare)
        val result = questionRepository.update(request)
        val resultQuestion = result.data
        if (result.isSuccess && resultQuestion != null) {
            questionRepositoryDone = resultQuestion
        } else {
            state = State.FAILING
            errors.addAll(result.errors)
            questionRepositoryDone
        }
    }
}
