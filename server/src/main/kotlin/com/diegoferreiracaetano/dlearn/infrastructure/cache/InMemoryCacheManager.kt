package com.diegoferreiracaetano.dlearn.infrastructure.cache

import com.diegoferreiracaetano.dlearn.data.cache.CacheManager
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration
import kotlin.time.TimeSource

class InMemoryCacheManager(
    private val expiration: Duration,
) : CacheManager {
    private val cache = ConcurrentHashMap<String, CacheEntry>()
    private val json = Json { ignoreUnknownKeys = true }

    private data class CacheEntry(
        val data: String,
        val timestamp: TimeSource.Monotonic.ValueTimeMark,
    )

    override fun <T> put(
        key: String,
        value: T,
        serializer: KSerializer<T>,
    ) {
        val data = json.encodeToString(serializer, value)
        cache[key] = CacheEntry(data, TimeSource.Monotonic.markNow())
    }

    override fun <T> get(
        key: String,
        serializer: KSerializer<T>,
    ): T? {
        val entry = cache[key] ?: return null
        if (entry.timestamp.elapsedNow() > expiration) {
            cache.remove(key)
            return null
        }
        return json.decodeFromString(serializer, entry.data)
    }
}
