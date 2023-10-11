package com.nao4j.otus.kask.web.biz.validation

import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationCreateTest {

    private val command = Command.CREATE

    private val processor by lazy { QuestionProcessor() }

    @Test
    fun correctTitle() =
        validationTitleCorrect(command, processor)

    @Test
    fun trimTitle() =
        validationTitleTrim(command, processor)

    @Test
    fun emptyTitle() =
        validationTitleEmpty(command, processor)

    @Test
    fun badSymbolsTitle() =
        validationTitleSymbols(command, processor)

    @Test
    fun correctDescription() =
        validationDescriptionCorrect(command, processor)

    @Test
    fun trimDescription() =
        validationDescriptionTrim(command, processor)

    @Test
    fun emptyDescription() =
        validationDescriptionEmpty(command, processor)

    @Test
    fun badSymbolsDescription() =
        validationDescriptionSymbols(command, processor)
}

