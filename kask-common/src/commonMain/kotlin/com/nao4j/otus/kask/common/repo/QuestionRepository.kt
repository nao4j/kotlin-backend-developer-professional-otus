package com.nao4j.otus.kask.common.repo

interface QuestionRepository {

    suspend fun create(request: DbQuestionRequest): DbQuestionResponse

    suspend fun read(request: DbQuestionIdRequest): DbQuestionResponse

    suspend fun update(request: DbQuestionRequest): DbQuestionResponse

    suspend fun delete(request: DbQuestionIdRequest): DbQuestionResponse

    companion object {

        val NONE = object : QuestionRepository {

            override suspend fun create(request: DbQuestionRequest): DbQuestionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun read(request: DbQuestionIdRequest): DbQuestionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun update(request: DbQuestionRequest): DbQuestionResponse {
                TODO("Not yet implemented")
            }

            override suspend fun delete(request: DbQuestionIdRequest): DbQuestionResponse {
                TODO("Not yet implemented")
            }
        }
    }
}
