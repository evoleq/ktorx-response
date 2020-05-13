package org.evoleq.ktorx.response

//import io.ktor.serialization.DefaultJsonConfiguration

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.parse
import org.junit.Test

import org.evoleq.ktorx.response.Response

class ResponseTest {
    
    @Serializable
    data class TestData(
        val x: Int
    )
    
    @Test fun `serialize to json` () {
        val data = TestData(7)
        val response = Response.Success(data)
        val expected = """{"type":"Success","data":{"x":7}}"""
        val serialized = Json(JsonConfiguration.Default).stringify(Response.serializer(TestData.serializer()),response)
        assert(serialized == expected)
        val deserialized = Json(JsonConfiguration.Default).parse(Response.serializer(TestData.serializer()),serialized)
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