package com.nao4j.otus.kask.web.v1

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.SerializationFeature
import com.nao4j.otus.kask.api.v1.models.QuestionCreateObject
import com.nao4j.otus.kask.api.v1.models.QuestionCreateRequest
import com.nao4j.otus.kask.api.v1.models.QuestionCreateResponse
import com.nao4j.otus.kask.api.v1.models.QuestionDebug
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteObject
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteRequest
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteResponse
import com.nao4j.otus.kask.api.v1.models.QuestionReadObject
import com.nao4j.otus.kask.api.v1.models.QuestionReadRequest
import com.nao4j.otus.kask.api.v1.models.QuestionReadResponse
import com.nao4j.otus.kask.api.v1.models.QuestionRequestDebugMode
import com.nao4j.otus.kask.api.v1.models.QuestionRequestDebugStubs
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateObject
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateRequest
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateResponse
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.jackson.jackson
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import kotlin.test.assertEquals
import org.junit.Test

class V1QuestionStubApiTest {

    @Test
    fun create() = testApplication {
        val response = testClient().post("/v1/question/create") {
            contentType(ContentType.Application.Json)
            setBody(
                QuestionCreateRequest(
                    requestType = "create",
                    requestId = "1",
                    debug = QuestionDebug(
                        mode = QuestionRequestDebugMode.STUB,
                        stub = QuestionRequestDebugStubs.SUCCESS
                    ),
                    question = QuestionCreateObject(
                        title = "Здравствуйте, это канал про аниме?",
                        description = "Как пропатчикть KDE под FreeBSD?",
                    )
                )
            )
        }
        assertEquals(expected = 200, actual = response.status.value)

        val responseBody = response.body<QuestionCreateResponse>()
        assertEquals(expected = "64", actual = responseBody.question?.id)
    }

    @Test
    fun read() = testApplication {
        val response = testClient().post("/v1/question/read") {
            contentType(ContentType.Application.Json)
            setBody(
                QuestionReadRequest(
                    requestType = "read",
                    requestId = "1",
                    debug = QuestionDebug(
                        mode = QuestionRequestDebugMode.STUB,
                        stub = QuestionRequestDebugStubs.SUCCESS
                    ),
                    question = QuestionReadObject(
                        id = "42"
                    )
                )
            )
        }
        assertEquals(expected = 200, actual = response.status.value)

        val responseBody = response.body<QuestionReadResponse>()
        assertEquals(expected = "64", actual = responseBody.question?.id)
    }

    @Test
    fun update() = testApplication {
        val response = testClient().post("/v1/question/update") {
            contentType(ContentType.Application.Json)
            setBody(
                QuestionUpdateRequest(
                    requestType = "update",
                    requestId = "1",
                    debug = QuestionDebug(
                        mode = QuestionRequestDebugMode.STUB,
                        stub = QuestionRequestDebugStubs.SUCCESS
                    ),
                    question = QuestionUpdateObject(
                        id = "42",
                        title = "Здравствуйте, это канал про аниме?",
                        description = "Как пропатчикть KDE под FreeBSD?",
                    )
                )
            )
        }
        assertEquals(expected = 200, actual = response.status.value)

        val responseBody = response.body<QuestionUpdateResponse>()
        assertEquals(expected = "42", actual = responseBody.question?.id)
    }

    @Test
    fun delete() = testApplication {
        val response = testClient().post("/v1/question/delete") {
            contentType(ContentType.Application.Json)
            setBody(
                QuestionDeleteRequest(
                    requestType = "delete",
                    requestId = "1",
                    debug = QuestionDebug(
                        mode = QuestionRequestDebugMode.STUB,
                        stub = QuestionRequestDebugStubs.SUCCESS
                    ),
                    question = QuestionDeleteObject(
                        id = "42"
                    )
                )
            )
        }
        assertEquals(expected = 200, actual = response.status.value)

        val responseBody = response.body<QuestionDeleteResponse>()
        assertEquals(expected = "64", actual = responseBody.question?.id)
    }

    private fun ApplicationTestBuilder.testClient() = createClient {
        install(ContentNegotiation) {
            jackson {
                disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

                enable(SerializationFeature.INDENT_OUTPUT)
                writerWithDefaultPrettyPrinter()
            }
        }
    }
}
