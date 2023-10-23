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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class BizRepoUpdateTest {

    private val userId = UserId("321")
    private val command = Command.UPDATE

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
            invokeUpdateQuestion = {
                DbQuestionResponse(
                    isSuccess = true,
                    data = Question(
                        id = Question.QuestionId("123"),
                        title = "xyz",
                        description = "xyz"
                    )
                )
            }
        )
    }
    private val settings by lazy { ContextSettings(repoTest = repo) }
    private val processor by lazy { QuestionProcessor(settings) }

    @Test
    fun repoUpdateSuccessTest() = runTest {
        val questionToUpdate = Question(
            id = Question.QuestionId("123"),
            title = "xyz",
            description = "xyz"
        )
        val ctx = QuestionContext(
            command = command,
            state = State.NONE,
            workMode = WorkMode.TEST,
            questionRequest = questionToUpdate
        )

        processor.exec(ctx)

        assertEquals(State.FINISHING, ctx.state)
        assertEquals(questionToUpdate.id, ctx.questionResponse.id)
        assertEquals(questionToUpdate.title, ctx.questionResponse.title)
        assertEquals(questionToUpdate.description, ctx.questionResponse.description)
    }

    @Test
    fun repoUpdateNotFoundTest() =
        repoNotFoundTest(command)
}
