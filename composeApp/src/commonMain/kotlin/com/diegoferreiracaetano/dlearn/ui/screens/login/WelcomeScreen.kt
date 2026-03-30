package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.components.button.AppButton
import com.diegoferreiracaetano.dlearn.designsystem.components.button.ButtonType
import com.diegoferreiracaetano.dlearn.designsystem.components.image.toAppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.ui.theme.facebookColor
import com.diegoferreiracaetano.dlearn.ui.util.toAppMessage
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginUIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel
import dlearn.composeapp.generated.resources.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WelcomeScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading = uiState is LoginUIState.Loading

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUIState.Success -> { onNavigateToHome() }
            is LoginUIState.Error -> {
                snackbarHostState.showSnackbar(state.throwable.toAppMessage())
            } else -> Unit
        }
    }

    WelcomeContent(
        onSignUpClick = onSignUpClick,
        onLoginClick = onLoginClick,
        onSocialClick = { provider -> viewModel.signInWith(provider) },
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeContent(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onSocialClick: (AccountProvider) -> Unit,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    AppContainer(
        modifier = modifier,
        snackBarHostState = snackbarHostState
    ) { innerModifier ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .then(innerModifier)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.dlearn_logo),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(Res.string.app_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(48.dp))

            if (isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
            }

            AppButton(
                text = Res.string.login_action,
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(Res.string.onboarding_already_have_account),
                    style = MaterialTheme.typography.bodyMedium
                )
                TextButton(
                    onClick = onSignUpClick,
                    enabled = !isLoading
                ) {
                    Text(
                        text = stringResource(Res.string.signup_action),
                        fontWeight = FontWeight.Bold,
                        color = if (isLoading) Color.Gray else MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(Res.string.onboarding_or_signup_with),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AppButton(
                    onClick = { onSocialClick(AccountProvider.GOOGLE) },
                    type = ButtonType.SECONDARY,
                    imageSource = Res.drawable.google.toAppImageSource(),
                    iconTint = Color.Unspecified,
                    modifier = Modifier.size(64.dp),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.width(16.dp))

                AppButton(
                    onClick = { onSocialClick(AccountProvider.APPLE) },
                    type = ButtonType.TERTIARY,
                    imageSource = Res.drawable.apple.toAppImageSource(),
                    modifier = Modifier.size(64.dp),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.width(16.dp))

                AppButton(
                    onClick = { onSocialClick(AccountProvider.FACEBOOK) },
                    imageSource = Res.drawable.facebook.toAppImageSource(),
                    iconTint = Color.Unspecified,
                    modifier = Modifier.size(64.dp),
                    backgroundColor = facebookColor,
                    enabled = !isLoading
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    val snackbarHostState = remember { SnackbarHostState() }
    DLearnTheme {
        WelcomeContent(
            onSignUpClick = {},
            onLoginClick = {},
            onSocialClick = {},
            isLoading = false,
            snackbarHostState = snackbarHostState
        )
    }
}
