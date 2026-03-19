package com.diegoferreiracaetano.dlearn.infrastructure.cache

import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration

class InMemoryCache<K, V>(private val expiration: Duration) {
    private val cache = ConcurrentHashMap<K, Pair<V, Long>>()

    suspend fun getOrPut(key: K, block: suspend () -> V): V {
        val now = System.currentTimeMillis()
        cache[key]?.let { (value, timestamp) ->
            if (now - timestamp < expiration.inWholeMilliseconds) return value
        }
        return block().also { cache[key] = it to now }
    }

    fun clear() {
        cache.clear()
    }
}
