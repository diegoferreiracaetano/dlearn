package com.diegoferreiracaetano.dlearn.data.cache

import kotlinx.serialization.KSerializer

interface CacheManager {
    fun <T> put(
        key: String,
        value: T,
        serializer: KSerializer<T>,
    )

    fun <T> get(
        key: String,
        serializer: KSerializer<T>,
    ): T?
}
