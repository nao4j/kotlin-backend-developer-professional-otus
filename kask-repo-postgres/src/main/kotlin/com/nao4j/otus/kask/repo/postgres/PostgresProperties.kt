package com.nao4j.otus.kask.repo.postgres

data class PostgresProperties(
    val url: String = "jdbc:postgresql://localhost:5432/postgres",
    val user: String = "postgres",
    val password: String = "postgres",
    val schema: String = "postgres",
    val dropDatabase: Boolean = false
)
