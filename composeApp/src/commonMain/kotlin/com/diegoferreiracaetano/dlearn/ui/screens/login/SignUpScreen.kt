package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.components.button.AppButton
import com.diegoferreiracaetano.dlearn.designsystem.components.error.factory.AppErrorFactory
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppTopBar
import com.diegoferreiracaetano.dlearn.designsystem.components.textfield.AppTextField
import com.diegoferreiracaetano.dlearn.designsystem.components.textfield.TextFieldType
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.viewmodel.signup.SignUpUIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.signup.SignUpViewModel
import dlearn.composeapp.generated.resources.*
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = koinViewModel()
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is SignUpUIState.Success -> onNavigateToHome()
            is SignUpUIState.Error -> {
                val errorData = AppErrorFactory.invoke(throwable = state.error)
                val message = getString(errorData.title)
                snackbarHostState.showSnackbar(message = message)
            }
            else -> Unit
        }
    }

    SignUpContent(
        onBackClick = onBackClick,
        onSignUpClick = viewModel::signUp,
        isLoading = uiState is SignUpUIState.Loading,
        snackbarHostState = snackbarHostState,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpContent(
    onBackClick: () -> Unit,
    onSignUpClick: (String, String, String) -> Unit,
    isLoading: Boolean,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AppContainer(
        modifier = modifier,
        snackBarHostState = snackbarHostState,
        isLoading = isLoading,
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.signup_action),
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
                text = stringResource(Res.string.signup_screen_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(Res.string.signup_screen_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = Res.string.title_name,
                label = Res.string.title_name,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

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

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(Res.string.signup_agree_terms),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = Res.string.signup_action,
                onClick = { onSignUpClick(name, email, password) },
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading
            )
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    DLearnTheme {
        SignUpContent(
            onBackClick = {},
            onSignUpClick = { _, _, _ -> },
            isLoading = false,
            snackbarHostState = remember { SnackbarHostState() }
        )
    }
}
