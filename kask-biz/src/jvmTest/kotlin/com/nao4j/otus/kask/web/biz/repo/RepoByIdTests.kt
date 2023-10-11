package com.nao4j.otus.kask.web.biz.repo

import com.nao4j.otus.kask.common.ContextSettings
import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.ProcessingError
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.common.repo.DbQuestionResponse
import com.nao4j.otus.kask.repo.test.QuestionRepositoryMock
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

private val initQuestion = Question(
    id = Question.QuestionId("123"),
    title = "abc",
    description = "abc"
)
private val repo = QuestionRepositoryMock(
    invokeReadQuestion = {
        if (it.id == initQuestion.id) {
            DbQuestionResponse(
                isSuccess = true,
                data = initQuestion,
            )
        } else DbQuestionResponse(
            isSuccess = false,
            data = null,
            errors = listOf(ProcessingError(message = "Not found", field = "id"))
        )
    }
)
private val settings by lazy { ContextSettings(repoTest = repo) }
private val processor by lazy { QuestionProcessor(settings) }

@OptIn(ExperimentalCoroutinesApi::class)
fun repoNotFoundTest(command: Command) = runTest {
    val ctx = QuestionContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        questionRequest = Question(
            id = Question.QuestionId("12345"),
            title = "xyz",
            description = "xyz"
        )
    )

    processor.exec(ctx)

    assertEquals(State.FAILING, ctx.state)
    assertEquals(Question(), ctx.questionResponse)
    assertEquals(1, ctx.errors.size)
    assertEquals("id", ctx.errors.first().field)
}
