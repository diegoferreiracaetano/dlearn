package com.diegoferreiracaetano.dlearn.data.user.source.remote

import com.diegoferreiracaetano.dlearn.data.user.UserRepository
import com.diegoferreiracaetano.dlearn.domain.user.User
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserRepositoryRemote(
    private val dataSource: UserNetworkDataSource,
) : UserRepository {
    companion object {
        private const val MOCK_DELAY_MS = 2000L
    }

    private var code: String? = null

    override fun users() =
        flow {
            emit(dataSource.loadUser().toDomainList())
        }

    override suspend fun save(user: User) {
        dataSource.saveUser(user.toRemote())
    }

    override fun findByUser(
        email: String,
        password: String,
    ) = flow {
        val user =
            dataSource
                .findByUser(
                    email,
                    password,
                )?.toDomain()

        emit(user)
    }

    override fun findByEmail(email: String): Flow<User?> =
        flow {
            emit(dataSource.findByEmail(email)?.toDomain())
        }

    override suspend fun sendCode(email: String) {
        delay(MOCK_DELAY_MS)
        code = email + "123456"
    }

    override suspend fun getCode(): String? {
        delay(MOCK_DELAY_MS)
        return code
    }
}
