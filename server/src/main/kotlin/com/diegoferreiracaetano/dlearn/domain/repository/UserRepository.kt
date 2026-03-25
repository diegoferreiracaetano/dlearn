package com.diegoferreiracaetano.dlearn.domain.repository

import com.diegoferreiracaetano.dlearn.domain.user.User

interface UserRepository {
    suspend fun findByEmail(email: String): User?
    suspend fun findById(id: String): User?
    suspend fun findAll(): List<User>
    suspend fun save(user: User): User
}
