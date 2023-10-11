package com.nao4j.otus.kask.common.repo

import com.nao4j.otus.kask.common.models.ProcessingError

interface DbResponse<T> {
    val data: T?
    val isSuccess: Boolean
    val errors: List<ProcessingError>
}
