package com.diegoferreiracaetano.dlearn.util

import com.diegoferreiracaetano.dlearn.Constants
import io.ktor.http.HttpStatusCode

val HttpStatusCode.Companion.PreconditionRequired: HttpStatusCode
    get() = HttpStatusCode(Constants.HTTP_STATUS_PRECONDITION_REQUIRED, "Precondition Required")
