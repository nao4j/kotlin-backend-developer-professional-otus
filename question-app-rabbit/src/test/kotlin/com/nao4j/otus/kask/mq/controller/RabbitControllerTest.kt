package com.nao4j.otus.kask.mq.controller

import com.nao4j.otus.kask.api.v1.apiV1RequestSerialize
import com.nao4j.otus.kask.api.v1.apiV1ResponseDeserialize
import com.nao4j.otus.kask.api.v1.models.QuestionCreateObject
import com.nao4j.otus.kask.api.v1.models.QuestionCreateRequest
import com.nao4j.otus.kask.api.v1.models.QuestionCreateResponse
import com.nao4j.otus.kask.api.v1.models.QuestionDebug
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteObject
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteRequest
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteResponse
import com.nao4j.otus.kask.api.v1.models.QuestionReadObject
import com.nao4j.otus.kask.api.v1.models.QuestionReadRequest
import com.nao4j.otus.kask.api.v1.models.QuestionReadResponse
import com.nao4j.otus.kask.api.v1.models.QuestionRequestDebugMode
import com.nao4j.otus.kask.api.v1.models.QuestionRequestDebugStubs
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateObject
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateRequest
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateResponse
import com.nao4j.otus.kask.mq.config.RabbitConfig
import com.nao4j.otus.kask.mq.config.RabbitConfig.Companion.RABBIT_PASSWORD
import com.nao4j.otus.kask.mq.config.RabbitConfig.Companion.RABBIT_USER
import com.nao4j.otus.kask.mq.config.RabbitExchangeConfiguration
import com.nao4j.otus.kask.mq.processor.RabbitDirectProcessor
import com.nao4j.otus.kask.web.biz.QuestionProcessor
import com.nao4j.otus.kask.web.stub.QuestionStub
import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.containers.wait.strategy.Wait

class RabbitControllerTest {

    companion object {
        const val EXCHANGE_TYPE = "direct"
        const val TRANSPORT_EXCHANGE = "transport-exchange-v1"
    }

    private val container by lazy {
        RabbitMQContainer("rabbitmq:3-management-alpine").apply {
            withExposedPorts(5672, 15672)
            withUser(RABBIT_USER, RABBIT_PASSWORD)
            waitingFor(Wait.forLogMessage(".*Server startup complete;.*", 1))
            start()
        }
    }

    private val config by lazy {
        RabbitConfig(
            host = container.host,
            port = container.getMappedPort(5672)
        )
    }

    private val processorConfig = RabbitExchangeConfiguration(
        keyIn = "in-v1",
        keyOut = "out-v1",
        exchange = TRANSPORT_EXCHANGE,
        queueIn = "v1-queue",
        queueOut = "v1-queue-out",
        consumerTag = "v1-consumer",
        exchangeType = EXCHANGE_TYPE
    )

    private val processor by lazy {
        RabbitDirectProcessor(
            config = config,
            processorConfig = processorConfig,
            processor = QuestionProcessor()
        )
    }

    private val controller by lazy {
        RabbitController(
            processors = setOf(processor)
        )
    }

    @BeforeTest
    fun setUp() {
        println("init controller")
        GlobalScope.launch {
            controller.start()
        }
        Thread.sleep(6000)
        println("controller initiated")
    }

    @Test
    fun questionCreateTest() {
        val processorConfig = processor.processorConfig
        val keyIn = processorConfig.keyIn
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = config.user
            password = config.password
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(processorConfig.exchange, EXCHANGE_TYPE)
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, processorConfig.exchange, processorConfig.keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(processorConfig.exchange, keyIn, null, apiV1RequestSerialize(create).toByteArray())
                Thread.sleep(3000)

                println("RESPONSE: $responseJson")
                val response = apiV1ResponseDeserialize<QuestionCreateResponse>(responseJson)
                val expected = QuestionStub.get()
                assertEquals(expected.title, response.question?.title)
                assertEquals(expected.description, response.question?.description)
            }
        }
    }

    @Test
    fun questionReadTest() {
        val processorConfig = processor.processorConfig
        val keyIn = processorConfig.keyIn
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = config.user
            password = config.password
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(processorConfig.exchange, EXCHANGE_TYPE)
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, processorConfig.exchange, processorConfig.keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(processorConfig.exchange, keyIn, null, apiV1RequestSerialize(read).toByteArray())
                Thread.sleep(3000)

                println("RESPONSE: $responseJson")
                val response = apiV1ResponseDeserialize<QuestionReadResponse>(responseJson)
                val expected = QuestionStub.get()
                assertEquals(expected.id.asString(), response.question?.id)
                assertEquals(expected.title, response.question?.title)
                assertEquals(expected.description, response.question?.description)
            }
        }
    }

    @Test
    fun questionUpdateTest() {
        val processorConfig = processor.processorConfig
        val keyIn = processorConfig.keyIn
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = config.user
            password = config.password
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(processorConfig.exchange, EXCHANGE_TYPE)
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, processorConfig.exchange, processorConfig.keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(processorConfig.exchange, keyIn, null, apiV1RequestSerialize(update).toByteArray())
                Thread.sleep(3000)

                println("RESPONSE: $responseJson")
                val response = apiV1ResponseDeserialize<QuestionUpdateResponse>(responseJson)
                val expected = QuestionStub.get()
                assertEquals(expected.id.asString(), response.question?.id)
                assertEquals(expected.title, response.question?.title)
                assertEquals(expected.description, response.question?.description)
            }
        }
    }

    @Test
    fun questionDeleteTest() {
        val processorConfig = processor.processorConfig
        val keyIn = processorConfig.keyIn
        ConnectionFactory().apply {
            host = config.host
            port = config.port
            username = config.user
            password = config.password
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(processorConfig.exchange, EXCHANGE_TYPE)
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, processorConfig.exchange, processorConfig.keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(processorConfig.exchange, keyIn, null, apiV1RequestSerialize(delete).toByteArray())
                Thread.sleep(3000)

                println("RESPONSE: $responseJson")
                val response = apiV1ResponseDeserialize<QuestionDeleteResponse>(responseJson)
                val expected = QuestionStub.get()
                assertEquals(expected.id.asString(), response.question?.id)
                assertEquals(expected.title, response.question?.title)
                assertEquals(expected.description, response.question?.description)
            }
        }
    }

    private val create = with(QuestionStub.get()) {
        QuestionCreateRequest(
            requestType = "create",
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS
            ),
            question = QuestionCreateObject(
                title = title,
                description = description
            )
        )
    }

    private val read = with(QuestionStub.get()) {
        QuestionReadRequest(
            requestType = "read",
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS
            ),
            question = QuestionReadObject(
                id = id.asString()
            )
        )
    }

    private val update = with(QuestionStub.get()) {
        QuestionUpdateRequest(
            requestType = "update",
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS
            ),
            question = QuestionUpdateObject(
                id = id.asString(),
                title = title,
                description = description
            )
        )
    }

    private val delete = with(QuestionStub.get()) {
        QuestionDeleteRequest(
            requestType = "delete",
            debug = QuestionDebug(
                mode = QuestionRequestDebugMode.STUB,
                stub = QuestionRequestDebugStubs.SUCCESS
            ),
            question = QuestionDeleteObject(
                id = id.asString()
            )
        )
    }
}
