package org.evoleq.ktorx.response

import kotlinx.serialization.*
import kotlinx.serialization.builtins.serializer

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

    override val descriptor: SerialDescriptor = SerialDescriptor("ResponseSerializer") {
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
                CompositeDecoder.READ_DONE -> break@loop
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
