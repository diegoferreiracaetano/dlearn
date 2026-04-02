package com.diegoferreiracaetano.dlearn.data.user.source.remote

import com.diegoferreiracaetano.dlearn.domain.user.AuthProvider
import com.diegoferreiracaetano.dlearn.domain.user.User
import kotlinx.serialization.Serializable

@Serializable
data class UserRemote(
    val id: String,
    val name: String,
    val email: String,
    val password: String? = null,
    val imageUrl: String? = null,
    val isPremium: Boolean = false,
    val phoneNumber: String? = null,
    val authProviders: List<AuthProvider> = emptyList(),
)

fun List<UserRemote>.toDomainList() = map(UserRemote::toDomain)

fun UserRemote.toDomain() =
    User(
        id = id,
        name = name,
        email = email,
        imageUrl = imageUrl,
        isPremium = isPremium,
        phoneNumber = phoneNumber,
    )

fun List<User>.toRemoteList() = map { it.toRemote() }

fun User.toRemote(
    password: String? = null,
    authProviders: List<AuthProvider> = emptyList(),
) = UserRemote(
    id = id,
    name = name,
    email = email,
    password = password,
    imageUrl = imageUrl,
    isPremium = isPremium,
    phoneNumber = phoneNumber,
    authProviders = authProviders,
)
