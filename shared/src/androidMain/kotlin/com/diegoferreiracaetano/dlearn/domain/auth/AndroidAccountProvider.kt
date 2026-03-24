package com.diegoferreiracaetano.dlearn.domain.auth

import android.accounts.Account
import android.accounts.AccountManager
import android.content.Context
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.util.fromJson
import com.diegoferreiracaetano.dlearn.util.toJson

class AndroidAccountProvider(
    private val context: Context
) : AccountProvider {
    private val accountManager = AccountManager.get(context)
    private val accountType = "com.diegoferreiracaetano.dlearn"
    private val authTokenType = "full_access"

    override suspend fun saveAccount(user: User, accessToken: String, refreshToken: String) {
        val account = Account(user.email, accountType)
        accountManager.addAccountExplicitly(account, null, null)
        accountManager.setAuthToken(account, authTokenType, accessToken)
        accountManager.setUserData(account, "refresh_token", refreshToken)
        accountManager.setUserData(account, "user_data", user.toJson())
    }

    override suspend fun getAccessToken(): String? {
        val account = getAccount() ?: return null
        return accountManager.peekAuthToken(account, authTokenType)
    }

    override suspend fun getRefreshToken(): String? {
        val account = getAccount() ?: return null
        return accountManager.getUserData(account, "refresh_token")
    }

    override suspend fun getUser(): User? {
        val account = getAccount() ?: return null
        return accountManager.getUserData(account, "user_data")?.fromJson<User>()
    }

    override suspend fun clearAccount() {
        getAccount()?.let {
            accountManager.removeAccountExplicitly(it)
        }
    }

    override suspend fun hasAccount(): Boolean = getAccount() != null

    private fun getAccount(): Account? {
        return accountManager.getAccountsByType(accountType).firstOrNull()
    }
}
