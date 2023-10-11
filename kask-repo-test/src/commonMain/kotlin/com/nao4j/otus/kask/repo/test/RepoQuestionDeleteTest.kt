package com.nao4j.otus.kask.repo.test

import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.repo.DbQuestionIdRequest
import com.nao4j.otus.kask.common.repo.QuestionRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoQuestionDeleteTest {

    abstract val repo: QuestionRepository

    protected open val deleteSuccess = initObjects[0]

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.delete(DbQuestionIdRequest(deleteSuccess.id))
        assertEquals(true, result.isSuccess)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun deleteNotFound() = runRepoTest {
        val result = repo.read(DbQuestionIdRequest(notFoundId))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitQuestions("delete") {
        override val initObjects: List<Question> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteLock"),
        )
        val notFoundId = Question.QuestionId("question-repo-delete-notFound")
    }
}
