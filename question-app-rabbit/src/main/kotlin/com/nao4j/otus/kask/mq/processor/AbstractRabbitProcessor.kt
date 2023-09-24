package com.nao4j.otus.kask.mq.processor

import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.mq.config.RabbitConfig
import com.nao4j.otus.kask.mq.config.RabbitExchangeConfiguration
import com.nao4j.otus.kask.mq.config.rabbitLogger
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.Channel
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import com.rabbitmq.client.Delivery
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

abstract class AbstractRabbitProcessor(
    private val config: RabbitConfig,
    val processorConfig: RabbitExchangeConfiguration
) {

    suspend fun process(dispatcher: CoroutineContext = IO) {
        rabbitLogger.info("create connection")
        withContext(dispatcher) {
            ConnectionFactory().apply {
                host = config.host
                port = config.port
                username = config.user
                password = config.password
            }.newConnection().use { connection ->
                connection.createChannel().use { channel ->
                    val deliveryCallback = channel.getDeliveryCallback()
                    val cancelCallback = getCancelCallback()
                    runBlocking {
                        channel.describeAndListen(deliveryCallback, cancelCallback)
                    }
                }
            }
        }
    }

    protected abstract suspend fun Channel.processMessage(message: Delivery, context: QuestionContext)

    protected abstract fun Channel.onError(e: Throwable, context: QuestionContext)

    private fun Channel.getDeliveryCallback(): DeliverCallback = DeliverCallback { _, message ->
        runBlocking {
            val context = QuestionContext().apply {
                timeStart = Clock.System.now()
            }
            kotlin.runCatching {
                processMessage(message, context)
            }.onFailure {
                onError(it, context)
            }
        }
    }

    private fun getCancelCallback() = CancelCallback {
        rabbitLogger.info("[{}] was cancelled", it)
    }

    private suspend fun Channel.describeAndListen(
        deliverCallback: DeliverCallback,
        cancelCallback: CancelCallback
    ) {
        withContext(IO) {
            rabbitLogger.info("start describing")
            exchangeDeclare(processorConfig.exchange, processorConfig.exchangeType)
            queueDeclare(processorConfig.queueIn, false, false, false, null)
            queueDeclare(processorConfig.queueOut, false, false, false, null)
            queueBind(processorConfig.queueIn, processorConfig.exchange, processorConfig.keyIn)
            queueBind(processorConfig.queueOut, processorConfig.exchange, processorConfig.keyOut)
            basicConsume(processorConfig.queueIn, true, processorConfig.consumerTag, deliverCallback, cancelCallback)
            rabbitLogger.info("finish describing")
            while (isOpen) {
                kotlin.runCatching {
                    delay(100)
                }.onFailure { e ->
                    e.printStackTrace()
                }
            }
            rabbitLogger.info("Channel for [{}] was closed.", processorConfig.consumerTag)
        }
    }
}
