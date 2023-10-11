package com.nao4j.otus.kask.web.biz.worker

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.stubs.StubCase
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker
import com.nao4j.otus.kask.web.stub.QuestionStub

fun CorChainDsl<QuestionContext>.stubReadSuccess(title: String) = worker {
    this.title = title
    on { stubCase == StubCase.SUCCESS && state == State.RUNNING }
    handle {
        state = State.FINISHING
        val stub = QuestionStub.prepareResult {
            questionRequest.title.takeIf { it.isNotBlank() }?.also { this.title = it }
        }
        questionResponse = stub
    }
}
