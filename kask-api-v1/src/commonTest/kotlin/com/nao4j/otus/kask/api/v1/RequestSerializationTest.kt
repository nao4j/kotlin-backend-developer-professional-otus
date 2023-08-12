package com.nao4j.otus.kask.api.v1

import com.nao4j.otus.kask.api.v1.models.IRequest
import com.nao4j.otus.kask.api.v1.models.QuestionCreateObject
import com.nao4j.otus.kask.api.v1.models.QuestionCreateRequest
import com.nao4j.otus.kask.api.v1.models.QuestionDebug
import com.nao4j.otus.kask.api.v1.models.QuestionRequestDebugMode.STUB
import com.nao4j.otus.kask.api.v1.models.QuestionRequestDebugStubs.SUCCESS
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestSerializationTest {

    private val request: IRequest = QuestionCreateRequest(
        requestType = "create",
        requestId = "1",
        debug = QuestionDebug(
            mode = STUB,
            stub = SUCCESS
        ),
        question = QuestionCreateObject(
            title = "StackOverflowError при обходе графа на Java",
            description = "Получаю StackOverflowError при рекурсивном обходе графа с петлями на Java"
        )
    )

    @Test
    fun serialize() {
        val requestJson = apiV1RequestSerialize(request)

        assertContains(requestJson, Regex("\"requestType\":\\s*\"create\""))
        assertContains(requestJson, Regex("\"requestId\":\\s*\"1\""))
        assertContains(requestJson, Regex("\"mode\":\\s*\"stub\""))
        assertContains(requestJson, Regex("\"stub\":\\s*\"success\""))
        assertContains(requestJson, Regex("\"title\":\\s*\"StackOverflowError при обходе графа на Java\""))
        assertContains(
            requestJson,
            Regex("\"description\":\\s*\"Получаю StackOverflowError при рекурсивном обходе графа с петлями на Java\"")
        )
    }

    @Test
    fun deserialize() {
        val requestJson = apiV1RequestSerialize(request)
        val requestObject = apiV1RequestDeserialize<QuestionCreateRequest>(requestJson)

        assertEquals(request, requestObject)
    }

    @Test
    fun deserializeNaked() {
        val requestJson = """
        {
            "requestType": "create",
            "requestId": "1",
            "debug": {
                "mode": "stub",
                "stub": "success"
            },
            "question": {
                "title": "StackOverflowError при обходе графа на Java",
                "description": "Получаю StackOverflowError при рекурсивном обходе графа с петлями на Java"
            }
        }
        """.trimIndent()
        val requestObject = apiV1RequestDeserialize<IRequest>(requestJson)

        assertEquals(request, requestObject)
    }
}
