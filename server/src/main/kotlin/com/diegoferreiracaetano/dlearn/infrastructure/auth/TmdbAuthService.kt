package com.diegoferreiracaetano.dlearn.infrastructure.auth

import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.tmdb.TmdbLoginRequest

class TmdbAuthService(
    private val tmdbClient: TmdbClient
) : ExternalAuthService {
    override val provider: AccountProvider = AccountProvider.TMDB

    override fun canHandle(metadata: Map<String, String>): Boolean {
        return true
    }

    override suspend fun authenticate(metadata: Map<String, String>): Map<String, String> {
        val tmdbUsername = metadata[MetadataKeys.EXTERNAL_USERNAME]
        val tmdbPassword = metadata[MetadataKeys.EXTERNAL_PASSWORD]

        return if (!tmdbUsername.isNullOrBlank() && !tmdbPassword.isNullOrBlank()) {
            authenticateWithLogin(tmdbUsername, tmdbPassword)
        } else {
            createGuestSession()
        }
    }

    private suspend fun authenticateWithLogin(username: String, password: String): Map<String, String> {
        return try {
            val tokenResponse = tmdbClient.createRequestToken()
            if (!tokenResponse.success) {
                createGuestSession()
            } else {
                val loginRequest = TmdbLoginRequest(
                    username = username,
                    password = password,
                    requestToken = tokenResponse.requestToken.orEmpty()
                )
                val validatedTokenResponse = tmdbClient.validateWithLogin(loginRequest)
                
                if (!validatedTokenResponse.success) {
                    createGuestSession()
                } else {
                    val sessionResponse = tmdbClient.createSession(validatedTokenResponse.requestToken.orEmpty())
                    if (!sessionResponse.success) {
                        createGuestSession()
                    } else {
                        val accountDetails = tmdbClient.getAccountDetails(sessionResponse.sessionId)
                        
                        mapOf(
                            MetadataKeys.TMDB_SESSION_ID to sessionResponse.sessionId,
                            MetadataKeys.TMDB_ACCOUNT_ID to accountDetails.id.toString(),
                            MetadataKeys.EXTERNAL_ID to accountDetails.id.toString(),
                            MetadataKeys.AUTH_TYPE to MetadataKeys.AUTH_TYPE_FULL,
                            MetadataKeys.USERNAME to accountDetails.username
                        )
                    }
                }
            }
        } catch (_: Exception) {
            createGuestSession()
        }
    }

    private suspend fun createGuestSession(): Map<String, String> {
        return try {
            val guestSession = tmdbClient.createGuestSession()
            
            if (guestSession.success && guestSession.guestSessionId != null) {
                mapOf(
                    MetadataKeys.TMDB_SESSION_ID to guestSession.guestSessionId,
                    MetadataKeys.EXTERNAL_ID to "${MetadataKeys.GUEST_PREFIX}${guestSession.guestSessionId.take(8)}",
                    MetadataKeys.AUTH_TYPE to MetadataKeys.AUTH_TYPE_GUEST,
                    MetadataKeys.EXPIRES_AT to guestSession.expiresAt.orEmpty()
                )
            } else {
                emptyMap()
            }
        } catch (_: Exception) {
            emptyMap()
        }
    }
}
