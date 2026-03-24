package com.diegoferreiracaetano.dlearn.api.util

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

val ApplicationCall.userId: String
    get() = principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString() ?: "guest"
