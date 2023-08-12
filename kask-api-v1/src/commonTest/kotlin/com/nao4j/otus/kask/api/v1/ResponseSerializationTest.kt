package com.nao4j.otus.kask.api.v1

import com.nao4j.otus.kask.api.v1.models.IResponse
import com.nao4j.otus.kask.api.v1.models.QuestionCreateResponse
import com.nao4j.otus.kask.api.v1.models.QuestionResponseObject
import com.nao4j.otus.kask.api.v1.models.ResponseResult.SUCCESS
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseSerializationTest {

    private val response: IResponse = QuestionCreateResponse(
        responseType = "create",
        requestId = "1",
        result = SUCCESS,
        question = QuestionResponseObject(
            title = "StackOverflowError при обходе графа на Java",
            description = "Получаю StackOverflowError при рекурсивном обходе графа с петлями на Java"
        )
    )

    @Test
    fun serialize() {
        val responseJson = apiV1ResponseSerialize(response)

        assertContains(responseJson, Regex("\"responseType\":\\s*\"create\""))
        assertContains(responseJson, Regex("\"requestId\":\\s*\"1\""))
        assertContains(responseJson, Regex("\"result\":\\s*\"success\""))
        assertContains(responseJson, Regex("\"title\":\\s*\"StackOverflowError при обходе графа на Java\""))
        assertContains(
            responseJson,
            Regex("\"description\":\\s*\"Получаю StackOverflowError при рекурсивном обходе графа с петлями на Java\"")
        )
    }

    @Test
    fun deserialize() {
        val responseJson = apiV1ResponseSerialize(response)
        val responseObject = apiV1ResponseDeserialize<IResponse>(responseJson)

        assertEquals(response, responseObject)
    }
}
