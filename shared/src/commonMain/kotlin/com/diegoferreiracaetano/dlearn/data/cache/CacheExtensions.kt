package com.diegoferreiracaetano.dlearn.data.cache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.serializer
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

enum class CacheStrategy {
    NETWORK_FIRST, // Tenta a fonte, usa cache apenas em caso de erro (Ideal para Mobile Offline)
    CACHE_FIRST, // Usa cache se existir, senão busca na fonte e salva (Ideal para Server Performance)
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

        try {
            this@toCache
                .onEach { data ->
                    actualManager.put(cacheKey, data, serializer)
                }.collect { emit(it) }
        } catch (e: Exception) {
            // Se for CACHE_FIRST e chegou aqui, o cache já foi testado e falhou.
            // No NETWORK_FIRST, tentamos o cache agora como fallback.
            if (strategy == CacheStrategy.NETWORK_FIRST) {
                val cachedObj = actualManager.get(cacheKey, serializer)
                if (cachedObj != null) {
                    emit(cachedObj)
                } else {
                    throw e
                }
            } else {
                throw e
            }
        }
    }
