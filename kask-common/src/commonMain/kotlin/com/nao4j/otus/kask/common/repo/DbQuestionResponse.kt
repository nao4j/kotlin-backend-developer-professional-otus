package com.nao4j.otus.kask.common.repo

import com.nao4j.otus.kask.common.models.ProcessingError
import com.nao4j.otus.kask.common.models.Question

data class DbQuestionResponse(
    override val data: Question?,
    override val isSuccess: Boolean,
    override val errors: List<ProcessingError> = emptyList()
) : DbResponse<Question> {

    companion object {

        val MOCK_SUCCESS_EMPTY = DbQuestionResponse(null, true)

        fun success(result: Question) = DbQuestionResponse(result, true)

        fun error(errors: List<ProcessingError>) = DbQuestionResponse(null, false, errors)

        fun error(error: ProcessingError) = DbQuestionResponse(null, false, listOf(error))
    }
}
