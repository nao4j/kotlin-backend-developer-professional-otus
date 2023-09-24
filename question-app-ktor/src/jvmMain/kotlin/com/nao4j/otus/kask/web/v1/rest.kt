package com.nao4j.otus.kask.web.v1

import com.nao4j.otus.kask.web.biz.QuestionProcessor
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.v1Question(processor: QuestionProcessor) {
    route("question") {
        post("create") {
            call.createQuestion(processor)
        }
        post("read") {
            call.readQuestion(processor)
        }
        post("update") {
            call.updateQuestion(processor)
        }
        post("delete") {
            call.deleteQuestion(processor)
        }
    }
}
