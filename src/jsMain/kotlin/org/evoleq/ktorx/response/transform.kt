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
    is Result.Success -> org.evoleq.ktorx.response.Response.Success<S>(
        value
    )
    is Result.Failure -> with(failureTransformation(value)) {
        val (message, code ) = this
        org.evoleq.ktorx.response.Response.Failure<S>(
            message,
            code
        )
    }
}