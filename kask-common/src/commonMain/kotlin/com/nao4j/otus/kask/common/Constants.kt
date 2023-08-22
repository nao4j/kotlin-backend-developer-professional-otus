package com.nao4j.otus.kask.common

import kotlin.Long.Companion.MIN_VALUE
import kotlinx.datetime.Instant

private val INSTANT_NONE = Instant.fromEpochMilliseconds(MIN_VALUE)

val Instant.Companion.NONE
    get() = INSTANT_NONE
