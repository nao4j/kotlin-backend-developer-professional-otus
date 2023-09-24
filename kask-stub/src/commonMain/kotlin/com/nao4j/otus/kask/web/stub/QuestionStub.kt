package com.nao4j.otus.kask.web.stub

import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.UserId
import kotlinx.datetime.Instant

object QuestionStub {

    fun get(id: Question.QuestionId): Question =
        if (id != Question.QuestionId.NONE) {
            QUESTION.copy(id = id)
        } else {
            QUESTION.copy(id = Question.QuestionId("64"))
        }

    private val QUESTION = Question(
        id = Question.QuestionId("42"),
        title = "Здравствуйте! Это канал про аниме?",
        description = "Как пропатчить KDE под FreeBSD?",
        ownerId = UserId("nao4j"),
        createdAt = Instant.parse("2007-01-06T13:44:05+03:00"),
        modifiedAt = Instant.parse("2007-01-06T21:04:14+03:00")
    )
}
