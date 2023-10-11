package com.nao4j.otus.kask.cor.handlers

import com.nao4j.otus.kask.cor.CorChainDsl
import com.nao4j.otus.kask.cor.CorDslMarker
import com.nao4j.otus.kask.cor.CorExec
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CorChain<T>(
    private val execs: List<CorExec<T>>,
    private val handler: suspend (T, List<CorExec<T>>) -> Unit,
    title: String,
    description: String = "",
    blockOn: suspend T.() -> Boolean = { true },
    blockExcept: suspend T.(Throwable) -> Unit = {},
) : AbstractCorExec<T>(title, description, blockOn, blockExcept) {

    override suspend fun handle(context: T) = handler(context, execs)
}

suspend fun <T> executeSequential(context: T, execs: List<CorExec<T>>): Unit =
    execs.forEach {
        it.exec(context)
    }

suspend fun <T> executeParallel(context: T, execs: List<CorExec<T>>): Unit = coroutineScope {
    execs.forEach {
        launch { it.exec(context) }
    }
}

@CorDslMarker
class CorChainDsl<T>(
    private val handler: suspend (T, List<CorExec<T>>) -> Unit = ::executeSequential,
) : CorExecDsl<T>(), CorChainDsl<T> {

    private val workers: MutableList<com.nao4j.otus.kask.cor.CorExecDsl<T>> = mutableListOf()

    override fun add(worker: com.nao4j.otus.kask.cor.CorExecDsl<T>) {
        workers.add(worker)
    }

    override fun build(): CorExec<T> = CorChain(
        title = title,
        description = description,
        execs = workers.map { it.build() },
        handler = handler,
        blockOn = blockOn,
        blockExcept = blockExcept
    )
}
