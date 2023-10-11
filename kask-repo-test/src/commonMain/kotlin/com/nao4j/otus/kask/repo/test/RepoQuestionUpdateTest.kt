package com.nao4j.otus.kask.repo.test

import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.UserId
import com.nao4j.otus.kask.common.repo.DbQuestionRequest
import com.nao4j.otus.kask.common.repo.QuestionRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
abstract class RepoQuestionUpdateTest {

    abstract val repo: QuestionRepository

    protected open val updateSuccess = initObjects[0]

    private val updateIdNotFound = Question.QuestionId("ad-repo-update-not-found")

    private val requestUpdateSuccess by lazy {
        Question(
            id = updateSuccess.id,
            title = "update object",
            description = "update object description",
            ownerId = UserId("owner-123")
        )
    }

    private val requestUpdateNotFound = Question(
        id = updateIdNotFound,
        title = "update object not found",
        description = "update object not found description",
        ownerId = UserId("owner-123")
    )

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.update(DbQuestionRequest(requestUpdateSuccess))
        assertEquals(true, result.isSuccess)
        assertEquals(requestUpdateSuccess.id, result.data?.id)
        assertEquals(requestUpdateSuccess.title, result.data?.title)
        assertEquals(requestUpdateSuccess.description, result.data?.description)
        assertEquals(emptyList(), result.errors)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.update(DbQuestionRequest(requestUpdateNotFound))
        assertEquals(false, result.isSuccess)
        assertEquals(null, result.data)
        val error = result.errors.find { it.code == "not-found" }
        assertEquals("id", error?.field)
    }

    companion object : BaseInitQuestions("update") {
        override val initObjects: List<Question> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateConc"),
        )
    }
}
