package com.nao4j.otus.kask.web.biz.repo

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.repo.DbQuestionIdRequest
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker

fun CorChainDsl<QuestionContext>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = "Готовим данные к удалению из БД"
    on { state == State.RUNNING }
    handle {
        questionRepositoryPrepare = questionValidated.copy()
    }
}

fun CorChainDsl<QuestionContext>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление объявления из БД по ID"
    on { state == State.RUNNING }
    handle {
        val request = DbQuestionIdRequest(questionRepositoryPrepare)
        val result = questionRepository.delete(request)
        if (!result.isSuccess) {
            state = State.FAILING
            errors.addAll(result.errors)
        }
        questionRepositoryDone = questionRepositoryRead
    }
}
