package com.nao4j.otus.kask.web.biz

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.web.stub.QuestionStub

class QuestionProcessor {

    suspend fun exec(ctx: QuestionContext) {
        require(ctx.workMode == WorkMode.STUB) {
            "Currently working only in STUB mode."
        }
        require(ctx.command != Command.NONE) {
            "Does not work with NONE command."
        }

        ctx.questionResponse = QuestionStub.get(id = ctx.questionRequest.id)
        ctx.state = State.FINISHING
    }
}
