package com.nao4j.otus.kask.web.biz.validation

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import com.nao4j.otus.kask.web.stub.QuestionStub
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

private val stub = QuestionStub.get()

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleCorrect(command: Command, processor: QuestionProcessor) = runTest {
    val ctx = QuestionContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        questionRequest = Question(
            id = stub.id,
            title = "abc",
            description = "abc"
        )
    )
    
    processor.exec(ctx)
    
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("abc", ctx.questionValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleTrim(command: Command, processor: QuestionProcessor) = runTest {
    val ctx = QuestionContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        questionRequest = Question(
            id = stub.id,
            title = " \n\t abc \t\n ",
            description = "abc"
        )
    )
    
    processor.exec(ctx)
    
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("abc", ctx.questionValidated.title)
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleEmpty(command: Command, processor: QuestionProcessor) = runTest {
    val ctx = QuestionContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        questionRequest = Question(
            id = stub.id,
            title = "",
            description = "abc"
        )
    )
    
    processor.exec(ctx)
    
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}

@OptIn(ExperimentalCoroutinesApi::class)
fun validationTitleSymbols(command: Command, processor: QuestionProcessor) = runTest {
    val ctx = QuestionContext(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        questionRequest = Question(
            id = Question.QuestionId("123"),
            title = "!@#$%^&*(),.{}",
            description = "abc"
        )
    )
    
    processor.exec(ctx)
    
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    val error = ctx.errors.firstOrNull()
    assertEquals("title", error?.field)
    assertContains(error?.message ?: "", "title")
}
