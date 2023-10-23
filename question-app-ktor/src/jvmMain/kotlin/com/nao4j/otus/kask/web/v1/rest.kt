package com.nao4j.otus.kask.web.v1

import com.nao4j.otus.kask.web.ApplicationSettings
import io.ktor.server.application.call
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.v1Question(settings: ApplicationSettings) {
    route("question") {
        post("create") {
            call.createQuestion(settings)
        }
        post("read") {
            call.readQuestion(settings)
        }
        post("update") {
            call.updateQuestion(settings)
        }
        post("delete") {
            call.deleteQuestion(settings)
        }
    }
}
