package com.diegoferreiracaetano.dlearn.ui.screens.new

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppBottomNavigation
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppBottomNavigationBar
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppTopBar
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewScreen(
    onTabSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AppContainer(
        modifier = modifier,
        topBar = { AppTopBar(title = "NOVIDADES") },
        bottomBar = {
            val nav = AppBottomNavigation(selectedRoute = "New", onTabSelected = onTabSelected)
            AppBottomNavigationBar(
                items = nav.items,
                selectedRoute = nav.selectedRoute,
                onTabSelected = nav.onTabSelected
            )
        }
    ) { innerModifier ->
        Box(
            modifier = innerModifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Tela de Novidades",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

@Preview
@Composable
fun NewScreenPreview() {
    DLearnTheme {
        NewScreen(onTabSelected = {})
    }
}
