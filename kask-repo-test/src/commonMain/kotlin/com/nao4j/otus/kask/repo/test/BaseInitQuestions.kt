package com.nao4j.otus.kask.repo.test

import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.UserId

abstract class BaseInitQuestions(val operation: String) : InitObjects<Question> {

    fun createInitTestModel(
        suf: String,
        ownerId: UserId = UserId("owner-123")
    ) = Question(
        id = Question.QuestionId("question-repo-$operation-$suf"),
        title = "$suf stub",
        description = "$suf stub description",
        ownerId = ownerId
    )
}
