package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.designsystem.components.list.AppList
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponentFactory
import com.diegoferreiracaetano.dlearn.ui.screens.home.state.HomeUiState
import com.diegoferreiracaetano.dlearn.ui.screens.main.LocalMainContainerState
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppIconType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavItem
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipItem
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onClose: () -> Unit,
    onShowSearchChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val containerState = LocalMainContainerState.current
    var searchText by remember { mutableStateOf("") }

    val actions = remember(onItemClick, viewModel, onTabSelected, searchText) {
        ComponentActions(
            onItemClick = onItemClick,
            onFilterTypeChanged = viewModel::onFilterTypeChanged,
            onSearchChanged = viewModel::onSearchChanged,
            onTabSelected = onTabSelected,
            searchText = searchText,
            onSearchTextChange = { searchText = it },
            onRetry = viewModel::retry
        )
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is HomeUiState.Success -> {
                onShowSearchChanged(state.screen.showSearch)
                containerState.onMainLoading(false)
            }
            is HomeUiState.Loading -> containerState.onMainLoading(true)
            is HomeUiState.Error -> containerState.onMainError(state.throwable)
        }
    }

    (uiState as? HomeUiState.Success)?.let { state ->
        HomeListContent(
            components = state.screen.components,
            actions = actions,
            modifier = modifier,
        )
    }
}

@Composable
fun HomeListContent(
    components: List<Component>,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    components.forEach { component->
        RenderComponentFactory.Render(
            component = component,
            actions = actions,
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun HomeListScreenPreview() {
    val components = listOf(
        AppContainerComponent(
            topBar = AppTopBarComponent(title = "DLearn"),
            bottomBar = BottomNavigationComponent(
                items = listOf(
                    BottomNavItem(
                        "Home",
                        NavigationRoutes.HOME,
                        AppIconType.PERSON
                    )
                ),
                selectedRoute = NavigationRoutes.HOME
            ),
            components = listOf(
                ChipGroupComponent(
                    id = "2",
                    items = listOf(ChipItem(id = "1", label = "Séries"))
                ),
                FullScreenBannerComponent(
                    id = "3",
                    title = "Banner",
                    subtitle = "2024",
                    imageUrl = ""
                ),
                MovieCarouselComponent(title = "Top 10", items = listOf()),
                BannerCarouselComponent(title = "Populares", items = listOf())
            )
        )
    )

    DLearnTheme {
        HomeListContent(
            components = components,
            actions = ComponentActions()
        )
    }
}
