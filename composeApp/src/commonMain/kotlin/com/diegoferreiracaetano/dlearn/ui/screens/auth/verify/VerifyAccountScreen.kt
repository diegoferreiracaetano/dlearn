package com.diegoferreiracaetano.dlearn.ui.screens.auth.verify

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.components.button.AppButton
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppTopBar
import com.diegoferreiracaetano.dlearn.designsystem.components.textfield.AppOtpVerification
import com.diegoferreiracaetano.dlearn.ui.screens.auth.verify.state.VerifyAccountUiState
import dlearn.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyAccountScreen(
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VerifyAccountViewModel = koinInject()
) {
    var otpCode by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var otpError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is VerifyAccountUiState.Success -> {
                onContinueClick()
            }
            is VerifyAccountUiState.Error -> otpError = state.message
            else -> otpError = null
        }
    }

    AppContainer(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.verify_account_title),
                onBack = onBackClick
            )
        }
    ) { innerModifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(innerModifier)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = stringResource(Res.string.verify_account_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(Res.string.verify_account_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppOtpVerification(
                otpText = otpCode,
                onOtpTextChange = { text, _ -> 
                    otpCode = text
                    otpError = null 
                },
                onResendClick = { viewModel.resendOtp() },
                modifier = Modifier.fillMaxWidth(),
                isError = otpError != null,
                errorText = otpError
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = stringResource(Res.string.verify_account_action),
                onClick = {
                    viewModel.verifyOtp(otpCode)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = otpCode.length == 6 && uiState !is VerifyAccountUiState.Loading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.otp_resend_prompt),
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(
                    onClick = { viewModel.resendOtp() },
                    enabled = uiState !is VerifyAccountUiState.Loading
                ) {
                    Text(
                        text = stringResource(Res.string.otp_resend_now),
                        fontWeight = FontWeight.Bold,
                        color = if (uiState is VerifyAccountUiState.Loading) 
                            MaterialTheme.colorScheme.onSurfaceVariant 
                        else 
                            MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
