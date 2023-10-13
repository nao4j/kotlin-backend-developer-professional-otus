package com.nao4j.otus.kask.web.biz.worker

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.ProcessingError
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker

fun CorChainDsl<QuestionContext>.stubNoCase(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        fail(
            ProcessingError(
                code = "validation",
                field = "stub",
                group = "validation",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
