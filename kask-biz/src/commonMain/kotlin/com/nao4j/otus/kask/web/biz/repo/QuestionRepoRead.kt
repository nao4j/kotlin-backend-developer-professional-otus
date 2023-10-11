package com.nao4j.otus.kask.web.biz.repo

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.repo.DbQuestionIdRequest
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker

fun CorChainDsl<QuestionContext>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение объявления из БД"
    on { state == State.RUNNING }
    handle {
        val request = DbQuestionIdRequest(questionValidated)
        val result = questionRepository.read(request)
        val resultQuestion = result.data
        if (result.isSuccess && resultQuestion != null) {
            questionRepositoryRead = resultQuestion
        } else {
            state = State.FAILING
            errors.addAll(result.errors)
        }
    }
}
