package com.nao4j.otus.kask.web.plugins

import com.nao4j.otus.kask.common.repo.QuestionRepository
import com.nao4j.otus.kask.repo.inmemory.QuestionRepoInMemory
import com.nao4j.otus.kask.repo.postgres.PostgresProperties
import com.nao4j.otus.kask.repo.postgres.QuestionRepoPostgres
import io.ktor.server.application.Application
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

fun Application.initQuestionRepository(dbType: QuestionDbType): QuestionRepository {
    val dbSetting = environment.config.propertyOrNull("app.db.${dbType.configName}.type")?.getString()?.lowercase()
    return when (dbSetting) {
        "in-memory", "inmemory", "memory", "mem" -> initInMemory()
        "postgres", "postgresql", "pg", "sql", "psql" -> initPostgres()
        else -> throw IllegalArgumentException("app.db.type must be set one of: 'in-memory', 'postgres'")
    }
}

enum class QuestionDbType(val configName: String) {
    TEST("test"),
    PROD("prod")
}

private fun Application.initInMemory(): QuestionRepository {
    val ttlSetting = environment.config.propertyOrNull("app.db.ttl")?.getString()?.let { Duration.parse(it) }
    return QuestionRepoInMemory(ttl = ttlSetting ?: 10.minutes)
}

private fun Application.initPostgres(): QuestionRepository {
    return QuestionRepoPostgres(
        properties = PostgresProperties(
            url = environment.config.propertyOrNull("app.db.url")?.getString() ?: throw IllegalArgumentException(),
            user = environment.config.propertyOrNull("app.db.user")?.getString() ?: throw IllegalArgumentException(),
            password = environment.config.propertyOrNull("app.db.password")?.getString() ?: throw IllegalArgumentException(),
            schema = environment.config.propertyOrNull("app.db.schema")?.getString() ?: throw IllegalArgumentException(),
        )
    )
}
