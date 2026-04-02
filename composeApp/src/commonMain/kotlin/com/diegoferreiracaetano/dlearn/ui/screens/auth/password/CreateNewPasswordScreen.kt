package com.diegoferreiracaetano.dlearn.ui.screens.auth.password

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.SnackbarType
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.showAppSnackBar
import com.diegoferreiracaetano.dlearn.designsystem.components.button.AppButton
import com.diegoferreiracaetano.dlearn.designsystem.components.loading.AppLoading
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppTopBar
import com.diegoferreiracaetano.dlearn.designsystem.components.textfield.AppTextField
import com.diegoferreiracaetano.dlearn.designsystem.components.textfield.TextFieldType
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.CreateNewPasswordViewModel
import com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.create_password_action
import dlearn.composeapp.generated.resources.create_password_confirm
import dlearn.composeapp.generated.resources.create_password_subtitle
import dlearn.composeapp.generated.resources.create_password_title
import dlearn.composeapp.generated.resources.title_password
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewPasswordScreen(
    onBackClick: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateNewPasswordViewModel = koinInject(),
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is CreateNewPasswordUiState.Success -> {
                snackbarHostState.showAppSnackBar(
                    scope = scope,
                    message = state.message,
                    type = SnackbarType.SUCCESS,
                )
                onSuccess()
            }
            is CreateNewPasswordUiState.Error -> {
                snackbarHostState.showAppSnackBar(
                    scope = scope,
                    message = state.message,
                    type = SnackbarType.ERROR,
                )
            }
            else -> {}
        }
    }

    // Provê o estado para o AppContainer e sub-componentes
    CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
        AppContainer(
            modifier = modifier,
            snackBarHostState = snackbarHostState,
            topBar = {
                AppTopBar(
                    title = stringResource(Res.string.create_password_title),
                    onBack = onBackClick,
                )
            },
        ) { innerModifier ->
            if (uiState is com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state.CreateNewPasswordUiState.Loading) {
                AppLoading(modifier = Modifier.fillMaxSize())
            }

            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .then(innerModifier)
                        .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                Text(
                    text = stringResource(Res.string.create_password_title),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                )

                Text(
                    text = stringResource(Res.string.create_password_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = Res.string.title_password,
                    label = Res.string.title_password,
                    type = TextFieldType.PASSWORD,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = Res.string.create_password_confirm,
                    label = Res.string.create_password_confirm,
                    type = TextFieldType.PASSWORD,
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(32.dp))

                AppButton(
                    text = stringResource(Res.string.create_password_action),
                    enabled = uiState !is CreateNewPasswordUiState.Loading,
                    onClick = {
                        viewModel.changePassword(password)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
