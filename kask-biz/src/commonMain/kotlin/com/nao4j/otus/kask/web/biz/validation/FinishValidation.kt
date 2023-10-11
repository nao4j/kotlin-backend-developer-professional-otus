package com.nao4j.otus.kask.web.biz.validation

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker

fun CorChainDsl<QuestionContext>.finishAdValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        questionValidated = questionValidating
    }
}
