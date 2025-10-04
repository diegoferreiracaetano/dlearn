package com.diegoferreiracaetano.dlearn.domain.user

import com.diegoferreiracaetano.dlearn.data.user.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class CreateAccountUseCase(
    private val repository: UserRepository,
) : UseCase<Pair<CreateAccountStepType, String>, Flow<Boolean>> {
    private val savedData = MutableStateFlow<Map<CreateAccountStepType, String>>(emptyMap())

    override fun invoke(params: Pair<CreateAccountStepType, String>) =
        flow {
            savedData.update { it + params }

            val result =
                if (isFinished()) {
                    repository.save(user())
                    true
                } else {
                    false
                }

            emit(result)
        }

    private fun isFinished() = savedData.value.size == CreateAccountStepType.entries.size

    private fun user() =
        User(
            name = savedData.value[CreateAccountStepType.NAME]!!,
            email = savedData.value[CreateAccountStepType.EMAIL]!!,
            password = savedData.value[CreateAccountStepType.PASSWORD]!!,
        )
}
