package com.diegoferreiracaetano.dlearn.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.SnackbarType
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.showAppSnackBar
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.util.event.GlobalEvent
import kotlinx.coroutines.CoroutineScope

/**
 * Factory/Handler centralizado para processar eventos globais da aplicação.
 * Desativa a lógica de decisão do AppNavGraph, mantendo-o focado apenas na estrutura.
 */
class GlobalEventHandler(
    private val navController: NavHostController,
    private val snackbarHostState: SnackbarHostState,
    private val scope: CoroutineScope
) {
    fun handle(event: GlobalEvent) {
        when (event) {
            is GlobalEvent.Challenge -> handleChallenge(event)
            is GlobalEvent.Navigation -> handleNavigation(event)
            is GlobalEvent.Message -> handleMessage(event)
        }
    }

    private fun handleChallenge(event: GlobalEvent.Challenge) {
        // Verificamos se há algum desafio pendente na sessão que requer ação do usuário
        val hasOtpChallenge = event.session.challenge.let {
            it.challengeType == ChallengeType.OTP_SMS || it.challengeType == ChallengeType.OTP_EMAIL
        }

        if (hasOtpChallenge) {
            // Abre o desafio como um DIALOG por cima da tela atual
            navController.navigate(AppNavigationRoute.VERIFY_ACCOUNT)
        }
    }

    private fun handleNavigation(event: GlobalEvent.Navigation) {
        navController.navigate(event.route)
    }

    private fun handleMessage(event: GlobalEvent.Message) {
        snackbarHostState.showAppSnackBar(
            scope = scope,
            message = event.text,
            type = event.type.toSnackbarType()
        )
    }

    private fun GlobalEvent.MessageType.toSnackbarType(): SnackbarType = when (this) {
        GlobalEvent.MessageType.SUCCESS -> SnackbarType.SUCCESS
        GlobalEvent.MessageType.ERROR -> SnackbarType.ERROR
        GlobalEvent.MessageType.WARNING -> SnackbarType.WARNING
        GlobalEvent.MessageType.INFO -> SnackbarType.SUCCESS // Fallback para SUCCESS já que o Design System não tem INFO
    }
}
