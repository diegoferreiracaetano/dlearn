package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.UserListScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class UserListOrchestrator(
    private val userRepository: UserRepository,
    private val screenBuilder: UserListScreenBuilder
) : Orchestrator {

    override fun execute(
        request: AppRequest,
        header: AppHeader,
        userId: String
    ): Flow<Screen> = flow {
        val users = userRepository.findAll()
        emit(screenBuilder.build(users))
    }
}
