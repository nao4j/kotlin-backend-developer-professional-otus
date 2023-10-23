package com.nao4j.otus.kask.repo.test

import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.repo.DbQuestionIdRequest
import com.nao4j.otus.kask.common.repo.QuestionRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoQuestionReadTest {

    abstract val repo: QuestionRepository

    protected open val readSuccess = initObjects[0]

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.read(DbQuestionIdRequest(readSuccess.id))
        assertEquals(true, result.isSuccess)
        assertEquals(readSuccess, result.data)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun readNotFound() = runRepoTest {
        val result = repo.read(DbQuestionIdRequest(notFoundId))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitQuestions("read") {
        override val initObjects: List<Question> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = Question.QuestionId("question-repo-read-notFound")
    }
}
