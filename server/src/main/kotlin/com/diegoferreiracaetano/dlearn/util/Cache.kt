package com.diegoferreiracaetano.dlearn.util

import java.util.concurrent.ConcurrentHashMap

class Cache<T>(private val ttlInMillis: Long) {
    private val cache = ConcurrentHashMap<String, CacheEntry<T>>()

    fun get(key: String): T? {
        val entry = cache[key]
        return if (entry != null && !entry.isExpired()) {
            println("Cache HIT for key: $key")
            entry.value
        } else {
            println("Cache MISS for key: $key")
            null
        }
    }

    fun put(key: String, value: T) {
        cache[key] = CacheEntry(value, System.currentTimeMillis() + ttlInMillis)
    }

    private data class CacheEntry<T>(val value: T, val expiryTime: Long) {
        fun isExpired(): Boolean = System.currentTimeMillis() > expiryTime
    }
}
