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
class QuestionCreateStubTest {

    private val processor = QuestionProcessor()
    private val id = Question.QuestionId("666")
    private val title = "title 666"
    private val description = "desc 666"

    @Test
    fun create() = runTest {
        val ctx = QuestionContext(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = StubCase.SUCCESS,
            questionRequest = Question(
                id = id,
                title = title,
                description = description
            )
        )

        processor.exec(ctx)

        assertEquals(QuestionStub.get().id, ctx.questionResponse.id)
        assertEquals(title, ctx.questionResponse.title)
        assertEquals(description, ctx.questionResponse.description)
    }

    @Test
    fun badTitle() = runTest {
        val ctx = QuestionContext(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = StubCase.BAD_TITLE,
            questionRequest = Question(
                id = id,
                title = "",
                description = description
            )
        )

        processor.exec(ctx)

        assertEquals(Question(), ctx.questionResponse)
        assertEquals("title", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badDescription() = runTest {
        val ctx = QuestionContext(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = StubCase.BAD_DESCRIPTION,
            questionRequest = Question(
                id = id,
                title = title,
                description = ""
            )
        )

        processor.exec(ctx)

        assertEquals(Question(), ctx.questionResponse)
        assertEquals("description", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = QuestionContext(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = StubCase.DB_ERROR,
            questionRequest = Question(
                id = id,
            )
        )

        processor.exec(ctx)

        assertEquals(Question(), ctx.questionResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = QuestionContext(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = StubCase.BAD_ID,
            questionRequest = Question(
                id = id,
                title = title,
                description = description
            )
        )

        processor.exec(ctx)

        assertEquals(Question(), ctx.questionResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }
}
