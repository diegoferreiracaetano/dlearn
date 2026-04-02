package com.diegoferreiracaetano.dlearn.infrastructure.cache

import com.diegoferreiracaetano.dlearn.data.cache.CacheManager
import kotlinx.serialization.KSerializer
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration

class InMemoryCacheManager(
    private val expiration: Duration,
) : CacheManager {
    private val cache = ConcurrentHashMap<String, Pair<Any, Long>>()

    override fun <T> put(
        key: String,
        value: T,
        serializer: KSerializer<T>,
    ) {
        if (value == null) return
        cache[key] = value to System.currentTimeMillis()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> get(
        key: String,
        serializer: KSerializer<T>,
    ): T? {
        val now = System.currentTimeMillis()
        val pair = cache[key] ?: return null
        val value = pair.first
        val timestamp = pair.second

        return if (now - timestamp < expiration.inWholeMilliseconds) {
            value as? T
        } else {
            cache.remove(key)
            null
        }
    }
}
