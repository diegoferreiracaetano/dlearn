package com.diegoferreiracaetano.dlearn.infrastructure.auth

import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.tmdb.TmdbLoginRequest
import org.slf4j.LoggerFactory

class TmdbAuthService(
    private val tmdbClient: TmdbClient
) : ExternalAuthService {
    private val logger = LoggerFactory.getLogger(javaClass)
    override val provider: AccountProvider = AccountProvider.TMDB

    override fun canHandle(metadata: Map<String, String>): Boolean {
        // Agora ele sempre retorna true para garantir o vínculo (guest ou login)
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
            logger.info("[TMDB] Authenticating with TMDB Login for user: $username")
            
            // 1. Get Request Token
            val tokenResponse = tmdbClient.createRequestToken()
            if (!tokenResponse.success) {
                logger.warn("[TMDB] Failed to create request token")
                return createGuestSession() // Fallback to guest if login fails
            }

            // 2. Validate with Login
            val loginRequest = TmdbLoginRequest(
                username = username,
                password = password,
                requestToken = tokenResponse.requestToken ?: ""
            )
            val validatedTokenResponse = tmdbClient.validateWithLogin(loginRequest)
            
            if (!validatedTokenResponse.success) {
                logger.warn("[TMDB] Invalid TMDB credentials for user: $username")
                return createGuestSession() // Fallback to guest
            }

            // 3. Create Session
            val sessionResponse = tmdbClient.createSession(validatedTokenResponse.requestToken ?: "")
            if (!sessionResponse.success) {
                logger.warn("[TMDB] Failed to create TMDB session")
                return createGuestSession() // Fallback to guest
            }

            // 4. Get Account Details
            val accountDetails = tmdbClient.getAccountDetails(sessionResponse.sessionId)
            
            logger.info("[TMDB] Authenticated successfully with TMDB Login. Account ID: ${accountDetails.id}")

            mapOf(
                MetadataKeys.TMDB_SESSION_ID to sessionResponse.sessionId,
                MetadataKeys.TMDB_ACCOUNT_ID to accountDetails.id.toString(),
                "external_id" to accountDetails.id.toString(),
                "auth_type" to "full_session",
                "username" to accountDetails.username
            )
        } catch (e: Exception) {
            logger.error("[TMDB] Error during TMDB login: ${e.message}")
            createGuestSession()
        }
    }

    private suspend fun createGuestSession(): Map<String, String> {
        return try {
            logger.info("[TMDB] Creating Guest Session as fallback or default")
            
            val guestSession = tmdbClient.createGuestSession()
            
            if (guestSession.success && guestSession.guestSessionId != null) {
                logger.info("[TMDB] Guest Session created: ${guestSession.guestSessionId}")
                
                mapOf(
                    MetadataKeys.TMDB_SESSION_ID to guestSession.guestSessionId,
                    "external_id" to "guest_${guestSession.guestSessionId.take(8)}",
                    "auth_type" to "guest_session",
                    "expires_at" to (guestSession.expiresAt ?: "")
                )
            } else {
                logger.warn("[TMDB] Failed to create Guest Session: ${guestSession.statusMessage}")
                emptyMap()
            }
        } catch (e: Exception) {
            logger.error("[TMDB] Error during guest session creation: ${e.message}")
            emptyMap()
        }
    }
}
