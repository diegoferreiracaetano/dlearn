package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.screens.home.state.HomeUiState
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavItem
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.CarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipItem
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.util.ErrorScreen
import com.diegoferreiracaetano.dlearn.ui.util.LoadingScreen
import com.diegoferreiracaetano.dlearn.ui.util.RenderComponent
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun HomeScreen(
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
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
                onSearchChanged = viewModel::onSearchChanged,
                modifier = modifier,
            )
        }
        is HomeUiState.Loading -> LoadingScreen()
        is HomeUiState.Error -> ErrorScreen(state.message)
    }
}

@Composable
fun HomeListScreen(
    screen: Screen,
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onFilterTypeChanged: (String?) -> Unit,
    onSearchChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }

    screen.components.forEach { component ->
        RenderComponent(
            modifier = modifier,
            component = component,
            onItemClick = onItemClick,
            onFilterTypeChanged = onFilterTypeChanged,
            onSearchChanged = onSearchChanged,
            onTabSelected = onTabSelected,
            searchText = searchText,
            onSearchTextChange = { searchText = it }
        )
    }

}

@Preview
@Composable
fun HomeListScreenPreview() {
    val screen = Screen(
        id = "home",
        components = listOf(
            AppContainerComponent(
                topBar = AppTopBarComponent(title = "DLearn"),
                bottomBar = BottomNavigationComponent(items = listOf(BottomNavItem("Home", "/home", "home"))),
                components = listOf(
                    ChipGroupComponent(id = "2", items = listOf(ChipItem(id = "1", label = "Séries"))),
                    FullScreenBannerComponent(id = "3", title = "Banner", subtitle = "2024", imageUrl = ""),
                    CarouselComponent(title = "Top 10", items = listOf()),
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
            onSearchChanged = {}
        )
    }
}
