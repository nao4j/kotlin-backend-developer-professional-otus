package com.nao4j.otus.kask.mapper.v1.exception

import com.nao4j.otus.kask.common.models.Command

class UnknownCommandException(command: Command) : Throwable("Wrong command $command at mapping toTransport stage")
