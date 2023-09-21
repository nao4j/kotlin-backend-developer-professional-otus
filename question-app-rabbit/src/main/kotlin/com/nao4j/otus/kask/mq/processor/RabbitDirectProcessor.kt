package com.nao4j.otus.kask.mq.processor

import com.nao4j.otus.kask.api.v1.apiV1RequestDeserialize
import com.nao4j.otus.kask.api.v1.apiV1ResponseSerialize
import com.nao4j.otus.kask.api.v1.models.IRequest
import com.nao4j.otus.kask.common.QuestionContext
import com.nao4j.otus.kask.common.models.State
import com.nao4j.otus.kask.common.models.asProcessingError
import com.nao4j.otus.kask.mapper.v1.fromTransport
import com.nao4j.otus.kask.mapper.v1.toTransportQuestion
import com.nao4j.otus.kask.mq.config.RabbitConfig
import com.nao4j.otus.kask.mq.config.RabbitExchangeConfiguration
import com.nao4j.otus.kask.mq.config.rabbitLogger
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery

class RabbitDirectProcessor(
    config: RabbitConfig,
    processorConfig: RabbitExchangeConfiguration,
    private val processor: QuestionProcessor,
) : AbstractRabbitProcessor(config, processorConfig) {

    override suspend fun Channel.processMessage(message: Delivery, context: QuestionContext) {
        apiV1RequestDeserialize<IRequest>(String(message.body)).also {
            rabbitLogger.info("TYPE: {}", it::class.java.simpleName)
            context.fromTransport(it)
        }
        rabbitLogger.info("start publish")
        val response = processor.exec(context).run {
            context.toTransportQuestion()
        }
        apiV1ResponseSerialize(response).also {
            rabbitLogger.info("Publishing {} to {} exchange for keyOut {}", response, processorConfig.exchange, processorConfig.keyOut)
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it.toByteArray())
        }.also {
            rabbitLogger.info("published")
        }
    }

    override fun Channel.onError(e: Throwable, context: QuestionContext) {
        e.printStackTrace()
        context.state = State.FAILING
        context.addError(error = arrayOf(e.asProcessingError()))
        val response = context.toTransportQuestion()
        apiV1ResponseSerialize(response).also {
            basicPublish(processorConfig.exchange, processorConfig.keyOut, null, it.toByteArray())
        }
    }
}
