package org.evoleq.ktorx.response

//import io.ktor.serialization.DefaultJsonConfiguration

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import org.junit.Test


class ResponseTest {

    @Serializable
    data class TestData(
        val x: Int
    )
    
    

    @Test fun `serialize to json` () {
        val data = TestData(7)
        val response = Response.Success(data)
        val expected = """{"type":"Success","data":{"x":7}}"""
        val serialized = Json.encodeToString(Response.serializer(TestData.serializer()),response)
        assert(serialized == expected)
        val deserialized = Json{}.decodeFromString(Response.serializer(TestData.serializer()),serialized)
        assert(deserialized == response)
    }
    @Test fun `intermediate json response`() {
        val data = TestData(7)
        val response = Response.Success(data)
        val expected = """{"type":"Success","data":{"x":7}}"""
        val serialized = Json.encodeToString(Response.serializer(TestData.serializer()),response)
        assert(serialized == expected)
        
        
        val deserializedAsJson: JsonResponse = Json.decodeFromString(Response.serializer(JsonElement.serializer()), serialized)
        val reSerializedFromJson =  Json.encodeToString(Response.serializer(JsonElement.serializer()), deserializedAsJson)
        assert(reSerializedFromJson == expected)
        val deserialized =  Json.decodeFromString(Response.serializer(TestData.serializer()),reSerializedFromJson)
        assert(deserialized == response)
        
    }
    @Test fun `serialize to protobuf` () {
        /*
        val data = TestData(7)
        val response = Response.Success(data)
        val expected = """{"type":"Success","data":{"x":7}}"""
        
        val serialized = ProtoBuf().dump(Response.serializer(TestData.serializer()),response)
        assert(serialized == expected)
        val deserialized = Json(JsonConfiguration.Default).parse(Response.serializer(TestData.serializer()),serialized)
        assert(deserialized == response)
        
         */
    }
    
}