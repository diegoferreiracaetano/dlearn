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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.button.AppButton
import com.diegoferreiracaetano.dlearn.designsystem.components.button.ButtonType
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.theme.facebookColor
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WelcomeScreen(
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppContainer(modifier = modifier) { innerModifier ->
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

            AppButton(
                text = stringResource(Res.string.signup_action),
                onClick = onSignUpClick,
                modifier = Modifier.fillMaxWidth()
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
                TextButton(onClick = onLoginClick) {
                    Text(
                        text = stringResource(Res.string.login_action),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
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
                    onClick = onSignUpClick,
                    type = ButtonType.SECONDARY,
                    image = Res.drawable.google,
                    iconTint = Color.Unspecified,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                AppButton(
                    onClick = onSignUpClick,
                    type = ButtonType.TERTIARY,
                    image = Res.drawable.apple,
                    modifier = Modifier.size(64.dp),
                )

                Spacer(modifier = Modifier.width(16.dp))

                AppButton(
                    onClick = onSignUpClick,
                    image = Res.drawable.facebook,
                    iconTint = Color.Unspecified,
                    modifier = Modifier.size(64.dp),
                    backgroundColor = facebookColor
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    DLearnTheme{
        WelcomeScreen(onSignUpClick = {}, onLoginClick = {})
    }
}
