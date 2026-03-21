package com.diegoferreiracaetano.dlearn.util

import io.ktor.http.*

val HttpStatusCode.Companion.PreconditionRequired: HttpStatusCode
    get() = HttpStatusCode(428, "Precondition Required")
