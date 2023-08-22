package com.nao4j.otus.kask.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class RequestId(private val id: String) {

    fun asString() = id

    companion object {
        val NONE = RequestId("")
    }
}
