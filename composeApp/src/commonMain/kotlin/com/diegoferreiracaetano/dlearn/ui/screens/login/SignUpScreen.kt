package com.diegoferreiracaetano.dlearn.ui.screens.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import dlearn.composeapp.generated.resources.signup_action
import dlearn.composeapp.generated.resources.signup_agree_terms
import dlearn.composeapp.generated.resources.signup_screen_subtitle
import dlearn.composeapp.generated.resources.signup_screen_title
import dlearn.composeapp.generated.resources.title_email
import dlearn.composeapp.generated.resources.title_name
import dlearn.composeapp.generated.resources.title_password
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    onBackClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    AppContainer(
        modifier = modifier,
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
                text = stringResource(Res.string.signup_action),
                onClick = onSignUpClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview() {
    DLearnTheme {
        SignUpScreen(onBackClick = {}, onSignUpClick = {})
    }
}
