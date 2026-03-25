package com.diegoferreiracaetano.dlearn.api.util

import com.diegoferreiracaetano.dlearn.AppConstants.X_COUNTRY
import com.diegoferreiracaetano.dlearn.AppConstants.X_NOTIFICATIONS_ENABLED
import com.diegoferreiracaetano.dlearn.network.AppHeader
import io.ktor.http.HttpHeaders.AcceptLanguage
import io.ktor.http.HttpHeaders.UserAgent
import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.header

val ApplicationCall.userId: String
    get() = principal<JWTPrincipal>()?.payload?.getClaim("userId")?.asString() ?: "guest"

val ApplicationCall.appHeader: AppHeader
    get() = AppHeader(
        paramUserAgent = request.header(UserAgent),
        paramLanguage = request.header(AcceptLanguage),
        paramCountry = request.header(X_COUNTRY),
        notificationsEnabled = request.header(X_NOTIFICATIONS_ENABLED)?.toBoolean() ?: true,
        userId = userId
    )
