package com.nao4j.otus.kask.repo.inmemory.model

import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.UserId

data class QuestionEntity(
    val id: String? = null,
    val title: String? = null,
    val description: String? = null,
    val ownerId: String? = null
) {

    constructor(model: Question) : this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        title = model.title.takeIf { it.isNotBlank() },
        description = model.description.takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() }
    )

    fun toInternal() = Question(
        id = id?.let { Question.QuestionId(it) } ?: Question.QuestionId.NONE,
        title = title ?: "",
        description = description ?: "",
        ownerId = ownerId?.let { UserId(it) }?: UserId.NONE
    )
}
