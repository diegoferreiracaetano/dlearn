package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onTabSelected: (String) -> Unit,
    onSearchClick: (() -> Unit)?,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var searchText by remember { mutableStateOf("") }

    val actions = remember(onItemClick, viewModel, onTabSelected, searchText) {
        ComponentActions(
            onItemClick = onItemClick,
            onFilterTypeChanged = viewModel::onFilterTypeChanged,
            onSearchClick = onSearchClick,
            onTabSelected = onTabSelected,
            onRetry = viewModel::retry
        )
    }

    HomeContent(
        uiState = uiState,
        actions = actions,
        modifier = modifier
    )
}

@Composable
fun HomeContent(
    uiState: UIState,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    uiState.Render(actions, modifier)
}

@Preview
@Composable
fun HomeScreenPreview() {
    val components = listOf(
        AppContainerComponent(
            topBar = AppTopBarComponent(title = "DLearn"),
            bottomBar = BottomNavigationComponent(
                items = listOf(
                    BottomNavItem("Home", NavigationRoutes.HOME, AppIconType.PERSON)
                ),
                selectedRoute = NavigationRoutes.HOME
            ),
            components = listOf(
                ChipGroupComponent(items = listOf(ChipItem(id = "1", label = "Séries"))),
                FullScreenBannerComponent(id = "3", title = "Banner", subtitle = "2024", imageUrl = ""),
                MovieCarouselComponent(title = "Top 10", items = listOf()),
                BannerCarouselComponent(title = "Populares", items = listOf())
            )
        )
    )

    DLearnTheme {
        HomeContent(
            uiState = UIState.Success(Screen(components = components)),
            actions = ComponentActions()
        )
    }
}
