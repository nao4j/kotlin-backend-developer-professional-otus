package com.nao4j.otus.kask.mapper.v1

import com.nao4j.otus.kask.api.v1.models.IRequest
import com.nao4j.otus.kask.api.v1.models.QuestionCreateObject
import com.nao4j.otus.kask.api.v1.models.QuestionCreateRequest
import com.nao4j.otus.kask.api.v1.models.QuestionDebug
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteObject
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteRequest
import com.nao4j.otus.kask.api.v1.models.QuestionReadObject
import com.nao4j.otus.kask.api.v1.models.QuestionReadRequest
import com.nao4j.otus.kask.api.v1.models.QuestionRequestDebugMode
import com.nao4j.otus.kask.api.v1.models.QuestionRequestDebugStubs
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateObject
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateRequest
import com.nao4j.otus.kask.common.NONE
import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.RequestId
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.UserId
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.common.stubs.StubCase
import com.nao4j.otus.kask.mapper.v1.exception.UnknownRequestClassException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlinx.datetime.Instant

class MapperV1FromTransportTest {

    @Test
    fun fromQuestionCreateRequest() {
        val expected = QuestionContext(
            workMode = WorkMode.STUB,
            stubCase = StubCase.SUCCESS,
            command = Command.CREATE,
            state = State.NONE,
            requestId = RequestId("testRequestId"),
            timeStart = Instant.NONE,
            questionRequest = Question(
                id = Question.QuestionId.NONE,
                title = "testTitle",
                description = "testDescription",
                ownerId = UserId.NONE,
                createdAt = Instant.NONE,
                modifiedAt = Instant.NONE
            ),
            questionResponse = Question(),
            questionsResponse = mutableListOf(),
            errors = mutableListOf()
        )
        val actual = QuestionContext().apply {
            fromTransport(
                QuestionCreateRequest(
                    requestType = "create",
                    requestId = "testRequestId",
                    debug = QuestionDebug(
                        mode = QuestionRequestDebugMode.STUB,
                        stub = QuestionRequestDebugStubs.SUCCESS
                    ),
                    question = QuestionCreateObject(
                        title = "testTitle",
                        description = "testDescription"
                    )
                )
            )
        }

        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun fromQuestionReadRequest() {
        val expected = QuestionContext(
            workMode = WorkMode.TEST,
            stubCase = StubCase.NONE,
            command = Command.READ,
            state = State.NONE,
            requestId = RequestId("testRequestId"),
            timeStart = Instant.NONE,
            questionRequest = Question(
                id = Question.QuestionId("testId"),
                title = "",
                description = "",
                ownerId = UserId.NONE,
                createdAt = Instant.NONE,
                modifiedAt = Instant.NONE
            ),
            questionResponse = Question(),
            questionsResponse = mutableListOf(),
            errors = mutableListOf()
        )
        val actual = QuestionContext().apply {
            fromTransport(
                QuestionReadRequest(
                    requestType = "read",
                    requestId = "testRequestId",
                    debug = QuestionDebug(
                        mode = QuestionRequestDebugMode.TEST,
                        stub = null
                    ),
                    question = QuestionReadObject(
                        id = "testId"
                    )
                )
            )
        }

        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun fromQuestionUpdateRequest() {
        val expected = QuestionContext(
            workMode = WorkMode.PROD,
            stubCase = StubCase.NOT_FOUND,
            command = Command.UPDATE,
            state = State.NONE,
            requestId = RequestId("testRequestId"),
            timeStart = Instant.NONE,
            questionRequest = Question(
                id = Question.QuestionId("testId"),
                title = "testTitle",
                description = "testDescription",
                ownerId = UserId.NONE,
                createdAt = Instant.NONE,
                modifiedAt = Instant.NONE
            ),
            questionResponse = Question(),
            questionsResponse = mutableListOf(),
            errors = mutableListOf()
        )
        val actual = QuestionContext().apply {
            fromTransport(
                QuestionUpdateRequest(
                    requestType = "update",
                    requestId = "testRequestId",
                    debug = QuestionDebug(
                        mode = QuestionRequestDebugMode.PROD,
                        stub = QuestionRequestDebugStubs.NOT_FOUND
                    ),
                    question = QuestionUpdateObject(
                        id = "testId",
                        title = "testTitle",
                        description = "testDescription"
                    )
                )
            )
        }

        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun fromQuestionDeleteRequest() {
        val expected = QuestionContext(
            workMode = WorkMode.STUB,
            stubCase = StubCase.BAD_ID,
            command = Command.DELETE,
            state = State.NONE,
            requestId = RequestId("testRequestId"),
            timeStart = Instant.NONE,
            questionRequest = Question(
                id = Question.QuestionId("testId"),
                title = "",
                description = "",
                ownerId = UserId.NONE,
                createdAt = Instant.NONE,
                modifiedAt = Instant.NONE
            ),
            questionResponse = Question(),
            questionsResponse = mutableListOf(),
            errors = mutableListOf()
        )
        val actual = QuestionContext().apply {
            fromTransport(
                QuestionDeleteRequest(
                    requestType = "delete",
                    requestId = "testRequestId",
                    debug = QuestionDebug(
                        mode = QuestionRequestDebugMode.STUB,
                        stub = QuestionRequestDebugStubs.BAD_ID
                    ),
                    question = QuestionDeleteObject(
                        id = "testId"
                    )
                )
            )
        }

        assertEquals(expected = expected, actual = actual)
    }

    @Test
    fun fromUnknownRequest() {
        assertFails {
            QuestionContext().apply {
                fromTransport(
                    object : IRequest {
                        override val requestType = ""
                        override val requestId = ""
                    }
                )
            }
        } is UnknownRequestClassException
    }
}
