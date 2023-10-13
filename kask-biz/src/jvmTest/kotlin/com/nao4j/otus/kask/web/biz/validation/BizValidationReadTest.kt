package com.nao4j.otus.kask.web.biz.validation

import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationReadTest {

    private val command = Command.READ

    private val processor by lazy { QuestionProcessor() }

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

