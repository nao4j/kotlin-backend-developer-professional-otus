package com.nao4j.otus.kask.web.biz.repo

import com.nao4j.otus.kask.common.ContextSettings
import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.UserId
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.common.repo.DbQuestionResponse
import com.nao4j.otus.kask.repo.test.QuestionRepositoryMock
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

class BizRepoCreateTest {

    private val userId = UserId("321")
    private val command = Command.CREATE
    private val uuid = "10000000-0000-0000-0000-000000000001"

    private val repo = QuestionRepositoryMock(
        invokeCreateQuestion = {
            DbQuestionResponse(
                isSuccess = true,
                data = Question(
                    id = Question.QuestionId(uuid),
                    title = it.question.title,
                    description = it.question.description,
                    ownerId = userId
                )
            )
        }
    )
    private val settings = ContextSettings(repoTest = repo)
    private val processor = QuestionProcessor(settings)

    @Test
    @OptIn(ExperimentalCoroutinesApi::class)
    fun repoCreateSuccessTest() = runTest {
        val ctx = QuestionContext(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            questionRequest = Question(
                title = "abc",
                description = "abc"
            )
        )

        processor.exec(ctx)

        assertEquals(State.FINISHING, ctx.state)
        assertNotEquals(Question.QuestionId.NONE, ctx.questionResponse.id)
        assertEquals("abc", ctx.questionResponse.title)
        assertEquals("abc", ctx.questionResponse.description)
    }
}
