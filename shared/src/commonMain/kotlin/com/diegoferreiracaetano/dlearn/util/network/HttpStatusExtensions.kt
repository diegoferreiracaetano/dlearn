package com.diegoferreiracaetano.dlearn.util.network

import io.ktor.http.HttpStatusCode

val HttpStatusCode.Companion.PreconditionRequired: HttpStatusCode
    get() = HttpStatusCode(428, "Precondition Required")
