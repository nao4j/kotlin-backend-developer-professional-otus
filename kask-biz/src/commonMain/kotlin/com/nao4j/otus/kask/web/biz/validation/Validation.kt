package com.nao4j.otus.kask.web.biz.validation

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.cor.chain
import com.nao4j.otus.kask.cor.handlers.CorChainDsl

fun CorChainDsl<QuestionContext>.validation(block: CorChainDsl<QuestionContext>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == State.RUNNING }
}
