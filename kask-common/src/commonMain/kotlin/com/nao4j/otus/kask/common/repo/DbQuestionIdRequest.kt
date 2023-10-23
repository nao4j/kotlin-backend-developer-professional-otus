package com.nao4j.otus.kask.common.repo

import com.nao4j.otus.kask.common.models.Question

data class DbQuestionIdRequest(
    val id: Question.QuestionId
) {
    constructor(question: Question) : this(question.id)
}
