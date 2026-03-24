package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.button.AppButton
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppTopBar
import com.diegoferreiracaetano.dlearn.designsystem.components.textfield.AppTextField
import com.diegoferreiracaetano.dlearn.designsystem.components.textfield.TextFieldType
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginUIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.login.LoginViewModel
import dlearn.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.checkSession()
    }

    LaunchedEffect(state) {
        if (state is LoginUIState.Success) {
            onNavigateToHome()
        }
    }

    AppContainer(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.login_action),
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
                text = stringResource(Res.string.login_screen_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(Res.string.login_screen_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = Res.string.title_email,
                label = Res.string.title_email,
                type = TextFieldType.EMAIL,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = Res.string.title_password,
                label = Res.string.title_password,
                type = TextFieldType.PASSWORD,
                modifier = Modifier.fillMaxWidth()
            )

            if (state is LoginUIState.Error) {
                Text(
                    text = (state as LoginUIState.Error).throwable.message ?: "Erro ao realizar login",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            TextButton(
                onClick = onForgotPasswordClick,
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = stringResource(Res.string.login_forgot_password),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (state is LoginUIState.Loading) {
                CircularProgressIndicator()
            } else {
                AppButton(
                    text = stringResource(Res.string.login_action),
                    onClick = { viewModel.login(email, password) },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    DLearnTheme {
        LoginScreen(onBackClick = {}, onNavigateToHome = {}, onForgotPasswordClick = {})
    }
}
