package com.nao4j.otus.kask.web.biz

import com.nao4j.otus.kask.common.ContextSettings
import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.common.models.Question
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.cor.chain
import com.nao4j.otus.kask.cor.rootChain
import com.nao4j.otus.kask.cor.worker
import com.nao4j.otus.kask.web.biz.group.operation
import com.nao4j.otus.kask.web.biz.group.stubs
import com.nao4j.otus.kask.web.biz.repo.initRepo
import com.nao4j.otus.kask.web.biz.repo.repoCreate
import com.nao4j.otus.kask.web.biz.repo.repoDelete
import com.nao4j.otus.kask.web.biz.repo.repoPrepareCreate
import com.nao4j.otus.kask.web.biz.repo.repoPrepareDelete
import com.nao4j.otus.kask.web.biz.repo.repoPrepareUpdate
import com.nao4j.otus.kask.web.biz.repo.repoRead
import com.nao4j.otus.kask.web.biz.repo.repoUpdate
import com.nao4j.otus.kask.web.biz.validation.finishQuestionValidation
import com.nao4j.otus.kask.web.biz.validation.validateDescriptionHasContent
import com.nao4j.otus.kask.web.biz.validation.validateDescriptionNotEmpty
import com.nao4j.otus.kask.web.biz.validation.validateIdNotEmpty
import com.nao4j.otus.kask.web.biz.validation.validateIdProperFormat
import com.nao4j.otus.kask.web.biz.validation.validateTitleHasContent
import com.nao4j.otus.kask.web.biz.validation.validateTitleNotEmpty
import com.nao4j.otus.kask.web.biz.validation.validation
import com.nao4j.otus.kask.web.biz.worker.initStatus
import com.nao4j.otus.kask.web.biz.worker.prepareResult
import com.nao4j.otus.kask.web.biz.worker.stubCreateSuccess
import com.nao4j.otus.kask.web.biz.worker.stubDbError
import com.nao4j.otus.kask.web.biz.worker.stubDeleteSuccess
import com.nao4j.otus.kask.web.biz.worker.stubNoCase
import com.nao4j.otus.kask.web.biz.worker.stubReadSuccess
import com.nao4j.otus.kask.web.biz.worker.stubUpdateSuccess
import com.nao4j.otus.kask.web.biz.worker.stubValidationBadDescription
import com.nao4j.otus.kask.web.biz.worker.stubValidationBadId
import com.nao4j.otus.kask.web.biz.worker.stubValidationBadTitle

class QuestionProcessor(val settings: ContextSettings = ContextSettings()) {

    suspend fun exec(ctx: QuestionContext) =
        BusinessChain.exec(ctx.apply { this.settings = this@QuestionProcessor.settings })

    companion object {

        private val BusinessChain = rootChain<QuestionContext> {
            initStatus("Инициализация статуса")
            initRepo("Инициализация репозитория")

            operation("Создание вопрос", Command.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в questionValidating") { questionValidating = questionRequest.copy() }
                    worker("Очистка id") { questionValidating.id = Question.QuestionId.NONE }
                    worker("Очистка заголовка") { questionValidating.title = questionValidating.title.trim() }
                    worker("Очистка описания") { questionValidating.description = questionValidating.description.trim() }
                    validateTitleNotEmpty("Проверка, что заголовок не пуст")
                    validateTitleHasContent("Проверка символов")
                    validateDescriptionNotEmpty("Проверка, что описание не пусто")
                    validateDescriptionHasContent("Проверка символов")

                    finishQuestionValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика сохранения"
                    repoPrepareCreate("Подготовка объекта для сохранения")
                    repoCreate("Создание объявления в БД")
                }
                prepareResult("Подготовка ответа")
            }
            operation("Получить вопрос", Command.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в questionValidating") { questionValidating = questionRequest.copy() }
                    worker("Очистка id") { questionValidating.id = Question.QuestionId(questionValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishQuestionValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика чтения"
                    repoRead("Чтение объявления из БД")
                    worker {
                        title = "Подготовка ответа для Read"
                        on { state == State.RUNNING }
                        handle { questionRepositoryDone = questionRepositoryRead }
                    }
                }
                prepareResult("Подготовка ответа")
            }
            operation("Изменить вопрос", Command.UPDATE) {
                stubs("Обработка стабов") {
                    stubUpdateSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в questionValidating") { questionValidating = questionRequest.copy() }
                    worker("Очистка id") { questionValidating.id = Question.QuestionId(questionValidating.id.asString().trim()) }
                    worker("Очистка заголовка") { questionValidating.title = questionValidating.title.trim() }
                    worker("Очистка описания") { questionValidating.description = questionValidating.description.trim() }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")
                    validateTitleNotEmpty("Проверка на непустой заголовок")
                    validateTitleHasContent("Проверка на наличие содержания в заголовке")
                    validateDescriptionNotEmpty("Проверка на непустое описание")
                    validateDescriptionHasContent("Проверка на наличие содержания в описании")

                    finishQuestionValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика сохранения"
                    repoRead("Чтение объявления из БД")
                    repoPrepareUpdate("Подготовка объекта для обновления")
                    repoUpdate("Обновление объявления в БД")
                }
                prepareResult("Подготовка ответа")
            }
            operation("Удалить вопрос", Command.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
                validation {
                    worker("Копируем поля в questionValidating") { questionValidating = questionRequest.copy() }
                    worker("Очистка id") { questionValidating.id = Question.QuestionId(questionValidating.id.asString().trim()) }
                    validateIdNotEmpty("Проверка на непустой id")
                    validateIdProperFormat("Проверка формата id")

                    finishQuestionValidation("Успешное завершение процедуры валидации")
                }
                chain {
                    title = "Логика удаления"
                    repoRead("Чтение объявления из БД")
                    repoPrepareDelete("Подготовка объекта для удаления")
                    repoDelete("Удаление объявления из БД")
                }
                prepareResult("Подготовка ответа")
            }
        }.build()
    }
}
