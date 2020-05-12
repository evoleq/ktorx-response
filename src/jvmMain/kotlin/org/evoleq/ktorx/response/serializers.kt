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
import org.drx.configuration.Configuration
import org.evoleq.ktorx.marker.KtorxDsl
import kotlin.reflect.KClass

class SerializersConfiguration : Configuration<Unit> {
    private val serializers = hashMapOf<KClass<*>, KSerializer<*>>()
    override fun configure() {
        serializers.forEach {
            Serializers[it.key] = it.value
        }
        return
    }
    @KtorxDsl
    infix fun KClass<*>.with(serializer: KSerializer<*>) {
        serializers[this] = serializer
    }
}
object Serializers {
    private val dataSerializers = hashMapOf<KClass<*>, KSerializer<*>>()
    //inline fun <reified Data> get(): KSerializer<Data> = dataSerializers[Data::class]!! as KSerializer<Data>
    operator fun get(klazz: KClass<*>): KSerializer<*> = dataSerializers[klazz]!!
    operator fun set(klazz: KClass<*>,serializer: KSerializer<*>) {
        dataSerializers[klazz] = serializer
    }
}