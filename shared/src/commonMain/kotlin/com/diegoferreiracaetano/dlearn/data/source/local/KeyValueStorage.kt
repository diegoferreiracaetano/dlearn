package com.diegoferreiracaetano.dlearn.data.source.local

import kotlinx.coroutines.flow.Flow

interface KeyValueStorage {
    fun <T : Any> get(key: String, defaultValue: T): T
    fun <T : Any> getFlow(key: String, defaultValue: T): Flow<T>
    fun <T : Any> put(key: String, value: T)
    fun remove(key: String)
    fun clear()
}
