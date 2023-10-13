package com.nao4j.otus.kask.web.biz.validation

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.helper.errorValidation
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker

fun CorChainDsl<QuestionContext>.validateDescriptionHasContent(title: String) = worker {
    this.title = title
    val regExp = Regex("\\p{L}")
    on { questionValidating.description.isNotEmpty() && !questionValidating.description.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "description",
                violationCode = "noContent",
                description = "field must contain letters"
            )
        )
    }
}
