package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.create_password_action
import dlearn.composeapp.generated.resources.create_password_confirm
import dlearn.composeapp.generated.resources.create_password_subtitle
import dlearn.composeapp.generated.resources.create_password_title
import dlearn.composeapp.generated.resources.title_password
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateNewPasswordScreen(
    onBackClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    AppContainer(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = stringResource(Res.string.create_password_title),
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
                text = stringResource(Res.string.create_password_title),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(Res.string.create_password_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = Res.string.title_password,
                label = Res.string.title_password,
                type = TextFieldType.PASSWORD,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                placeholder = Res.string.create_password_confirm,
                label = Res.string.create_password_confirm,
                type = TextFieldType.PASSWORD,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            AppButton(
                text = stringResource(Res.string.create_password_action),
                onClick = onResetClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun CreateNewPasswordScreenPreview() {
    DLearnTheme {
        CreateNewPasswordScreen(onBackClick = {}, onResetClick = {})
    }
}
