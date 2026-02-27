package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.domain.home.Home
import com.diegoferreiracaetano.dlearn.domain.home.HomeCategory
import com.diegoferreiracaetano.dlearn.domain.home.HomeCategoryItems
import com.diegoferreiracaetano.dlearn.domain.home.HomeDataContent
import com.diegoferreiracaetano.dlearn.domain.home.HomeLayoutSection
import com.diegoferreiracaetano.dlearn.domain.home.HomeSectionType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.ui.components.carousel.BannerCarousel
import com.diegoferreiracaetano.dlearn.ui.components.carousel.Carousel
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
        collapsibleContent = {
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
    ) {
        when (uiState) {
            is HomeUiState.Success -> {
                val homeData = uiState.home.data
                items(uiState.home.layout) { section ->
                    Section(section = section, data = homeData, onItemClick = onItemClick)
                }
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            is HomeUiState.Loading -> {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }

            is HomeUiState.Error -> {
                item {
                    Box(
                        modifier = Modifier.fillParentMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = uiState.message ?: "An unknown error occurred")
                    }
                }
            }
        }
    }
}

@Composable
private fun Section(
    section: HomeLayoutSection,
    data: HomeDataContent,
    onItemClick: (String) -> Unit
) {
    when (section.type) {
        HomeSectionType.BANNER_MAIN -> {
            data.bannerMain?.let {
                FullScreenBanner(
                    banners = listOf(it),
                    onItemClick = { item -> onItemClick(item.id) },
                    onWatchClick = { item -> println("Watch ${item.title}") },
                    onAddToListClick = { item -> println("Add to List ${item.title}") }
                )
            }
        }

        HomeSectionType.TOP_10 -> {
            Carousel(
                title = section.title.orEmpty(),
                showRanking = true,
                items = data.top10,
                onItemClick = { onItemClick(it.id) },
                modifier = Modifier.padding(top = 16.dp),
            )
        }

        HomeSectionType.POPULAR -> {
            BannerCarousel(
                title = section.title.orEmpty(),
                banners = data.popular,
                onItemClick = { onItemClick(it.id) }
            )
        }

        HomeSectionType.CATEGORY -> {
            data.categories.forEach { category ->
                Carousel(
                    title = category.category.name,
                    items = category.items,
                    onItemClick = { onItemClick(it.id) },
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomeListScreenPreview() {
    val video = Video(
        id = "1",
        title = "Title",
        subtitle = "Subtitle",
        description = "Description",
        url = "",
        imageUrl = ""
    )
    val homeData = HomeDataContent(
        bannerMain = video,
        top10 = (1..10).map {
            Video(
                id = "$it",
                title = "Title $it",
                subtitle = "Subtitle $it",
                description = "Description $it",
                url = "",
                imageUrl = ""
            )
        },
        popular = (1..10).map {
            Video(
                id = "p$it",
                title = "Title $it",
                subtitle = "Subtitle $it",
                description = "Description $it",
                url = "",
                imageUrl = ""
            )
        },
        categories = listOf(
            HomeCategoryItems(
                category = HomeCategory(id = 1, name = "Category 1"),
                items = (1..10).map {
                    Video(
                        id = "c1-$it",
                        title = "Title $it",
                        subtitle = "Subtitle $it",
                        description = "Description $it",
                        url = "",
                        imageUrl = ""
                    )
                }
            )
        )
    )
    val home = Home(
        layout = listOf(
            HomeLayoutSection(type = HomeSectionType.BANNER_MAIN),
            HomeLayoutSection(type = HomeSectionType.TOP_10, title = "Top 10"),
            HomeLayoutSection(type = HomeSectionType.POPULAR, title = "Popular"),
            HomeLayoutSection(type = HomeSectionType.CATEGORY)
        ),
        data = homeData
    )

    DLearnTheme {
        HomeListScreen(
            uiState = HomeUiState.Success(home),
            onTabSelected = {},
            onItemClick = {}
        )
    }
}

@Preview
@Composable
fun SectionBannerMainPreview() {
    val video = Video(
        id = "1",
        title = "Title",
        subtitle = "Subtitle",
        description = "Description",
        url = "",
        imageUrl = ""
    )
    val data = HomeDataContent(
        bannerMain = video,
        top10 = emptyList(),
        popular = emptyList(),
        categories = emptyList()
    )
    DLearnTheme {
        Section(
            section = HomeLayoutSection(type = HomeSectionType.BANNER_MAIN),
            data = data,
            onItemClick = {}
        )
    }
}

@Preview
@Composable
fun SectionTop10Preview() {
    val data = HomeDataContent(
        bannerMain = null,
        top10 = (1..10).map {
            Video(
                id = "$it",
                title = "Title $it",
                subtitle = "Subtitle $it",
                description = "Description $it",
                url = "",
                imageUrl = ""
            )
        },
        popular = emptyList(),
        categories = emptyList()
    )
    DLearnTheme {
        Section(
            section = HomeLayoutSection(type = HomeSectionType.TOP_10, title = "Top 10"),
            data = data,
            onItemClick = {}
        )
    }
}
