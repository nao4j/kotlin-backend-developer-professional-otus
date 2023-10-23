package com.nao4j.otus.kask.repo.stub

import com.nao4j.otus.kask.common.repo.DbQuestionIdRequest
import com.nao4j.otus.kask.common.repo.DbQuestionRequest
import com.nao4j.otus.kask.common.repo.DbQuestionResponse
import com.nao4j.otus.kask.common.repo.QuestionRepository
import com.nao4j.otus.kask.web.stub.QuestionStub

class QuestionRepositoryStub : QuestionRepository {

    override suspend fun create(request: DbQuestionRequest): DbQuestionResponse =
        DbQuestionResponse(
            data = QuestionStub.prepareResult { },
            isSuccess = true
        )

    override suspend fun read(request: DbQuestionIdRequest): DbQuestionResponse =
        DbQuestionResponse(
            data = QuestionStub.prepareResult { },
            isSuccess = true
        )

    override suspend fun update(request: DbQuestionRequest): DbQuestionResponse =
        DbQuestionResponse(
            data = QuestionStub.prepareResult { },
            isSuccess = true
        )

    override suspend fun delete(request: DbQuestionIdRequest): DbQuestionResponse =
        DbQuestionResponse(
            data = QuestionStub.prepareResult { },
            isSuccess = true
        )
}
