package com.nao4j.otus.kask.web.biz.group

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.cor.chain
import com.nao4j.otus.kask.cor.handlers.CorChainDsl

fun CorChainDsl<QuestionContext>.operation(title: String, command: Command, block: CorChainDsl<QuestionContext>.() -> Unit) = chain {
    block()
    this.title = title
    on { this.command == command && state == State.RUNNING }
}
