package com.nao4j.otus.kask.web.biz

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.Command
import com.nao4j.otus.kask.cor.rootChain
import com.nao4j.otus.kask.web.biz.group.operation
import com.nao4j.otus.kask.web.biz.group.stubs
import com.nao4j.otus.kask.web.biz.worker.initStatus
import com.nao4j.otus.kask.web.biz.worker.stubCreateSuccess
import com.nao4j.otus.kask.web.biz.worker.stubDbError
import com.nao4j.otus.kask.web.biz.worker.stubDeleteSuccess
import com.nao4j.otus.kask.web.biz.worker.stubNoCase
import com.nao4j.otus.kask.web.biz.worker.stubReadSuccess
import com.nao4j.otus.kask.web.biz.worker.stubUpdateSuccess
import com.nao4j.otus.kask.web.biz.worker.stubValidationBadDescription
import com.nao4j.otus.kask.web.biz.worker.stubValidationBadId
import com.nao4j.otus.kask.web.biz.worker.stubValidationBadTitle

class QuestionProcessor {

    suspend fun exec(ctx: QuestionContext) =
        BusinessChain.exec(ctx)

    companion object {

        private val BusinessChain = rootChain<QuestionContext> {
            initStatus("Инициализация статуса")

            operation("Создание вопрос", Command.CREATE) {
                stubs("Обработка стабов") {
                    stubCreateSuccess("Имитация успешной обработки")
                    stubValidationBadTitle("Имитация ошибки валидации заголовка")
                    stubValidationBadDescription("Имитация ошибки валидации описания")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
            operation("Получить вопрос", Command.READ) {
                stubs("Обработка стабов") {
                    stubReadSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
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
            }
            operation("Удалить вопрос", Command.DELETE) {
                stubs("Обработка стабов") {
                    stubDeleteSuccess("Имитация успешной обработки")
                    stubValidationBadId("Имитация ошибки валидации id")
                    stubDbError("Имитация ошибки работы с БД")
                    stubNoCase("Ошибка: запрошенный стаб недопустим")
                }
            }
        }.build()
    }
}
