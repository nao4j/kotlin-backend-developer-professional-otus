package com.nao4j.otus.kask.common.models

import com.nao4j.otus.kask.common.NONE
import com.nao4j.otus.kask.common.models.Question.QuestionId.Companion.NONE
import kotlin.jvm.JvmInline
import kotlinx.datetime.Instant

data class Question(
    var id: QuestionId = NONE,
    var title: String = "",
    var description: String = "",
    var ownerId: UserId = UserId.NONE,
    var createdAt: Instant = Instant.NONE,
    var modifiedAt: Instant = Instant.NONE
) {

    @JvmInline
    value class QuestionId(private val id: String) {

        fun asString(): String = id

        companion object {
            val NONE = QuestionId("")
        }
    }
}
