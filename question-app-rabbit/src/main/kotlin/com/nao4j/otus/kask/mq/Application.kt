package com.nao4j.otus.kask.mq

import com.nao4j.otus.kask.mq.config.RabbitConfig
import com.nao4j.otus.kask.mq.config.RabbitExchangeConfiguration
import com.nao4j.otus.kask.mq.controller.RabbitController
import com.nao4j.otus.kask.mq.processor.RabbitDirectProcessor
import com.nao4j.otus.kask.web.biz.QuestionProcessor

fun main() {
    val config = RabbitConfig()
    val questionProcessor = QuestionProcessor()
    val processorConfig = RabbitExchangeConfiguration(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = "transport-exchange-v1",
        queueIn = "v1-queue",
        queueOut= "v1-queue-out",
        consumerTag = "v1-consumer",
        exchangeType = "direct"
    )

    val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = processorConfig,
            processor = questionProcessor
        )
    }
    val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }

    controller.start()
}
