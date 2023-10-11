package com.nao4j.otus.kask.repo.inmemory

import com.nao4j.otus.kask.common.repo.QuestionRepository
import com.nao4j.otus.kask.repo.test.RepoQuestionUpdateTest

class QuestionRepoInMemoryUpdateTest : RepoQuestionUpdateTest() {

    override val repo: QuestionRepository = QuestionRepoInMemory(
        initObjects = initObjects
    )
}
