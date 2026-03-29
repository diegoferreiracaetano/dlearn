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
        val hasUsername = metadata.containsKey(MetadataKeys.EXTERNAL_USERNAME) || metadata.containsKey("tmdb_username")
        val hasPassword = metadata.containsKey(MetadataKeys.EXTERNAL_PASSWORD) || metadata.containsKey("tmdb_password")
        return hasUsername && hasPassword
    }

    override suspend fun authenticate(metadata: Map<String, String>): Map<String, String> {
        val username = metadata[MetadataKeys.EXTERNAL_USERNAME] ?: metadata["tmdb_username"]
        val password = metadata[MetadataKeys.EXTERNAL_PASSWORD] ?: metadata["tmdb_password"]

        if (username == null || password == null) {
            logger.warn("TMDB authentication skipped: Missing username or password in metadata")
            return emptyMap()
        }

        return try {
            logger.info("Attempting TMDB authentication for user: $username")
            
            val tokenResponse = tmdbClient.createRequestToken()
            logger.debug("Request token created: ${tokenResponse.requestToken}")

            val validatedToken = tmdbClient.validateWithLogin(
                TmdbLoginRequest(
                    username = username,
                    password = password,
                    requestToken = tokenResponse.requestToken
                )
            )
            logger.debug("Token validated successfully")

            val sessionResponse = tmdbClient.createSession(validatedToken.requestToken)
            logger.debug("Session created: ${sessionResponse.sessionId}")

            val accountResponse = tmdbClient.getAccountDetails(sessionResponse.sessionId)
            logger.info("TMDB Account details fetched. ID: ${accountResponse.id}")

            mapOf(
                MetadataKeys.TMDB_SESSION_ID to sessionResponse.sessionId,
                MetadataKeys.TMDB_ACCOUNT_ID to accountResponse.id.toString(),
                "tmdb_username" to username,
                "tmdb_password" to password,
                "external_id" to accountResponse.id.toString()
            )
        } catch (e: Exception) {
            logger.error("TMDB Authentication failed for $username: ${e.message}", e)
            emptyMap() // Retornamos vazio para não travar o cadastro principal, mas logamos o erro
        }
    }
}
