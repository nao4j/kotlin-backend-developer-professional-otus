package com.nao4j.otus.kask.cor

interface CorExec<T> {

    val title: String

    val description: String

    suspend fun exec(context: T)
}
