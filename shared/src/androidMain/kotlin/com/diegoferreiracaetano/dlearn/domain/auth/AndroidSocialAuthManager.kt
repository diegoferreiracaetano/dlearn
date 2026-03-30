package com.diegoferreiracaetano.dlearn.domain.auth

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.diegoferreiracaetano.dlearn.BuildConfig
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AndroidSocialAuthManager(private val context: Context) : SocialAuthManager {

    override suspend fun googleSignIn(): SocialAuthResult = withContext(Dispatchers.Main) {
        val serverClientId = BuildConfig.GOOGLE_WEB_CLIENT_ID
        
        if (serverClientId.isBlank()) {
            return@withContext SocialAuthResult.Error(AppErrorCode.SOCIAL_AUTH_CONFIG_MISSING)
        }

        val credentialManager = CredentialManager.create(context)
        
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(serverClientId)
            .setAutoSelectEnabled(true)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        try {
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential
            
            // Tenta converter para GoogleIdTokenCredential (forma direta ou via parsing de dados)
            val googleIdTokenCredential = try {
                if (credential is GoogleIdTokenCredential) {
                    credential
                } else {
                    GoogleIdTokenCredential.createFrom(credential.data)
                }
            } catch (e: Exception) {
                null
            }

            if (googleIdTokenCredential != null) {
                SocialAuthResult.Success(idToken = googleIdTokenCredential.idToken)
            } else {
                SocialAuthResult.Error(AppErrorCode.UNSUPPORTED_CREDENTIAL_TYPE)
            }
        } catch (e: GetCredentialException) {
            SocialAuthResult.Error(AppErrorCode.SOCIAL_AUTH_FAILED)
        } catch (e: Exception) {
            SocialAuthResult.Error(AppErrorCode.UNKNOWN_ERROR)
        }
    }

    override suspend fun appleSignIn(): SocialAuthResult {
        return SocialAuthResult.Error(AppErrorCode.UNSUPPORTED_CREDENTIAL_TYPE)
    }

    override suspend fun facebookSignIn(): SocialAuthResult {
        if (BuildConfig.FACEBOOK_APP_ID.isBlank()) {
            return SocialAuthResult.Error(AppErrorCode.SOCIAL_AUTH_CONFIG_MISSING)
        }
        return SocialAuthResult.Error(AppErrorCode.SOCIAL_AUTH_FAILED)
    }
}
