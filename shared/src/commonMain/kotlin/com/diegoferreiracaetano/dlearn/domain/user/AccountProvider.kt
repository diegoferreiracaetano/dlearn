package com.diegoferreiracaetano.dlearn.domain.user

import kotlinx.serialization.Serializable

@Serializable
enum class AccountProvider {
    GOOGLE, APPLE, FACEBOOK
}
