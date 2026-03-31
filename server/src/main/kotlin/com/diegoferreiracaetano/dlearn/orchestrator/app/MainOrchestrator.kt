package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.MainScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainOrchestrator(
    private val mainScreenBuilder: MainScreenBuilder,
    private val userRepository: UserRepository
) : Orchestrator {
    override fun execute(
        request: AppRequest,
        header: AppHeader,
        userId: String
    ): Flow<Screen> = flow {
        val user = userRepository.findById(userId)
        emit(
            mainScreenBuilder.build(
                lang = header.language,
                user = user
            )
        )
    }
}
