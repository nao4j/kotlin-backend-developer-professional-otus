package com.nao4j.otus.kask.mapper.v1

import com.nao4j.otus.kask.api.v1.models.IResponse
import com.nao4j.otus.kask.api.v1.models.QuestionCreateResponse
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteResponse
import com.nao4j.otus.kask.api.v1.models.QuestionReadResponse
import com.nao4j.otus.kask.api.v1.models.QuestionResponseObject
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateResponse
import com.nao4j.otus.kask.api.v1.models.ResponseResult
import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.ProcessingError
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.UserId
import com.nao4j.otus.kask.mapper.v1.exception.UnknownCommandException

fun QuestionContext.toTransportQuestion(): IResponse = when (val command = command) {
    Command.CREATE -> toTransportCreate()
    Command.READ -> toTransportRead()
    Command.UPDATE -> toTransportUpdate()
    Command.DELETE -> toTransportDelete()
    Command.NONE -> throw UnknownCommandException(command)
}

private fun QuestionContext.toTransportCreate() = QuestionCreateResponse(
    responseType = "create",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == State.FINISHING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    question = questionResponse.toTransportQuestion(),
    errors = errors.toTransportErrors()
)

private fun QuestionContext.toTransportRead() = QuestionReadResponse(
    responseType = "read",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == State.FINISHING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    question = questionResponse.toTransportQuestion(),
    errors = errors.toTransportErrors()
)

private fun QuestionContext.toTransportUpdate() = QuestionUpdateResponse(
    responseType = "update",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == State.FINISHING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    question = questionResponse.toTransportQuestion(),
    errors = errors.toTransportErrors()
)

private fun QuestionContext.toTransportDelete() = QuestionDeleteResponse(
    responseType = "delete",
    requestId = this.requestId.asString().takeIf { it.isNotBlank() },
    result = if (state == State.FINISHING) ResponseResult.SUCCESS else ResponseResult.ERROR,
    question = questionResponse.toTransportQuestion(),
    errors = errors.toTransportErrors()
)

private fun Question.toTransportQuestion(): QuestionResponseObject = QuestionResponseObject(
    id = id.takeIf { it != Question.QuestionId.NONE }?.asString(),
    title = title.takeIf { it.isNotBlank() },
    description = description.takeIf { it.isNotBlank() },
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
)

private fun List<ProcessingError>.toTransportErrors(): List<com.nao4j.otus.kask.api.v1.models.Error>? =
    this.map { it.toTransportError() }.toList().takeIf { it.isNotEmpty() }

private fun ProcessingError.toTransportError() = com.nao4j.otus.kask.api.v1.models.Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() }
)
