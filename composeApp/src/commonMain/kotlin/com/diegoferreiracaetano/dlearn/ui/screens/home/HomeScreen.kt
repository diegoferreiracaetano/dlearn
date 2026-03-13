package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.ComponentIds
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.designsystem.components.error.AppError
import com.diegoferreiracaetano.dlearn.designsystem.components.loading.AppLoading
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponentFactory
import com.diegoferreiracaetano.dlearn.ui.screens.home.state.HomeUiState
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppIconType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavItem
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipItem
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is HomeUiState.Success -> {
            HomeListScreen(
                screen = state.screen,
                onTabSelected = onTabSelected,
                onItemClick = onItemClick,
                onFilterTypeChanged = viewModel::onFilterTypeChanged,
                onCategoryChanged = viewModel::onCategoryChanged,
                onSearchChanged = viewModel::onSearchChanged,
                modifier = modifier,
            )
        }
        is HomeUiState.Loading -> AppLoading()
        is HomeUiState.Error -> {
            AppError(
                throwable = state.throwable,
                modifier = modifier,
                onPrimary = viewModel::retry,
                onClose = onClose
            )
        }
    }
}

@Composable
fun HomeListScreen(
    screen: Screen,
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onFilterTypeChanged: (String?) -> Unit,
    onCategoryChanged: (String?, String?) -> Unit,
    onSearchChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }

    val actions = remember(onItemClick, onFilterTypeChanged, onCategoryChanged, onSearchChanged, onTabSelected, searchText) {
        ComponentActions(
            onItemClick = onItemClick,
            onFilterTypeChanged = onFilterTypeChanged,
            onCategoryChanged = onCategoryChanged,
            onSearchChanged = onSearchChanged,
            onTabSelected = onTabSelected,
            searchText = searchText,
            onSearchTextChange = { searchText = it }
        )
    }

    screen.components.forEach { component ->
        RenderComponentFactory.Render(
            modifier = modifier,
            component = component,
            actions = actions
        )
    }
}

@Preview
@Composable
fun HomeListScreenPreview() {
    val screen = Screen(
        id = ComponentIds.HOME_SCREEN,
        components = listOf(
            AppContainerComponent(
                topBar = AppTopBarComponent(title = "DLearn"),
                bottomBar = BottomNavigationComponent(
                    items = listOf(BottomNavItem("Home", NavigationRoutes.HOME, AppIconType.PERSON)),
                    selectedRoute = NavigationRoutes.HOME
                ),
                components = listOf(
                    ChipGroupComponent(id = "2", items = listOf(ChipItem(id = "1", label = "Séries"))),
                    FullScreenBannerComponent(id = "3", title = "Banner", subtitle = "2024", imageUrl = ""),
                    MovieCarouselComponent(title = "Top 10", items = listOf()),
                    BannerCarouselComponent(title = "Populares", items = listOf())
                )
            )
        )
    )

    DLearnTheme {
        HomeListScreen(
            screen = screen,
            onTabSelected = {},
            onItemClick = {},
            onFilterTypeChanged = {},
            onCategoryChanged = { _, _ -> },
            onSearchChanged = {}
        )
    }
}
