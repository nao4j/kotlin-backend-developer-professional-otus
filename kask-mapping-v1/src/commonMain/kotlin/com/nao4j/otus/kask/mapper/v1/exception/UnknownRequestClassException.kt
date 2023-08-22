package com.nao4j.otus.kask.mapper.v1.exception

import kotlin.reflect.KClass

class UnknownRequestClassException(clazz: KClass<*>) : RuntimeException("Class $clazz cannot be mapped to MkplContext")
