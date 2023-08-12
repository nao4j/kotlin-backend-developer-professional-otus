package com.nao4j.otus.kask.mapper.v1

import com.nao4j.otus.kask.api.v1.models.IRequest
import com.nao4j.otus.kask.api.v1.models.QuestionCreateObject
import com.nao4j.otus.kask.api.v1.models.QuestionCreateRequest
import com.nao4j.otus.kask.api.v1.models.QuestionDebug
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteRequest
import com.nao4j.otus.kask.api.v1.models.QuestionReadRequest
import com.nao4j.otus.kask.api.v1.models.QuestionRequestDebugMode
import com.nao4j.otus.kask.api.v1.models.QuestionRequestDebugStubs
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateObject
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateRequest
import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.RequestId
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.common.stubs.StubCase
import com.nao4j.otus.kask.mapper.v1.exception.UnknownRequestClassException

fun QuestionContext.fromTransport(request: IRequest) = when (request) {
    is QuestionCreateRequest -> fromTransport(request)
    is QuestionReadRequest -> fromTransport(request)
    is QuestionUpdateRequest -> fromTransport(request)
    is QuestionDeleteRequest -> fromTransport(request)
    else -> throw UnknownRequestClassException(request::class)
}

private fun QuestionContext.fromTransport(request: QuestionCreateRequest) {
    command = Command.CREATE
    requestId = request.requestId()
    questionRequest = request.question?.toInternal() ?: Question()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun QuestionContext.fromTransport(request: QuestionReadRequest) {
    command = Command.READ
    requestId = request.requestId()
    questionRequest = request.question?.id.toQuestionWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun QuestionContext.fromTransport(request: QuestionUpdateRequest) {
    command = Command.UPDATE
    requestId = request.requestId()
    questionRequest = request.question?.toInternal() ?: Question()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun QuestionContext.fromTransport(request: QuestionDeleteRequest) {
    command = Command.DELETE
    requestId = request.requestId()
    questionRequest = request.question?.id.toQuestionWithId()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun IRequest?.requestId() = this?.requestId?.let { RequestId(it) } ?: RequestId.NONE

private fun String?.toQuestionId() = this?.let { Question.QuestionId(it) } ?: Question.QuestionId.NONE

private fun String?.toQuestionWithId() = Question(id = this.toQuestionId())

private fun QuestionCreateObject.toInternal(): Question = Question(
    title = this.title ?: "",
    description = this.description ?: ""
)

private fun QuestionUpdateObject.toInternal(): Question = Question(
    id = this.id.toQuestionId(),
    title = this.title ?: "",
    description = this.description ?: ""
)

private fun QuestionDebug?.transportToWorkMode(): WorkMode = when (this?.mode) {
    QuestionRequestDebugMode.PROD -> WorkMode.PROD
    QuestionRequestDebugMode.TEST -> WorkMode.TEST
    QuestionRequestDebugMode.STUB -> WorkMode.STUB
    null -> WorkMode.PROD
}

private fun QuestionDebug?.transportToStubCase(): StubCase = when (this?.stub) {
    QuestionRequestDebugStubs.SUCCESS -> StubCase.SUCCESS
    QuestionRequestDebugStubs.NOT_FOUND -> StubCase.NOT_FOUND
    QuestionRequestDebugStubs.BAD_ID -> StubCase.BAD_ID
    QuestionRequestDebugStubs.BAD_TITLE -> StubCase.BAD_TITLE
    QuestionRequestDebugStubs.BAD_DESCRIPTION -> StubCase.BAD_DESCRIPTION
    QuestionRequestDebugStubs.CANNOT_DELETE -> StubCase.CANNOT_DELETE
    null -> StubCase.NONE
}
