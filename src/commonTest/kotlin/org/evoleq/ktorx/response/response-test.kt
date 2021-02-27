package org.evoleq.ktorx.response



import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlin.test.Test
import kotlin.test.assertEquals


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
        assertEquals(serialized, expected)
        val deserialized = Json{}.decodeFromString(Response.serializer(TestData.serializer()),serialized)
        assertEquals(deserialized, response)
    }
    @Test fun `intermediate json response`() {
        val data = TestData(7)
        val response = Response.Success(data)
        val expected = """{"type":"Success","data":{"x":7}}"""
        val serialized = Json.encodeToString(Response.serializer(TestData.serializer()),response)
        assertEquals(serialized, expected)
        
        
        val deserializedAsJson: JsonResponse = Json.decodeFromString(Response.serializer(JsonElement.serializer()), serialized)
        val reSerializedFromJson =  Json.encodeToString(Response.serializer(JsonElement.serializer()), deserializedAsJson)
        assertEquals(reSerializedFromJson, expected)
        val deserialized =  Json.decodeFromString(Response.serializer(TestData.serializer()),reSerializedFromJson)
        assertEquals(deserialized, response)
        
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