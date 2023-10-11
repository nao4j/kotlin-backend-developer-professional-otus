package com.nao4j.otus.kask.repo.inmemory

import com.benasher44.uuid.uuid4
import com.nao4j.otus.kask.common.models.ProcessingError
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.repo.DbQuestionIdRequest
import com.nao4j.otus.kask.common.repo.DbQuestionRequest
import com.nao4j.otus.kask.common.repo.DbQuestionResponse
import com.nao4j.otus.kask.common.repo.QuestionRepository
import com.nao4j.otus.kask.repo.inmemory.model.QuestionEntity
import io.github.reactivecircus.cache4k.Cache
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class QuestionRepoInMemory(
    initObjects: List<Question> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() }
) : QuestionRepository {

    private val cache = Cache.Builder<String, QuestionEntity>()
        .expireAfterWrite(ttl)
        .build()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    override suspend fun create(request: DbQuestionRequest): DbQuestionResponse {
        val key = randomUuid()
        val ad = request.question.copy(id = Question.QuestionId(key))
        val entity = QuestionEntity(ad)
        cache.put(key, entity)
        return DbQuestionResponse(
            data = ad,
            isSuccess = true
        )
    }

    override suspend fun read(request: DbQuestionIdRequest): DbQuestionResponse {
        val key = request.id.takeIf { it != Question.QuestionId.NONE }?.asString() ?: return resultErrorEmptyId
        return cache.get(key)
            ?.let {
                DbQuestionResponse(
                    data = it.toInternal(),
                    isSuccess = true
                )
            } ?: resultErrorNotFound
    }

    override suspend fun update(request: DbQuestionRequest): DbQuestionResponse {
        val key = request.question.id.takeIf { it != Question.QuestionId.NONE }?.asString() ?: return resultErrorEmptyId
        val newAd = request.question.copy()
        val entity = QuestionEntity(newAd)
        return when (cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.put(key, entity)
                DbQuestionResponse(
                    data = newAd,
                    isSuccess = true
                )
            }
        }
    }

    override suspend fun delete(request: DbQuestionIdRequest): DbQuestionResponse {
        val key = request.id.takeIf { it != Question.QuestionId.NONE }?.asString() ?: return resultErrorEmptyId
        return when (val oldAd = cache.get(key)) {
            null -> resultErrorNotFound
            else -> {
                cache.invalidate(key)
                DbQuestionResponse(
                    data = oldAd.toInternal(),
                    isSuccess = true
                )
            }
        }
    }

    private fun save(question: Question) {
        val entity = QuestionEntity(question)
        if (entity.id == null) {
            return
        }
        cache.put(entity.id, entity)
    }

    companion object {

        val resultErrorEmptyId = DbQuestionResponse(
            data = null,
            isSuccess = false,
            errors = listOf(
                ProcessingError(
                    code = "id-empty",
                    group = "validation",
                    field = "id",
                    message = "Id must not be null or blank"
                )
            )
        )

        val resultErrorNotFound = DbQuestionResponse(
            isSuccess = false,
            data = null,
            errors = listOf(
                ProcessingError(
                    code = "not-found",
                    field = "id",
                    message = "Not Found"
                )
            )
        )
    }
}
