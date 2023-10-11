package com.nao4j.otus.kask.web.biz.worker

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.ProcessingError
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.stubs.StubCase
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker

fun CorChainDsl<QuestionContext>.stubDbError(title: String) = worker {
    this.title = title
    on { stubCase == StubCase.DB_ERROR && state == State.RUNNING }
    handle {
        state = State.FAILING
        this.errors.add(
            ProcessingError(
                group = "internal",
                code = "internal-db",
                message = "Internal error"
            )
        )
    }
}
