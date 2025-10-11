package com.diegoferreiracaetano.dlearn.ui.components.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.ui.components.alert.AppSnackbarHost
import com.diegoferreiracaetano.dlearn.ui.components.chip.AppChip
import com.diegoferreiracaetano.dlearn.ui.components.chip.AppChipGroup
import com.diegoferreiracaetano.dlearn.ui.components.loading.AppLoading
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.util.UiState
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.login_screen_title
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppScaffoldContent(
    modifier: Modifier = Modifier,
    topBar: AppTopBar? = null,
    chipContent: @Composable (() -> Unit)? = null,
    bottomBar: AppBottomNavigation? = null,
    snackBarHostState: SnackbarHostState,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable (Modifier) -> Unit
) {
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { AppSnackbarHost(hostState = snackBarHostState) },
        topBar = {
            Column {
                topBar?.let {
                    AppTopBarFactory(
                        config = it,
                        scrollBehavior = scrollBehavior
                    )
                }
                chipContent?.invoke()
            }
        },
        bottomBar = {
            bottomBar?.let {
                AppBottomNavigationBar(
                    items = it.items,
                    onTabSelected = it.onTabSelected,
                    selectedRoute = it.selectedRoute
                )
            }
        }
    ) { innerPadding ->
        val baseModifier = modifier
            .fillMaxWidth()
            .padding(innerPadding)
            .consumeWindowInsets(innerPadding)
            .systemBarsPadding()
            .padding(start = 16.dp, end = 16.dp)

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            content(baseModifier)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContainer(
    modifier: Modifier = Modifier,
    topBar: AppTopBar? = null,
    chipContent: @Composable (() -> Unit)? = null,
    bottomBar: AppBottomNavigation? = null,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
    content: @Composable (Modifier) -> Unit
) {
    AppScaffoldContent(
        modifier = modifier,
        topBar = topBar,
        chipContent = chipContent,
        bottomBar = bottomBar,
        snackBarHostState = snackBarHostState,
        scrollBehavior = scrollBehavior,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppContainer(
    uiState: UiState<T>?,
    modifier: Modifier = Modifier,
    topBar: AppTopBar? = null,
    chipContent: @Composable (() -> Unit)? = null,
    bottomBar: AppBottomNavigation? = null,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onLoading: @Composable () -> Unit = { AppLoading() },
    content: @Composable (modifier: Modifier, data: T) -> Unit
) {
    LaunchedEffect(uiState?.error) {
        uiState?.error?.let { snackBarHostState.showSnackbar(it) }
    }

    AppScaffoldContent(
        modifier = modifier,
        topBar = topBar,
        chipContent = chipContent,
        bottomBar = bottomBar,
        snackBarHostState = snackBarHostState,
        scrollBehavior = scrollBehavior
    ) { baseModifier ->
        when {
            uiState == null || uiState.isLoading -> onLoading()
            uiState.success != null -> content(baseModifier, uiState.success)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun AppTopBarPreview() {
    DLearnTheme(darkTheme = true) {
        AppContainer(
            topBar = AppTopBar(
                title = "Create",
                backgroundColor = MaterialTheme.colorScheme.background,
                onBack = {}
            ),
            chipContent = {
                AppChipGroup(
                    items = listOf(
                        AppChip(label = "Séries"),
                        AppChip(label = "Filmes"),
                        AppChip(label = "Categorias", hasDropDown = true, isFilter = false)
                    ),
                    onFilterChanged = {}
                )
            }
        ) { padding ->
            Column(
                modifier = padding,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(Res.string.login_screen_title),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}