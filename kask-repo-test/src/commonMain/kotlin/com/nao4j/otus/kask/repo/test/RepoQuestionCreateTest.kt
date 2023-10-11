package com.nao4j.otus.kask.repo.test

import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.UserId
import com.nao4j.otus.kask.common.repo.DbQuestionRequest
import com.nao4j.otus.kask.common.repo.QuestionRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoQuestionCreateTest {

    abstract val repo: QuestionRepository

    private val createObj = Question(
        title = "create object",
        description = "create object description",
        ownerId = UserId("owner-123")
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.create(DbQuestionRequest(createObj))
        val expected = createObj.copy(id = result.data?.id ?: Question.QuestionId.NONE)
        assertEquals(true, result.isSuccess)
        assertEquals(expected.title, result.data?.title)
        assertEquals(expected.description, result.data?.description)
        assertNotEquals(Question.QuestionId.NONE, result.data?.id)
        assertEquals(emptyList(), result.errors)
    }

    companion object : BaseInitQuestions("create") {
        override val initObjects: List<Question> = emptyList()
    }
}
