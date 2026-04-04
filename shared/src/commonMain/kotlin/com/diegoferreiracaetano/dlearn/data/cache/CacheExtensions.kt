package com.diegoferreiracaetano.dlearn.data.cache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

enum class CacheStrategy {
    NETWORK_FIRST,
    CACHE_FIRST,
}

@PublishedApi
internal object CacheKoinHelper : KoinComponent

/**
 * Extension para adicionar resiliência ou performance de cache a qualquer Flow.
 */
inline fun <reified T> Flow<T>.toCache(
    key: String? = null,
    strategy: CacheStrategy = CacheStrategy.NETWORK_FIRST,
    manager: CacheManager? = null,
): Flow<T> =
    flow {
        val actualManager = manager ?: CacheKoinHelper.get<CacheManager>()
        val cacheKey = key ?: "cache_${T::class.simpleName}"
        val serializer = serializer<T>()

        if (strategy == CacheStrategy.CACHE_FIRST) {
            val cachedObj = actualManager.get(cacheKey, serializer)
            if (cachedObj != null) {
                emit(cachedObj)
                return@flow
            }
        }

        var networkSucceeded = false
        val networkError = try {
            this@toCache
                .onEach { data ->
                    actualManager.put(cacheKey, data, serializer)
                }.collect { 
                    networkSucceeded = true
                    emit(it) 
                }
            null
        } catch (e: Exception) {
            e
        }

        if (networkError != null && !networkSucceeded) {
            if (strategy == CacheStrategy.NETWORK_FIRST) {
                val cachedObj = actualManager.get(cacheKey, serializer)
                if (cachedObj != null) {
                    emit(cachedObj)
                } else {
                    throw networkError
                }
            } else {
                throw networkError
            }
        } else if (networkError != null) {
            throw networkError
        }
    }
