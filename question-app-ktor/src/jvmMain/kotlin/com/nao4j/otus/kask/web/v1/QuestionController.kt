package com.nao4j.otus.kask.web.v1

import com.nao4j.otus.kask.api.v1.models.QuestionCreateRequest
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteRequest
import com.nao4j.otus.kask.api.v1.models.QuestionReadRequest
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateRequest
import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.mapper.v1.fromTransport
import com.nao4j.otus.kask.mapper.v1.toTransportQuestion
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

suspend fun ApplicationCall.createQuestion(processor: QuestionProcessor) {
    val request = receive<QuestionCreateRequest>()
    val context = QuestionContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportQuestion())
}

suspend fun ApplicationCall.readQuestion(processor: QuestionProcessor) {
    val request = receive<QuestionReadRequest>()
    val context = QuestionContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportQuestion())
}

suspend fun ApplicationCall.updateQuestion(processor: QuestionProcessor) {
    val request = receive<QuestionUpdateRequest>()
    val context = QuestionContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportQuestion())
}

suspend fun ApplicationCall.deleteQuestion(processor: QuestionProcessor) {
    val request = receive<QuestionDeleteRequest>()
    val context = QuestionContext()
    context.fromTransport(request)
    processor.exec(context)
    respond(context.toTransportQuestion())
}
