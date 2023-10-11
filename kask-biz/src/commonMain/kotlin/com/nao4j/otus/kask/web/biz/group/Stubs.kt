package com.nao4j.otus.kask.web.biz.group

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.cor.chain
import com.nao4j.otus.kask.cor.handlers.CorChainDsl

fun CorChainDsl<QuestionContext>.stubs(title: String, block: CorChainDsl<QuestionContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == WorkMode.STUB && state == State.RUNNING }
}
