package com.nao4j.otus.kask.api.v1

import com.nao4j.otus.kask.api.v1.models.IRequest
import com.nao4j.otus.kask.api.v1.models.IResponse
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.contextual
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

private val requestSerializer = PolymorphicSerializer(IRequest::class, "requestType")
private val responseSerializer = PolymorphicSerializer(IResponse::class, "responseType")

private class PolymorphicSerializer<T : Any>(
    private val klass: KClass<T>,
    private val discriminatorField: String
) : JsonContentPolymorphicSerializer<T>(klass) {

    @Suppress("UNCHECKED_CAST")
    @OptIn(InternalSerializationApi::class)
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<out T> {
        val discriminatorValue = element.jsonObject[discriminatorField]?.jsonPrimitive?.content
        val info = findInfo(klass, "Unknown class to deserialize: $discriminatorValue") {
            discriminator == discriminatorValue
        }
        return info.klass.serializer() as DeserializationStrategy<out T>
    }
}

@OptIn(ExperimentalSerializationApi::class)
internal fun SerializersModuleBuilder.setupPolymorphic() {
    polymorphicSerializer<IRequest>()
    polymorphicDefaultDeserializer(IRequest::class) { requestSerializer }

    polymorphicSerializer<IResponse>()
    polymorphicDefaultDeserializer(IResponse::class) { responseSerializer }

    contextual(requestSerializer)
    contextual(responseSerializer)
}

@OptIn(InternalSerializationApi::class, ExperimentalSerializationApi::class)
private inline fun <reified T : Any> SerializersModuleBuilder.polymorphicSerializer() {
    polymorphicDefaultSerializer(T::class) { value ->
        val info = findInfo(T::class, "Unknown class to serialize ${value::class}") { klass == value::class }
        object : KSerializer<T> {
            override val descriptor: SerialDescriptor
                get() = info.klass.serializer().descriptor

            override fun serialize(encoder: Encoder, value: T) {
                val copy = info.makeCopyWithDiscriminator(value)
                info.serialize(encoder, copy)
            }

            override fun deserialize(decoder: Decoder): T = throw NotImplementedError("you should not use this method")
        }
    }
}

private inline fun findInfo(
    klass: KClass<*>,
    error: String,
    predicate: PolymorphicInfo<out Any, out Any>.() -> Boolean
) = infos.firstOrNull { it.superClass == klass && it.predicate() } ?: throw SerializationException(error)

internal data class PolymorphicInfo<S : Any, T : S>(
    val klass: KClass<T>,
    val superClass: KClass<S>,
    val serialize: (Encoder, Any) -> Unit,
    val discriminator: String,
    val makeCopyWithDiscriminator: (Any) -> T
)

@Suppress("UNCHECKED_CAST")
@OptIn(InternalSerializationApi::class)
internal fun <S : Any, T : S> info(
    klass: KClass<T>,
    superClass: KClass<S>,
    discriminator: String,
    makeCopyWithDiscriminator: T.(String) -> T
) = PolymorphicInfo(
    klass,
    superClass,
    { e: Encoder, v: Any ->
        klass.serializer().serialize(e, v as T)
    },
    discriminator,
    { v: Any ->
        makeCopyWithDiscriminator(v as T, discriminator)
    }
)

