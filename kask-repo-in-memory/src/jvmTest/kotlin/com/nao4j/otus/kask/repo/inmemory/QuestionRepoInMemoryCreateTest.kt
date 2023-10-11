package com.nao4j.otus.kask.repo.inmemory

import com.nao4j.otus.kask.common.repo.QuestionRepository
import com.nao4j.otus.kask.repo.test.RepoQuestionCreateTest

class QuestionRepoInMemoryCreateTest : RepoQuestionCreateTest() {

    override val repo: QuestionRepository = QuestionRepoInMemory(
        initObjects = initObjects
    )
}
