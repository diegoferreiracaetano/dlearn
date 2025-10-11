package com.diegoferreiracaetano.dlearn.util

inline fun <T> Iterable<T?>.filterNotNull(predicate: (T) -> Boolean): List<T> {
    return this.filter { it != null && predicate(it) }.map { it!! }
}

inline fun <T> Iterable<T>.filterOrNull(predicate: (T) -> Boolean): List<T>? {
    val result = this.filter(predicate)
    return result.takeIf { it.isNotEmpty() }
}