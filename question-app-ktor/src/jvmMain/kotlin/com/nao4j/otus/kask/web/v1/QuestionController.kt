package com.nao4j.otus.kask.web.v1

import com.nao4j.otus.kask.api.v1.models.QuestionCreateRequest
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteRequest
import com.nao4j.otus.kask.api.v1.models.QuestionReadRequest
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateRequest
import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.mapper.v1.fromTransport
import com.nao4j.otus.kask.mapper.v1.toTransportQuestion
import com.nao4j.otus.kask.web.ApplicationSettings
import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.receive
import io.ktor.server.response.respond

suspend fun ApplicationCall.createQuestion(settings: ApplicationSettings) {
    val request = receive<QuestionCreateRequest>()
    val context = QuestionContext()
    context.fromTransport(request)
    settings.processor.exec(context)
    respond(context.toTransportQuestion())
}

suspend fun ApplicationCall.readQuestion(settings: ApplicationSettings) {
    val request = receive<QuestionReadRequest>()
    val context = QuestionContext()
    context.fromTransport(request)
    settings.processor.exec(context)
    respond(context.toTransportQuestion())
}

suspend fun ApplicationCall.updateQuestion(settings: ApplicationSettings) {
    val request = receive<QuestionUpdateRequest>()
    val context = QuestionContext()
    context.fromTransport(request)
    settings.processor.exec(context)
    respond(context.toTransportQuestion())
}

suspend fun ApplicationCall.deleteQuestion(settings: ApplicationSettings) {
    val request = receive<QuestionDeleteRequest>()
    val context = QuestionContext()
    context.fromTransport(request)
    settings.processor.exec(context)
    respond(context.toTransportQuestion())
}
