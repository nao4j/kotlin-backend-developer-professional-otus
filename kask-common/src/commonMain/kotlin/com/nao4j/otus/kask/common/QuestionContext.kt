package com.nao4j.otus.kask.common

import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.ProcessingError
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.RequestId
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.common.stubs.StubCase
import kotlinx.datetime.Instant

data class QuestionContext(
    var workMode: WorkMode = WorkMode.PROD,
    var stubCase: StubCase = StubCase.NONE,

    var command: Command = Command.NONE,
    var state: State = State.NONE,

    var requestId: RequestId = RequestId.NONE,
    var timeStart: Instant = Instant.NONE,
    var questionRequest: Question = Question(),
    var questionResponse: Question = Question(),

    var questionsResponse: MutableList<Question> = mutableListOf(),
    val errors: MutableList<ProcessingError> = mutableListOf()
) {

    fun addError(vararg error: ProcessingError): Boolean =
        errors.addAll(error)

    fun fail(error: ProcessingError) {
        addError(error)
        state = State.FAILING
    }
}
