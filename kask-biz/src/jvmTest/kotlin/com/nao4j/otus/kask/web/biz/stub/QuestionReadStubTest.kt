package com.nao4j.otus.kask.web.biz.stub

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.common.stubs.StubCase
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import com.nao4j.otus.kask.web.stub.QuestionStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class QuestionReadStubTest {

    private val processor = QuestionProcessor()
    private val id = Question.QuestionId("666")

    @Test
    fun read() = runTest {
        val ctx = QuestionContext(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = StubCase.SUCCESS,
            questionRequest = Question(
                id = id
            )
        )
        
        processor.exec(ctx)
        
        with (QuestionStub.get()) {
            assertEquals(id, ctx.questionResponse.id)
            assertEquals(title, ctx.questionResponse.title)
            assertEquals(description, ctx.questionResponse.description)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = QuestionContext(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = StubCase.BAD_ID,
            questionRequest = Question()
        )
        
        processor.exec(ctx)
        
        assertEquals(Question(), ctx.questionResponse)
        assertEquals("id", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = QuestionContext(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = StubCase.DB_ERROR,
            questionRequest = Question(
                id = id
            )
        )
        
        processor.exec(ctx)
        
        assertEquals(Question(), ctx.questionResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = QuestionContext(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = StubCase.BAD_TITLE,
            questionRequest = Question(
                id = id
            )
        )
        
        processor.exec(ctx)
        
        assertEquals(Question(), ctx.questionResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
