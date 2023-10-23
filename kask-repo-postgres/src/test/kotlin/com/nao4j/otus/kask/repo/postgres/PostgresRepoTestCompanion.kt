package com.nao4j.otus.kask.repo.postgres

import com.benasher44.uuid.uuid4
import com.nao4j.otus.kask.common.models.Question
import java.time.Duration
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName

class PostgresContainer : PostgreSQLContainer<PostgresContainer>(DockerImageName.parse("postgres:latest"))

object PostgresRepoTestCompanion {

    private const val USER = "postgres"
    private const val PASS = "postgres"
    private const val SCHEMA = "postgres"

    private val container by lazy {
        PostgresContainer().apply {
            withUsername(USER)
            withPassword(PASS)
            withDatabaseName(SCHEMA)
            withStartupTimeout(Duration.ofSeconds(300L))
            start()
        }
    }

    private val url: String by lazy { container.jdbcUrl }

    fun questionRepoUnderTestContainer(
        initObjects: Collection<Question> = emptyList(),
        randomUuid: () -> String = { uuid4().toString() }
    ): QuestionRepoPostgres {
        return QuestionRepoPostgres(
            properties = PostgresProperties(
                url = url,
                user = USER,
                password = PASS,
                schema = SCHEMA,
                dropDatabase = true
            ),
            initObjects = initObjects,
            randomUuid = randomUuid
        )
    }
}
