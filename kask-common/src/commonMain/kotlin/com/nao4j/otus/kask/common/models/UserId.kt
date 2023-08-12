package com.nao4j.otus.kask.common.models

import kotlin.jvm.JvmInline

@JvmInline
value class UserId(private val id: String) {

    fun asString(): String = id

    companion object {
        val NONE = UserId("")
    }
}
