package com.nao4j.otus.kask.common.helper

import com.nao4j.otus.kask.common.models.ProcessingError

val errorEmptyId = ProcessingError(
    field = "id",
    message = "Id must not be null or blank"
)

val errorNotFound = ProcessingError(
    field = "id",
    message = "Not Found",
    code = "not-found"
)

fun errorValidation(
    field: String,
    violationCode: String,
    description: String,
    level: ProcessingError.Level = ProcessingError.Level.ERROR
) = ProcessingError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level
)

fun errorAdministration(
    field: String = "",
    violationCode: String,
    description: String,
    level: ProcessingError.Level = ProcessingError.Level.ERROR,
) = ProcessingError(
    field = field,
    code = "administration-$violationCode",
    group = "administration",
    message = "Microservice management error: $description",
    level = level,
)

fun Throwable.asProcessingError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: ""
) = ProcessingError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this
)
