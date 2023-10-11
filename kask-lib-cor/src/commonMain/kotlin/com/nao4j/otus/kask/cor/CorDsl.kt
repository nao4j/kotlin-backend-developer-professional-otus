package com.nao4j.otus.kask.cor

import com.nao4j.otus.kask.cor.handlers.CorChainDsl
import com.nao4j.otus.kask.cor.handlers.CorWorkerDsl
import com.nao4j.otus.kask.cor.handlers.executeParallel

@DslMarker
annotation class CorDslMarker

@CorDslMarker
interface CorExecDsl<T> {

    var title: String

    var description: String

    fun on(function: suspend T.() -> Boolean)

    fun except(function: suspend T.(e: Throwable) -> Unit)

    fun build(): CorExec<T>
}

@CorDslMarker
interface CorChainDsl<T> : CorExecDsl<T> {

    fun add(worker: CorExecDsl<T>)
}

@CorDslMarker
interface CorWorkerDsl<T> : CorExecDsl<T> {

    fun handle(function: suspend T.() -> Unit)
}

fun <T> rootChain(function: CorChainDsl<T>.() -> Unit): CorChainDsl<T> =
    CorChainDsl<T>().apply(function)

fun <T> CorChainDsl<T>.chain(function: CorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>().apply(function))
}

fun <T> CorChainDsl<T>.parallel(function: CorChainDsl<T>.() -> Unit) {
    add(CorChainDsl<T>(::executeParallel).apply(function))
}

fun <T> CorChainDsl<T>.worker(function: CorWorkerDsl<T>.() -> Unit) {
    add(CorWorkerDsl<T>().apply(function))
}

fun <T> CorChainDsl<T>.worker(
    title: String,
    description: String = "",
    blockHandle: T.() -> Unit
) {
    add(
        CorWorkerDsl<T>().also {
            it.title = title
            it.description = description
            it.handle(blockHandle)
        }
    )
}
