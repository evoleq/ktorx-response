/**
 * Copyright (c) 2020 Dr. Florian Schmidt
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.evoleq.ktorx.response

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.evoleq.math.cat.marker.MathCatDsl

@Serializable(with = ResponseSerializer::class)
sealed class Response<Data> {
    //@Serializable(with=SuccessSerializer::class)
    @Serializable
    data class Success<Data>(@Serializable val data: Data): Response<Data>()
    @Serializable
    data class Failure<Data>(
        val message: String,
        val code: Int
    ) : Response<Data>()
/*
    companion object{
        val dataSerializers = hashMapOf<KClass<*>, KSerializer<*>>()
        inline fun <reified Data> findSerializer(): KSerializer<Data> = dataSerializers[Data::class]!! as KSerializer<Data>
    }

 */
}

class ResponseSerializer<Data : Any>(val dataSerializer: KSerializer<Data>): KSerializer<Response<Data>> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("ResponseSerializer") {
        val dataDescriptor = dataSerializer.descriptor
        element("type", String.serializer().descriptor)
        element("data", dataDescriptor)
        element("message", String.serializer().descriptor)
        element("code", Int.serializer().descriptor)
    }

    override fun serialize(encoder: Encoder, value: Response<Data>) {
        val out = encoder.beginStructure(descriptor)
        when(value) {
            is Response.Success -> {
                out.encodeStringElement(descriptor,0,"${value::class.simpleName}")
                out.encodeSerializableElement(descriptor,1, dataSerializer, value.data )
            }
            is Response.Failure -> {
                out.encodeStringElement(descriptor,0,"${value::class.simpleName}")
                out.encodeStringElement(descriptor,2,value.message)
                out.encodeIntElement(descriptor,3,value.code)
            }
        }
        out.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): Response<Data> {
        val inp = decoder.beginStructure(descriptor)
        lateinit var type: String
        lateinit var data: Data
        lateinit var message: String
        var code: Int = -1
        loop@ while (true) {
            when (val i = inp.decodeElementIndex(descriptor)) {
                CompositeDecoder.DECODE_DONE -> break@loop
                0 -> type = inp.decodeStringElement(descriptor,i)
                1 -> data = inp.decodeSerializableElement(descriptor, i, dataSerializer)
                2 -> message = inp.decodeStringElement(descriptor,i)
                3 -> code = inp.decodeIntElement(descriptor,i)
                else -> throw SerializationException("Unknown index $i")
            }
        }
        inp.endStructure(descriptor)
        return when(type){
            Response.Success::class.simpleName -> Response.Success(
                data
            )
            Response.Failure::class.simpleName -> Response.Failure(
                message,
                code
            )
            else ->  throw SerializationException("Unknown type $type")
        }
    }
}

@MathCatDsl
infix fun <S, T> Response<S>.map(f:(S)->T): Response<T> = when(this) {
    is Response.Success -> try{
        Response.Success(f(this.data))
    } catch(exception: Exception) {
        Response.Failure<T>(
            exception.message?:"Error while mapping Response. No message provided",
            -1
        )
    }
    is Response.Failure -> Response.Failure(
        message,
        code
    )
}

@MathCatDsl
infix fun <S, T> Response<S>.bind(f: (S)->Response<T>): Response<T> = when(this) {
    is Response.Success -> try{
        f(data)
    } catch(exception: Exception) {
        Response.Failure<T>(
            exception.message?:"Error while binding Response. No message provided",
            -1
        )
    }
    is Response.Failure -> Response.Failure(
        message,
        code
    )
}

@MathCatDsl
fun <S, T> Response<(S)->T>.apply(): (Response<S>)->Response<T> = {
    response -> this@apply bind { f -> response map f }
}

@MathCatDsl
infix fun <S, T> Response<(S)->T>.apply(response: Response<S>): Response<T> = apply()(response)

/*
object Serializers {
    private val dataSerializers = hashMapOf<KClass<*>, KSerializer<*>>()
    //inline fun <reified Data> get(): KSerializer<Data> = dataSerializers[Data::class]!! as KSerializer<Data>
    operator fun get(klazz: KClass<*>): KSerializer<*> = dataSerializers[klazz]!!
    operator fun set(klazz: KClass<*>, serializer: KSerializer<*>) {
        dataSerializers[klazz] = serializer
    }
}

 */
