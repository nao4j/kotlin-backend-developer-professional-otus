package com.nao4j.otus.kask.mq.config

data class RabbitExchangeConfiguration(
    val keyIn: String,
    val keyOut: String,
    val exchange: String,
    val queueIn: String,
    val queueOut: String,
    val consumerTag: String,
    val exchangeType: String
)
