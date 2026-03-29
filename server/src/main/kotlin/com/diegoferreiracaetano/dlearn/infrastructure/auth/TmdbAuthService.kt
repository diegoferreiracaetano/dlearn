package com.diegoferreiracaetano.dlearn.infrastructure.auth

import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import org.slf4j.LoggerFactory

class TmdbAuthService(
    private val tmdbClient: TmdbClient
) : ExternalAuthService {
    private val logger = LoggerFactory.getLogger(javaClass)
    override val provider: AccountProvider = AccountProvider.TMDB

    override fun canHandle(metadata: Map<String, String>): Boolean {
        // Agora ele sempre retorna true para garantir o vínculo automático no login/registro
        return true
    }

    override suspend fun authenticate(metadata: Map<String, String>): Map<String, String> {
        return try {
            logger.info("[TMDB] Automatically creating Guest Session for user")
            
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
