package com.nao4j.otus.kask.web.biz.validation

import com.nao4j.otus.kask.common.ContextSettings
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.repo.stub.QuestionRepositoryStub
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import kotlin.test.Test
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class BizValidationCreateTest {

    private val command = Command.CREATE

    private val settings by lazy {
        ContextSettings(
            repoTest = QuestionRepositoryStub()
        )
    }
    private val processor by lazy { QuestionProcessor(settings) }

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

