package com.diegoferreiracaetano.dlearn.util

fun Double.isPositive(): Boolean = !this.isNaN() && this > 0.0
fun Float.isPositive(): Boolean = !this.isNaN() && this > 0f
fun Int.isPositive(): Boolean = this > 0