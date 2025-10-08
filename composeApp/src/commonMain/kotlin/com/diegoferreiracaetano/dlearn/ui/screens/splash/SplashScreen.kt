package com.diegoferreiracaetano.dlearn.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.app_name
import dlearn.composeapp.generated.resources.app_subtitle
import dlearn.composeapp.generated.resources.dlearn_logo
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val SPLASH_WAIT_TIME: Long = 2000

@Composable
fun SplashScreen(
    onTimeout: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnTimeout by rememberUpdatedState(onTimeout)

    @OptIn(ExperimentalResourceApi::class)
    Box(
        modifier =
            modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        LaunchedEffect(Unit) {
            delay(SPLASH_WAIT_TIME)
            currentOnTimeout()
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.dlearn_logo),
                contentDescription = null,
                modifier =
                    Modifier
                        .heightIn(max = 90.dp)
            )

            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(top = 16.dp),
            )

            Text(
                text = stringResource(Res.string.app_subtitle),
                fontStyle = FontStyle.Italic,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}

@Preview
@Composable
private fun SplashScreenPreview() {
    DLearnTheme {
        SplashScreen({ })
    }
}
