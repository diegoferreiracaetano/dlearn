package com.diegoferreiracaetano.dlearn.data.user.source.remote

import com.diegoferreiracaetano.dlearn.domain.user.User

class UserRemote(
    val id: String,
    val name: String,
    val email: String,
    val password: String,
    val imageUrl: String? = null,
    val isPremium: Boolean = false,
    val phoneNumber: String? = null
)

fun List<UserRemote>.toDomainList() = map(UserRemote::toDomain)

fun UserRemote.toDomain() =
    User(
        id = id,
        name = name,
        email = email,
        password = password,
        imageUrl = imageUrl,
        isPremium = isPremium,
        phoneNumber = phoneNumber
    )

fun List<User>.toExternal() = map(User::toExternal)

fun User.toExternal() =
    UserRemote(
        id = id,
        name = name,
        email = email,
        password = password ?: "",
        imageUrl = imageUrl,
        isPremium = isPremium,
        phoneNumber = phoneNumber
    )
