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

import org.evoleq.math.cat.suspend.monad.result.Result

fun <S: Any, F> Result<S, F>.transform(failureTransformation: (F)->Pair<String,Int>): Response<S> = when(this){
    is Result.Success -> Response.Success<S>(
        value
    )
    is Result.Failure -> with(failureTransformation(value)) {
        val (message, code ) = this
        Response.Failure<S>(
            message,
            code
        )
    }
}


fun <S: Any> Response<S>.toResult(): Result<S, Response.Failure<S>> = when(this) {
    is Response.Success -> Result.ret(data)
    is Response.Failure -> Result.fail(this)
}

fun <S: Any, F> Response<S>.transform(failureTransformation: (Response.Failure<S>) -> F): Result<S, F> = when(this) {
    is Response.Success -> Result.ret(data)
    is Response.Failure -> Result.fail(failureTransformation(this))
}