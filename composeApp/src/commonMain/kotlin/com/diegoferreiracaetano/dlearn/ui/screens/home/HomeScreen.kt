package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.BannerCard
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.BannerCarousel
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.Carousel
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.CarouselItem
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.FullScreenBanner
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.FullScreenVideo
import com.diegoferreiracaetano.dlearn.designsystem.components.chip.AppChip
import com.diegoferreiracaetano.dlearn.designsystem.components.chip.AppChipGroup
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppBottomNavigation
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppBottomNavigationBar
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppTopBar
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.domain.home.Home
import com.diegoferreiracaetano.dlearn.domain.home.HomeDataContent
import com.diegoferreiracaetano.dlearn.domain.home.HomeSectionType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.ui.screens.home.state.HomeUiState
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
        onFilterTypeChanged = viewModel::onFilterTypeChanged,
        onSearchChanged = viewModel::onSearchChanged,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeListScreen(
    uiState: HomeUiState,
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onFilterTypeChanged: (String?) -> Unit,
    onSearchChanged: (String?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    var collapsibleContentVisible by remember { mutableStateOf(true) }

    AppContainer(
        modifier = modifier,
        topBar = {
            AppTopBar(
                title = "HOME",
                searchValue = searchText,
                onSearchValueChange = {
                    searchText = it
                    onSearchChanged(it)
                }
            )
        },
        bottomBar = {
            val nav = AppBottomNavigation(onTabSelected = onTabSelected)
            AppBottomNavigationBar(
                items = nav.items,
                selectedRoute = nav.selectedRoute,
                onTabSelected = nav.onTabSelected
            )
        }
    ) {

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                state = listState
            ) {

                stickyHeader {
                    AnimatedVisibility(
                        visible = collapsibleContentVisible,
                        enter = slideInVertically(initialOffsetY = { -it }),
                        exit = slideOutVertically(targetOffsetY = { -it }),
                    ) {
                        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                            AppChipGroup(
                                items = listOf(
                                    AppChip(label = "Séries"),
                                    AppChip(label = "Filmes"),
                                    AppChip(label = "Categorias", hasDropDown = true, isFilter = false)
                                ),
                                onFilterChanged = onFilterTypeChanged
                            )
                        }
                    }
                }

                when (uiState) {
                    is HomeUiState.Success -> {
                        val homeData = uiState.home.data
                        items(uiState.home.sections) { sectionType ->
                            Section(sectionType = sectionType, data = homeData, onItemClick = onItemClick)
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
    }
}

@Composable
private fun Section(
    sectionType: HomeSectionType,
    data: HomeDataContent,
    onItemClick: (String) -> Unit
) {
    val sectionItems = data.items.filter { it.section == sectionType }

    when (sectionType) {
        HomeSectionType.BANNER_MAIN -> {
            sectionItems.firstOrNull()?.let { video ->
                FullScreenBanner(
                    pageCount = 1,
                    pageContent = {
                        FullScreenVideo(
                            title = video.title,
                            subtitle = video.subtitle,
                            imageUrl = video.imageUrl,
                            onItemClick = { onItemClick(video.id) },
                            onWatchClick = { println("Watch ${video.title}") },
                            onAddToListClick = { println("Add to List ${video.title}") }
                        )
                    }
                )
            }
        }

        HomeSectionType.TOP_10 -> {
            if (sectionItems.isNotEmpty()) {
                Carousel(
                    title = "Top 10",
                    items = sectionItems.take(10).mapIndexed { index, video ->
                        CarouselItem(
                            title = video.title,
                            subtitle = video.subtitle,
                            imageUrl = video.imageUrl,
                            rank = index + 1,
                            onClick = { onItemClick(video.id) }
                        )
                    },
                    modifier = Modifier.padding(top = 16.dp),
                )
            }
        }

        HomeSectionType.POPULAR -> {
            if (sectionItems.isNotEmpty()) {
                BannerCarousel(
                    title = "Populares",
                    pageCount = sectionItems.size,
                    pageContent = { index ->
                        val video = sectionItems[index]
                        BannerCard(
                            title = video.title,
                            subtitle = video.subtitle,
                            imageUrl = video.imageUrl,
                            onClick = { onItemClick(video.id) }
                        )
                    }
                )
            }
        }

        HomeSectionType.CATEGORY -> {
            val categories = sectionItems.mapNotNull { it.category }.distinctBy { it.id }
            categories.forEach { category ->
                val categoryVideos = sectionItems.filter { it.category?.id == category.id }
                Carousel(
                    title = category.name,
                    items = categoryVideos.map { video ->
                        CarouselItem(
                            title = video.title,
                            subtitle = video.subtitle,
                            imageUrl = video.imageUrl,
                            onClick = { onItemClick(video.id) }
                        )
                    },
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
    val homeData = HomeDataContent(
        items = listOf(
            Video(id = "1", title = "Banner", subtitle = "2024", description = "", url = "", imageUrl = "", section = HomeSectionType.BANNER_MAIN),
            Video(id = "2", title = "Top 1", subtitle = "2024", description = "", url = "", imageUrl = "", section = HomeSectionType.TOP_10),
            Video(id = "3", title = "Popular", subtitle = "2024", description = "", url = "", imageUrl = "", section = HomeSectionType.POPULAR)
        )
    )
    val home = Home(
        sections = listOf(
            HomeSectionType.BANNER_MAIN,
            HomeSectionType.TOP_10,
            HomeSectionType.POPULAR
        ),
        data = homeData
    )

    DLearnTheme {
        HomeListScreen(
            uiState = HomeUiState.Success(home),
            onTabSelected = {},
            onItemClick = {},
            onFilterTypeChanged = {},
            onSearchChanged = {}
        )
    }
}
