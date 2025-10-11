package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.ui.components.carousel.BannerCarousel
import com.diegoferreiracaetano.dlearn.ui.components.carousel.Carousel
import com.diegoferreiracaetano.dlearn.ui.components.carousel.ContinueWatchingCarousel
import com.diegoferreiracaetano.dlearn.ui.components.carousel.FullScreenBanner
import com.diegoferreiracaetano.dlearn.ui.components.chip.AppChip
import com.diegoferreiracaetano.dlearn.ui.components.chip.AppChipGroup
import com.diegoferreiracaetano.dlearn.ui.components.navigation.AppBottomNavigation
import com.diegoferreiracaetano.dlearn.ui.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.ui.components.navigation.AppTopBar
import com.diegoferreiracaetano.dlearn.ui.screens.home.state.HomeUiState
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
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

    HomeListScreen(
        uiState = uiState,
        onTabSelected = onTabSelected,
        onItemClick = onItemClick,
        modifier = modifier,
    )
}

 @OptIn(ExperimentalMaterial3Api::class)
 @Composable
 fun HomeListScreen(
     uiState: HomeUiState,
     onTabSelected: (String) -> Unit,
     onItemClick: (String) -> Unit,
     modifier: Modifier = Modifier,
 ) {
    AppContainer(
        modifier = modifier,
        chipContent = {
            AppChipGroup(
                items = listOf(
                    AppChip(label = "Séries"),
                    AppChip(label = "Filmes"),
                    AppChip(label = "Categorias", hasDropDown = true, isFilter = false)
                ),
                onFilterChanged = {}
            )
        },
        topBar = AppTopBar(title = "HOME"),
        bottomBar = AppBottomNavigation(
            onTabSelected = onTabSelected
        )
    ) { modifier ->
        when (uiState) {
            is HomeUiState.Success -> {
                Column(
                    modifier = modifier.verticalScroll(rememberScrollState()),
                ) {
                    uiState.banners?.let {
                        FullScreenBanner(
                            banners = it,
                            onItemClick = { item -> println("Clicked ${item.title}") },
                            onWatchClick = { item -> println("Watch ${item.title}") },
                            onAddToListClick = { item -> println("Add to List ${item.title}") }
                        )
                    }

                    uiState.continueWatching?.let {
                        ContinueWatchingCarousel(
                            title = "Continue Watching",
                            items = it,
                            onItemClick = {}
                        )
                    }

                    uiState.favorites?.let {
                        BannerCarousel(
                            title = "Favorites",
                            banners = it,
                            onItemClick = {}
                        )
                    }

                    uiState.carousels?.forEach { carousel ->
                        Carousel(
                            title = carousel.title,
                            items = carousel.items,
                            onItemClick = {},
                            modifier = Modifier.padding(top = 16.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is HomeUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = uiState.toString() ?: "An unknown error occurred")
                }
            }
        }
    }
 }

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeListScreenSuccessPreview() {
    DLearnTheme {
        HomeListScreen(
            uiState = HomeUiState.Success(
                banners = emptyList(),
                continueWatching = emptyList(),
                favorites = emptyList(),
                carousels = emptyList()
            ),
            onTabSelected = {},
            onItemClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeListScreenLoadingPreview() {
    DLearnTheme {
        HomeListScreen(
            uiState = HomeUiState.Loading,
            onTabSelected = {},
            onItemClick = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeListScreenErrorPreview() {
    DLearnTheme {
    HomeListScreen(
        uiState = HomeUiState.Error(message = "An error occurred"),
        onTabSelected = {},
        onItemClick = {}
    )
        }
}
