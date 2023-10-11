package com.nao4j.otus.kask.web.biz.validation

import com.nao4j.otus.kask.common.ContextSettings
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.repo.stub.QuestionRepositoryStub
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationReadTest {

    private val command = Command.READ

    private val settings by lazy {
        ContextSettings(
            repoTest = QuestionRepositoryStub()
        )
    }
    private val processor by lazy { QuestionProcessor(settings) }

    @Test
    fun correctId() =
        validationIdCorrect(command, processor)

    @Test
    fun trimId() =
        validationIdTrim(command, processor)

    @Test
    fun emptyId() =
        validationIdEmpty(command, processor)

    @Test
    fun badFormatId() =
        validationIdFormat(command, processor)
}

