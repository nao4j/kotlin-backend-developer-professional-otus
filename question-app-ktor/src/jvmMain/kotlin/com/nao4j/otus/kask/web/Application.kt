package com.nao4j.otus.kask.web

import com.fasterxml.jackson.databind.json.JsonMapper
import com.nao4j.otus.kask.common.ContextSettings
import com.nao4j.otus.kask.repo.inmemory.QuestionRepoInMemory
import com.nao4j.otus.kask.repo.stub.QuestionRepositoryStub
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import com.nao4j.otus.kask.web.v1.v1Question
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.module(
    processor: QuestionProcessor = QuestionProcessor(
        settings = ContextSettings(
            repoStub = QuestionRepositoryStub(),
            repoTest = QuestionRepoInMemory()
        )
    )
) {
    routing {
        route("v1") {
            install(ContentNegotiation) {
                jackson {
                    val mapper = JsonMapper.builder().build()
                    setConfig(mapper.serializationConfig)
                    setConfig(mapper.deserializationConfig)
                }
            }

            v1Question(processor)
        }
    }
}

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
