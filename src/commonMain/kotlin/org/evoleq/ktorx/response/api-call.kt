package org.evoleq.ktorx.response

import io.ktor.client.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import org.evoleq.ktorx.marker.KtorxDsl
import org.evoleq.math.cat.suspend.monad.state.KlScopedSuspendedState
import org.evoleq.math.cat.suspend.monad.state.ScopedSuspendedState
import org.evoleq.math.cat.suspend.morphism.by


typealias ApiCall<Request, Data> = KlScopedSuspendedState<HttpClient, Request, Response<Data>>

@KtorxDsl
@Suppress("FunctionName")
fun<Request, Data> ApiCall(arrow: suspend CoroutineScope.(Request)->ScopedSuspendedState<HttpClient,Response<Data>>): ApiCall<Request, Data> =
    KlScopedSuspendedState (arrow)

@KtorxDsl
fun <Request, Data> HttpClient.perform(apiCall: ApiCall<Request,Data>): Pair<HttpClient,ApiCall<Request,Data>> = Pair(this,apiCall)

@KtorxDsl
suspend infix fun <Request, Data> Pair<HttpClient,ApiCall<Request,Data>>.on(request: Request): Pair<Response<Data>, HttpClient> =
    coroutineScope { by(by(second)(request))(first) }
