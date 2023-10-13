package com.nao4j.otus.kask.web.biz.validation

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdCorrect(command: Command, processor: QuestionProcessor) = runTest {
    val ctx = QuestionContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        questionRequest = Question(
            id = Question.QuestionId("123-234-abc-ABC"),
            title = "abc",
            description = "abc"
        )
    )

    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdTrim(command: Command, processor: QuestionProcessor) = runTest {
    val ctx = QuestionContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        questionRequest = Question(
            id = Question.QuestionId(" \n\t 123-234-abc-ABC \n\t "),
            title = "abc",
            description = "abc"
        )
    )

    processor.exec(ctx)

    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdEmpty(command: Command, processor: QuestionProcessor) = runTest {
    val ctx = QuestionContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        questionRequest = Question(
            id = Question.QuestionId(""),
            title = "abc",
            description = "abc"
        )
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationIdFormat(command: Command, processor: QuestionProcessor) = runTest {
    val ctx = QuestionContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        questionRequest = Question(
            id = Question.QuestionId("!@#\$%^&*(),.{}"),
            title = "abc",
            description = "abc"
        )
    )

    processor.exec(ctx)

    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("id", error?.field)
    assertContains(error?.message ?: "", "id")
}
