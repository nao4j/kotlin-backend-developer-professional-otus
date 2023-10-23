package com.nao4j.otus.kask.repo.postgres

import com.nao4j.otus.kask.common.repo.QuestionRepository
import com.nao4j.otus.kask.repo.postgres.PostgresRepoTestCompanion.questionRepoUnderTestContainer
import com.nao4j.otus.kask.repo.test.RepoQuestionCreateTest
import com.nao4j.otus.kask.repo.test.RepoQuestionDeleteTest
import com.nao4j.otus.kask.repo.test.RepoQuestionReadTest
import com.nao4j.otus.kask.repo.test.RepoQuestionUpdateTest

class QuestionRepoPostgresCreateTest : RepoQuestionCreateTest() {

    override val repo: QuestionRepository = questionRepoUnderTestContainer(
        initObjects = initObjects,
        randomUuid = { "20000000-0000-0000-0000-000000000002" }
    )
}

class QuestionRepoPostgresReadTest : RepoQuestionReadTest() {

    override val repo: QuestionRepository = questionRepoUnderTestContainer(
        initObjects = initObjects
    )
}

class QuestionRepoPostgresUpdateTest : RepoQuestionUpdateTest() {

    override val repo: QuestionRepository = questionRepoUnderTestContainer(
        initObjects = initObjects,
        randomUuid = { "20000000-0000-0000-0000-000000000002" }
    )
}

class QuestionRepoPostgresDeleteTest : RepoQuestionDeleteTest() {

    override val repo: QuestionRepository = questionRepoUnderTestContainer(
        initObjects = initObjects
    )
}
