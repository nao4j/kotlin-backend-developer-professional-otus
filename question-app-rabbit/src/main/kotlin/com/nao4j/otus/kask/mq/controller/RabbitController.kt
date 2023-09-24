package com.nao4j.otus.kask.mq.controller

import com.nao4j.otus.kask.mq.config.rabbitLogger
import com.nao4j.otus.kask.mq.processor.AbstractRabbitProcessor
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class RabbitController(
    private val processors: Set<AbstractRabbitProcessor>
) {

    fun start() = runBlocking {
        rabbitLogger.info("start init processors")
        processors.forEach {
            try {
                launch { it.process() }
            } catch (e: RuntimeException) {
                e.printStackTrace()
            }
        }
    }
}
