package org.evoleq.ktorx.response

import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.patch
import io.ktor.client.request.put
import io.ktor.client.request.options
import io.ktor.client.request.delete
import io.ktor.client.request.head
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

suspend inline fun <reified Data> HttpClient.get(
    dataSerializer: KSerializer<Data>,
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Response<Data> = with(
    get<String>(urlString,block)
) {
    val json = Json(JsonConfiguration.Default.copy(prettyPrint = true))
    json.parse(Response.serializer(dataSerializer),this)
}

suspend inline fun <reified Data> HttpClient.post(
    dataSerializer: KSerializer<Data>,
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Response<Data> = with(
    post<String>(urlString,block)
) {
    val json = Json(JsonConfiguration.Default.copy(prettyPrint = true))
    json.parse(Response.serializer(dataSerializer),this)
}

suspend inline fun <reified Data> HttpClient.patch(
    dataSerializer: KSerializer<Data>,
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Response<Data> = with(
    patch<String>(urlString,block)
) {
    val json = Json(JsonConfiguration.Default.copy(prettyPrint = true))
    json.parse(Response.serializer(dataSerializer),this)
}

suspend inline fun <reified Data> HttpClient.put(
    dataSerializer: KSerializer<Data>,
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Response<Data> = with(
    put<String>(urlString,block)
) {
    val json = Json(JsonConfiguration.Default.copy(prettyPrint = true))
    json.parse(Response.serializer(dataSerializer),this)
}

suspend inline fun <reified Data> HttpClient.options(
    dataSerializer: KSerializer<Data>,
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Response<Data> = with(
    options<String>(urlString,block)
) {
    val json = Json(JsonConfiguration.Default.copy(prettyPrint = true))
    json.parse(Response.serializer(dataSerializer),this)
}

suspend inline fun <reified Data> HttpClient.delete(
    dataSerializer: KSerializer<Data>,
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Response<Data> = with(
    delete<String>(urlString,block)
) {
    val json = Json(JsonConfiguration.Default.copy(prettyPrint = true))
    json.parse(Response.serializer(dataSerializer),this)
}

suspend inline fun <reified Data> HttpClient.head(
    dataSerializer: KSerializer<Data>,
    urlString: String,
    block: HttpRequestBuilder.() -> Unit = {}
): Response<Data> = with(
    head<String>(urlString,block)
) {
    val json = Json(JsonConfiguration.Default.copy(prettyPrint = true))
    json.parse(Response.serializer(dataSerializer),this)
}