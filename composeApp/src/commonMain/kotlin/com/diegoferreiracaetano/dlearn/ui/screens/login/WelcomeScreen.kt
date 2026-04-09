package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
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
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import com.diegoferreiracaetano.dlearn.ui.util.toAppMessage
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginUIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.app_name
import dlearn.composeapp.generated.resources.app_subtitle
import dlearn.composeapp.generated.resources.apple
import dlearn.composeapp.generated.resources.dlearn_logo
import dlearn.composeapp.generated.resources.facebook
import dlearn.composeapp.generated.resources.google
import dlearn.composeapp.generated.resources.login_action
import dlearn.composeapp.generated.resources.onboarding_already_have_account
import dlearn.composeapp.generated.resources.onboarding_or_signup_with
import dlearn.composeapp.generated.resources.signup_action
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
    viewModel: LoginViewModel = koinViewModel(),
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading = uiState is LoginUIState.Loading

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is LoginUIState.Success -> {
                onNavigateToHome()
            }

            is LoginUIState.Error -> {
                snackbarHostState.showSnackbar(state.throwable.toAppMessage())
            }

            else -> Unit
        }
    }

    WelcomeContent(
        onSignUpClick = onSignUpClick,
        onLoginClick = onLoginClick,
        onSocialClick = { provider -> viewModel.signInWith(provider) },
        isLoading = isLoading,
        snackbarHostState = snackbarHostState,
        modifier = modifier.testTag(TestTags.Screens.WELCOME_SCREEN),
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
        snackBarHostState = snackbarHostState,
    ) { innerModifier ->
        Column(
            modifier =
            Modifier
                .fillMaxSize()
                .then(innerModifier)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(Res.drawable.dlearn_logo),
                contentDescription = null,
                modifier = Modifier.size(120.dp),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = stringResource(Res.string.app_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(48.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.testTag(TestTags.Components.LOADING_INDICATOR))
                Spacer(modifier = Modifier.height(16.dp))
            }

            AppButton(
                text = Res.string.login_action,
                onClick = onLoginClick,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                testTag = TestTags.Components.LOGIN_BUTTON,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = stringResource(Res.string.onboarding_already_have_account),
                    style = MaterialTheme.typography.bodyMedium,
                )
                TextButton(
                    onClick = onSignUpClick,
                    enabled = !isLoading,
                    modifier = Modifier.testTag(TestTags.Components.SIGN_UP_BUTTON),
                ) {
                    Text(
                        text = stringResource(Res.string.signup_action),
                        fontWeight = FontWeight.Bold,
                        color = if (isLoading) Color.Gray else MaterialTheme.colorScheme.primary,
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(Res.string.onboarding_or_signup_with),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
            ) {
                AppButton(
                    onClick = { onSocialClick(AccountProvider.GOOGLE) },
                    type = ButtonType.SECONDARY,
                    imageSource = Res.drawable.google.toAppImageSource(),
                    iconTint = Color.Unspecified,
                    modifier = Modifier.size(64.dp),
                    enabled = !isLoading,
                    testTag = TestTags.Components.GOOGLE_BUTTON,
                )

                Spacer(modifier = Modifier.width(16.dp))

                AppButton(
                    onClick = { onSocialClick(AccountProvider.APPLE) },
                    type = ButtonType.TERTIARY,
                    imageSource = Res.drawable.apple.toAppImageSource(),
                    modifier = Modifier.size(64.dp),
                    enabled = !isLoading,
                    testTag = TestTags.Components.APPLE_BUTTON,
                )

                Spacer(modifier = Modifier.width(16.dp))

                AppButton(
                    onClick = { onSocialClick(AccountProvider.FACEBOOK) },
                    imageSource = Res.drawable.facebook.toAppImageSource(),
                    iconTint = Color.Unspecified,
                    modifier = Modifier.size(64.dp),
                    backgroundColor = facebookColor,
                    enabled = !isLoading,
                    testTag = TestTags.Components.FACEBOOK_BUTTON,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun WelcomeScreenIdlePreview() {
    DLearnTheme {
        WelcomeContent(
            onSignUpClick = {},
            onLoginClick = {},
            onSocialClick = {},
            isLoading = false,
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}

@Preview
@Composable
fun WelcomeScreenLoadingPreview() {
    DLearnTheme {
        WelcomeContent(
            onSignUpClick = {},
            onLoginClick = {},
            onSocialClick = {},
            isLoading = true,
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}
