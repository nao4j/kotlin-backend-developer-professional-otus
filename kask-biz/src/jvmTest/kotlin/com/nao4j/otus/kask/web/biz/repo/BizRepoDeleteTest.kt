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
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoDeleteTest {

    private val userId = UserId("321")
    private val command = Command.DELETE

    private val initQuestion = Question(
        id = Question.QuestionId("123"),
        title = "abc",
        description = "abc",
        ownerId = userId
    )
    private val repo by lazy {
        QuestionRepositoryMock(
            invokeReadQuestion = {
                DbQuestionResponse(
                    isSuccess = true,
                    data = initQuestion
                )
            },
            invokeDeleteQuestion = {
                if (it.id == initQuestion.id)
                    DbQuestionResponse(
                        isSuccess = true,
                        data = initQuestion
                    )
                else DbQuestionResponse(isSuccess = false, data = null)
            }
        )
    }
    private val settings by lazy { ContextSettings(repoTest = repo) }
    private val processor by lazy { QuestionProcessor(settings) }

    @Test
    fun repoDeleteSuccessTest() = runTest {
        val questionToUpdate = Question(
            id = Question.QuestionId("123")
        )
        val ctx = QuestionContext(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            questionRequest = questionToUpdate
        )

        processor.exec(ctx)

        assertEquals(State.FINISHING, ctx.state)
        assertTrue { ctx.errors.isEmpty() }
        assertEquals(initQuestion.id, ctx.questionResponse.id)
        assertEquals(initQuestion.title, ctx.questionResponse.title)
        assertEquals(initQuestion.description, ctx.questionResponse.description)
    }

    @Test
    fun repoDeleteNotFoundTest() =
        repoNotFoundTest(command)
}
