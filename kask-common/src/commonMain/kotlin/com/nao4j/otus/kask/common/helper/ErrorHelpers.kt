package com.nao4j.otus.kask.common.helper

import com.nao4j.otus.kask.common.models.ProcessingError

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