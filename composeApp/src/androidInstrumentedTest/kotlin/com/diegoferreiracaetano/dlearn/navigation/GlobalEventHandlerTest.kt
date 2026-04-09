package com.diegoferreiracaetano.dlearn.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.navigation.NavHostController
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.util.event.GlobalEvent
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.junit.Test

class GlobalEventHandlerTest {

    private val navController: NavHostController = mockk(relaxed = true)
    private val snackbarHostState: SnackbarHostState = mockk(relaxed = true)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private val globalEventHandler = GlobalEventHandler(navController, snackbarHostState, scope)

    @Test
    fun given_navigation_event_when_handle_should_navigate_to_route() {
        val route = "target_route"
        val event = GlobalEvent.Navigation(route)

        globalEventHandler.handle(event)

        verify { navController.navigate(route) }
        confirmVerified(navController)
    }

    @Test
    fun given_otp_challenge_event_when_handle_should_navigate_to_verify_account() {
        val challengeSession = ChallengeSession(
            transactionId = "tx123",
            challenge = Challenge(challengeType = ChallengeType.OTP_EMAIL)
        )
        val event = GlobalEvent.Challenge(challengeSession)

        globalEventHandler.handle(event)

        verify { navController.navigate(AppNavigationRoute.VERIFY_ACCOUNT) }
    }

    @Test
    fun given_message_event_when_handle_should_process_event() {
        val message = "success message"
        val event = GlobalEvent.Message(message, GlobalEvent.MessageType.SUCCESS)

        globalEventHandler.handle(event)

        coVerify(timeout = 2000) { snackbarHostState.showSnackbar(any<SnackbarVisuals>()) }
    }
}
