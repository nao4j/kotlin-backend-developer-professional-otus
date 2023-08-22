package com.nao4j.otus.kask.mapper.v1

import com.nao4j.otus.kask.api.v1.models.Error
import com.nao4j.otus.kask.api.v1.models.QuestionCreateResponse
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteResponse
import com.nao4j.otus.kask.api.v1.models.QuestionReadResponse
import com.nao4j.otus.kask.api.v1.models.QuestionResponseObject
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateResponse
import com.nao4j.otus.kask.api.v1.models.ResponseResult
import com.nao4j.otus.kask.common.NONE
import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.ProcessingError
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.RequestId
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.UserId
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.common.stubs.StubCase
import com.nao4j.otus.kask.mapper.v1.exception.UnknownCommandException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlinx.datetime.Instant

class MapperV1ToTransportTest {

    @Test
    fun toQuestionCreateResponse() {
        val expected = QuestionCreateResponse(
            responseType = "create",
            requestId = "testRequestId",
            result = ResponseResult.SUCCESS,
            errors = null,
            question = QuestionResponseObject(
                id = "testId",
                ownerId = "testOwnerId",
                title = "testTitle",
                description = "testDescription"
            )
        )
        val actual = QuestionContext(
            workMode = WorkMode.PROD,
            stubCase = StubCase.NONE,
            command = Command.CREATE,
            state = State.FINISHING,
            requestId = RequestId("testRequestId"),
            timeStart = Instant.NONE,
            questionRequest = Question(),
            questionResponse = Question(
                id = Question.QuestionId("testId"),
                ownerId = UserId("testOwnerId"),
                title = "testTitle",
                description = "testDescription",
                createdAt = Instant.fromEpochSeconds(500),
                modifiedAt = Instant.fromEpochSeconds(600)
            ),
            questionsResponse = mutableListOf(),
            errors = mutableListOf()
        ).toTransportQuestion()

        assertEquals(actual = actual, expected = expected)
    }

    @Test
    fun toQuestionQuestionReadResponse() {
        val expected = QuestionReadResponse(
            responseType = "read",
            requestId = "testRequestId",
            result = ResponseResult.ERROR,
            question = QuestionResponseObject(
                id = null,
                ownerId = null,
                title = null,
                description = null
            ),
            errors = listOf(
                Error(
                    code = "testCode",
                    group = "testGroup",
                    `field` = "testField",
                    message = "testMessage"
                )
            )
        )
        val actual = QuestionContext(
            workMode = WorkMode.PROD,
            stubCase = StubCase.NONE,
            command = Command.READ,
            state = State.FAILING,
            requestId = RequestId("testRequestId"),
            timeStart = Instant.NONE,
            questionRequest = Question(),
            questionResponse = Question(
                id = Question.QuestionId.NONE,
                ownerId = UserId.NONE,
                title = "",
                description = "",
                createdAt = Instant.fromEpochSeconds(500),
                modifiedAt = Instant.fromEpochSeconds(600)
            ),
            questionsResponse = mutableListOf(),
            errors = mutableListOf(
                ProcessingError(
                    code = "testCode",
                    level = ProcessingError.Level.ERROR,
                    group = "testGroup",
                    field = "testField",
                    message = "testMessage",
                    exception = NullPointerException()
                )
            )
        ).toTransportQuestion()

        assertEquals(actual = actual, expected = expected)
    }

    @Test
    fun toQuestionQuestionUpdateResponse() {
        val expected = QuestionUpdateResponse(
            responseType = "update",
            requestId = "testRequestId",
            result = ResponseResult.SUCCESS,
            errors = null,
            question = QuestionResponseObject(
                id = "testId",
                ownerId = "testOwnerId",
                title = "testTitle",
                description = "testDescription"
            )
        )
        val actual = QuestionContext(
            workMode = WorkMode.PROD,
            stubCase = StubCase.NONE,
            command = Command.UPDATE,
            state = State.FINISHING,
            requestId = RequestId("testRequestId"),
            timeStart = Instant.NONE,
            questionRequest = Question(),
            questionResponse = Question(
                id = Question.QuestionId("testId"),
                ownerId = UserId("testOwnerId"),
                title = "testTitle",
                description = "testDescription",
                createdAt = Instant.fromEpochSeconds(500),
                modifiedAt = Instant.fromEpochSeconds(600)
            ),
            questionsResponse = mutableListOf(),
            errors = mutableListOf()
        ).toTransportQuestion()

        assertEquals(actual = actual, expected = expected)
    }

    @Test
    fun toQuestionQuestionDeleteResponse() {
        val expected = QuestionDeleteResponse(
            responseType = "delete",
            requestId = "testRequestId",
            result = ResponseResult.SUCCESS,
            errors = null,
            question = QuestionResponseObject(
                id = "testId",
                ownerId = "testOwnerId",
                title = "testTitle",
                description = "testDescription"
            )
        )
        val actual = QuestionContext(
            workMode = WorkMode.PROD,
            stubCase = StubCase.NONE,
            command = Command.DELETE,
            state = State.FINISHING,
            requestId = RequestId("testRequestId"),
            timeStart = Instant.NONE,
            questionRequest = Question(),
            questionResponse = Question(
                id = Question.QuestionId("testId"),
                ownerId = UserId("testOwnerId"),
                title = "testTitle",
                description = "testDescription",
                createdAt = Instant.fromEpochSeconds(500),
                modifiedAt = Instant.fromEpochSeconds(600)
            ),
            questionsResponse = mutableListOf(),
            errors = mutableListOf()
        ).toTransportQuestion()

        assertEquals(actual = actual, expected = expected)
    }

    @Test
    fun toUndefinedResponse() {
        assertFails {
            QuestionContext(
                command = Command.NONE
            ).toTransportQuestion()
        } is UnknownCommandException
    }
}
