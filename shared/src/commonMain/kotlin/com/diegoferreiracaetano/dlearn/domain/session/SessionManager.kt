package com.diegoferreiracaetano.dlearn.domain.session

import com.diegoferreiracaetano.dlearn.domain.auth.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.user.MovieProvider
import com.diegoferreiracaetano.dlearn.domain.user.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SessionManager(
    private val accountProvider: AccountProvider,
) {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    suspend fun initialize() {
        _isLoggedIn.value = accountProvider.hasAccount()
    }

    suspend fun login(
        user: User,
        provider: MovieProvider?,
        accessToken: String,
        refreshToken: String
    ) {
        accountProvider.saveAccount(user, provider, accessToken, refreshToken)
        _isLoggedIn.value = true
    }

    suspend fun logout() {
        accountProvider.clearAccount()
        _isLoggedIn.value = false
    }

    suspend fun user(): User = accountProvider.getUser() ?: throw IllegalStateException("User not logged in")
    suspend fun provider(): MovieProvider? = accountProvider.getProvider()

    suspend fun token(): String? = accountProvider.getAccessToken()
    suspend fun refreshToken(): String? = accountProvider.getRefreshToken()
}
