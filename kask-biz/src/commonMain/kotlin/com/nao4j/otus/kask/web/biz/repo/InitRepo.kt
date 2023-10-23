package com.nao4j.otus.kask.web.biz.repo

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.helper.errorAdministration
import com.nao4j.otus.kask.common.models.WorkMode
import com.nao4j.otus.kask.common.repo.QuestionRepository
import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.worker

fun CorChainDsl<QuestionContext>.initRepo(title: String) = worker {
    this.title = title
    description = "Вычисление основного рабочего репозитория в зависимости от запрошенного режима работы"
    handle {
        questionRepository = when {
            workMode == WorkMode.TEST -> settings.repoTest
            workMode == WorkMode.STUB -> settings.repoStub
            else -> settings.repoProd
        }
        if (workMode != WorkMode.STUB && questionRepository == QuestionRepository.NONE) fail(
            errorAdministration(
                field = "repo",
                violationCode = "dbNotConfigured",
                description = "The database is unconfigured for chosen workmode ($workMode). Please, contact the administrator staff"
            )
        )
    }
}
