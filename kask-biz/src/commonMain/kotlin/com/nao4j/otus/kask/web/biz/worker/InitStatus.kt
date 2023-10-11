package com.nao4j.otus.kask.web.biz.worker

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker

fun CorChainDsl<QuestionContext>.initStatus(title: String) = worker() {
    this.title = title
    on { state == State.NONE }
    handle { state = State.RUNNING }
}
