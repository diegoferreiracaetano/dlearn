package com.diegoferreiracaetano.dlearn.data.cache

import com.diegoferreiracaetano.dlearn.data.source.local.KeyValueStorage
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

class PersistentCacheManager(
    private val keyValueStorage: KeyValueStorage,
    private val json: Json
) : CacheManager {
    override fun <T> put(key: String, value: T, serializer: KSerializer<T>) {
        runCatching {
            val serialized = json.encodeToString(serializer, value)
            keyValueStorage.put(key, serialized)
        }
    }

    override fun <T> get(key: String, serializer: KSerializer<T>): T? {
        val cachedData = keyValueStorage.get(key, "")
        if (cachedData.isEmpty()) return null
        return runCatching {
            json.decodeFromString(serializer, cachedData)
        }.getOrNull()
    }
}
