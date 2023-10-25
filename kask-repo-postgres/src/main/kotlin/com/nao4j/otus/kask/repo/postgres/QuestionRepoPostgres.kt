package com.nao4j.otus.kask.repo.postgres

import com.benasher44.uuid.uuid4
import com.nao4j.otus.kask.common.helper.asProcessingError
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.repo.DbQuestionIdRequest
import com.nao4j.otus.kask.common.repo.DbQuestionRequest
import com.nao4j.otus.kask.common.repo.DbQuestionResponse
import com.nao4j.otus.kask.common.repo.QuestionRepository
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class QuestionRepoPostgres(
    properties: PostgresProperties = PostgresProperties(),
    initObjects: Collection<Question> = emptyList(),
    private val randomUuid: () -> String = { uuid4().toString() },
) : QuestionRepository {

    init {
        Database.connect(
            url = "${properties.url}?currentSchema=${properties.schema}",
            driver = when {
                properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
                else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
            },
            user = properties.user,
            password = properties.password
        )

        transaction {
            if (properties.dropDatabase) {
                SchemaUtils.drop(QuestionTable)
            }
            SchemaUtils.create(QuestionTable)
            initObjects.forEach(::createQuestion)
        }
    }

    override suspend fun create(request: DbQuestionRequest): DbQuestionResponse = transactionWrapper {
        val res = QuestionTable.insert { to(it, request.question, randomUuid) }
        val createdQuestion = QuestionTable.from(res)
        DbQuestionResponse.success(createdQuestion)
    }

    override suspend fun read(request: DbQuestionIdRequest): DbQuestionResponse = transactionWrapper {
        val res = QuestionTable.select { QuestionTable.id eq request.id.asString() }.singleOrNull()
            ?: return@transactionWrapper DbQuestionResponse.errorNotFound
        val question = QuestionTable.from(res)
        DbQuestionResponse.success(question)
    }

    override suspend fun update(request: DbQuestionRequest): DbQuestionResponse = transactionWrapper {
        if (request.question.id == Question.QuestionId.NONE) {
            return@transactionWrapper DbQuestionResponse.errorEmptyId
        }
        QuestionTable.select { QuestionTable.id eq request.question.id.asString() }
            .firstOrNull()
            ?.let { QuestionTable.from(it) }
            ?: return@transactionWrapper DbQuestionResponse.errorNotFound
        QuestionTable.update({ (QuestionTable.id eq request.question.id.asString()) }) {
            to(it, request.question, randomUuid)
        }
        val res = QuestionTable.select { QuestionTable.id eq request.question.id.asString() }.singleOrNull()
            ?: return@transactionWrapper DbQuestionResponse.errorNotFound
        val question = QuestionTable.from(res)
        DbQuestionResponse.success(question)
    }

    override suspend fun delete(request: DbQuestionIdRequest): DbQuestionResponse = transactionWrapper {
        if (request.id == Question.QuestionId.NONE) {
            return@transactionWrapper DbQuestionResponse.errorEmptyId
        }
        val current = QuestionTable.select { QuestionTable.id eq request.id.asString() }
            .firstOrNull()
            ?.let { QuestionTable.from(it) }
        when {
            current == null -> DbQuestionResponse.errorNotFound
            else -> {
                QuestionTable.deleteWhere { id eq request.id.asString() }
                DbQuestionResponse.success(current)
            }
        }
    }

    private fun createQuestion(question: Question): Question {
        val res = QuestionTable.insert {
            to(it, question, randomUuid)
        }

        return QuestionTable.from(res)
    }

    private fun <T> transactionWrapper(block: () -> T, handle: (Exception) -> T): T =
        try {
            transaction { block() }
        } catch (e: Exception) {
            handle(e)
        }

    private fun transactionWrapper(block: () -> DbQuestionResponse): DbQuestionResponse =
        transactionWrapper(block) { DbQuestionResponse.error(it.asProcessingError()) }
}
