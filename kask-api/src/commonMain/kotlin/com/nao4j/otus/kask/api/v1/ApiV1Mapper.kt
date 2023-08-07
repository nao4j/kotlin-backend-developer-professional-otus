@file:Suppress("UNCHECKED_CAST")

package com.nao4j.otus.kask.api.v1

import com.nao4j.otus.kask.api.v1.models.IRequest
import com.nao4j.otus.kask.api.v1.models.IResponse
import com.nao4j.otus.kask.api.v1.models.QuestionCreateRequest
import com.nao4j.otus.kask.api.v1.models.QuestionCreateResponse
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteRequest
import com.nao4j.otus.kask.api.v1.models.QuestionDeleteResponse
import com.nao4j.otus.kask.api.v1.models.QuestionReadRequest
import com.nao4j.otus.kask.api.v1.models.QuestionReadResponse
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateRequest
import com.nao4j.otus.kask.api.v1.models.QuestionUpdateResponse
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

val apiV1Mapper = Json {
    classDiscriminator = "_"
    encodeDefaults = true
    ignoreUnknownKeys = true

    serializersModule = SerializersModule {
        setupPolymorphic()
    }
}

internal val infos = listOf(
    info(QuestionCreateRequest::class, IRequest::class, "create") { copy(requestType = it) },
    info(QuestionReadRequest::class, IRequest::class, "read") { copy(requestType = it) },
    info(QuestionUpdateRequest::class, IRequest::class, "update") { copy(requestType = it) },
    info(QuestionDeleteRequest::class, IRequest::class, "delete") { copy(requestType = it) },

    info(QuestionCreateResponse::class, IResponse::class, "create") { copy(responseType = it) },
    info(QuestionReadResponse::class, IResponse::class, "read") { copy(responseType = it) },
    info(QuestionUpdateResponse::class, IResponse::class, "update") { copy(responseType = it) },
    info(QuestionDeleteResponse::class, IResponse::class, "delete") { copy(responseType = it) },
)

fun apiV1RequestSerialize(request: IRequest): String =
    apiV1Mapper.encodeToString(request)

fun <T : IRequest> apiV1RequestDeserialize(json: String): T =
    apiV1Mapper.decodeFromString<IRequest>(json) as T

fun apiV1ResponseSerialize(response: IResponse): String =
    apiV1Mapper.encodeToString(response)

fun <T : IResponse> apiV1ResponseDeserialize(json: String): T =
    apiV1Mapper.decodeFromString<IResponse>(json) as T
