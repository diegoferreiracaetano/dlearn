package com.diegoferreiracaetano.dlearn.util

import io.ktor.http.encodeURLParameter

fun String.encodeURL(): String = this.encodeURLParameter()

inline fun <T> Iterable<T?>.filterNotNull(predicate: (T) -> Boolean): List<T> =
    this.filter { it != null && predicate(it) }.map { it!! }

inline fun <T> Iterable<T>.filterOrNull(predicate: (T) -> Boolean): List<T>? {
    val result = this.filter(predicate)
    return result.takeIf { it.isNotEmpty() }
}
