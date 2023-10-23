package com.nao4j.otus.kask.repo.test

import com.nao4j.otus.kask.common.repo.DbQuestionIdRequest
import com.nao4j.otus.kask.common.repo.DbQuestionRequest
import com.nao4j.otus.kask.common.repo.DbQuestionResponse
import com.nao4j.otus.kask.common.repo.QuestionRepository

class QuestionRepositoryMock(
    private val invokeCreateQuestion: (DbQuestionRequest) -> DbQuestionResponse = { DbQuestionResponse.MOCK_SUCCESS_EMPTY },
    private val invokeReadQuestion: (DbQuestionIdRequest) -> DbQuestionResponse = { DbQuestionResponse.MOCK_SUCCESS_EMPTY },
    private val invokeUpdateQuestion: (DbQuestionRequest) -> DbQuestionResponse = { DbQuestionResponse.MOCK_SUCCESS_EMPTY },
    private val invokeDeleteQuestion: (DbQuestionIdRequest) -> DbQuestionResponse = { DbQuestionResponse.MOCK_SUCCESS_EMPTY }
): QuestionRepository {

    override suspend fun create(request: DbQuestionRequest): DbQuestionResponse =
        invokeCreateQuestion(request)

    override suspend fun read(request: DbQuestionIdRequest): DbQuestionResponse =
        invokeReadQuestion(request)

    override suspend fun update(request: DbQuestionRequest): DbQuestionResponse =
        invokeUpdateQuestion(request)

    override suspend fun delete(request: DbQuestionIdRequest): DbQuestionResponse =
        invokeDeleteQuestion(request)
}
