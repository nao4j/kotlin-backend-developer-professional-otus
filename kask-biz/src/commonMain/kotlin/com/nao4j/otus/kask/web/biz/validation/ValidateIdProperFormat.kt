package com.nao4j.otus.kask.web.biz.validation

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.helper.errorValidation
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker

fun CorChainDsl<QuestionContext>.validateIdProperFormat(title: String) = worker {
    this.title = title

    val regExp = Regex("^[0-9a-zA-Z-]+$")
    on { questionValidating.id != Question.QuestionId.NONE && !questionValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = questionValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
