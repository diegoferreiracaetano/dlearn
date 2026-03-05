package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.BannerCard
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.BannerCarousel
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.Carousel
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.FullScreenBanner
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.FullScreenVideo
import com.diegoferreiracaetano.dlearn.designsystem.components.chip.AppChip
import com.diegoferreiracaetano.dlearn.designsystem.components.chip.AppChipGroup
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppBottomNavigationBar
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppNavigationTab
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppTopBar
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavItem
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.CardComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.CarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipItem
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.CarouselItem as DsCarouselItem

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RenderComponent(
    component: Component,
    onItemClick: (String) -> Unit,
    onFilterTypeChanged: (String?) -> Unit,
    onSearchChanged: (String) -> Unit,
    onTabSelected: (String) -> Unit,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    collapsibleContentVisible: Boolean = true,
    modifier: Modifier = Modifier
) {
    when (component) {
        is AppContainerComponent -> {
            val listState = rememberLazyListState()
            val isVisible by remember { derivedStateOf { listState.firstVisibleItemIndex == 0 } }

            AppContainer(
                modifier = modifier,
                topBar = {
                    component.topBar?.let { topBar ->
                        AppTopBar(
                            title = topBar.title,
                            searchValue = searchText.ifEmpty { topBar.searchValue ?: "" },
                            onSearchValueChange = {
                                onSearchTextChange(it)
                                onSearchChanged(it)
                            }
                        )
                    }
                },
                bottomBar = {
                    component.bottomBar?.let { bottomBar ->
                        AppBottomNavigationBar(
                            items = bottomBar.items.map { it.toNavigationTab() },
                            selectedRoute = bottomBar.selectedRoute ?: "",
                            onTabSelected = onTabSelected
                        )
                    }
                }
            ) {
                LazyColumn(
                    state = listState,
                ) {
                    items(component.components) { inner ->
                        RenderComponent(
                            component = inner,
                            onItemClick = onItemClick,
                            onFilterTypeChanged = onFilterTypeChanged,
                            onSearchChanged = onSearchChanged,
                            onTabSelected = onTabSelected,
                            searchText = searchText,
                            onSearchTextChange = onSearchTextChange,
                            collapsibleContentVisible = isVisible
                        )
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }

        is ChipGroupComponent -> {
            AnimatedVisibility(
                visible = collapsibleContentVisible,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it }),
            ) {
                AppChipGroup(
                    items = component.items.map { it.toAppChip(onFilterTypeChanged) },
                    onFilterChanged = onFilterTypeChanged,
                    modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                )
            }
        }

        is FullScreenBannerComponent -> {
            FullScreenBanner(
                modifier = Modifier.fillMaxHeight(1f),
                pageCount = 1,
                pageContent = {
                    FullScreenVideo(
                        title = component.title,
                        subtitle = component.subtitle ?: "",
                        imageUrl = component.imageUrl,
                        onItemClick = { component.id.let { onItemClick(it) } },
                        onWatchClick = { component.id.let { onItemClick(it) } },
                        onAddToListClick = { /* TODO */ }
                    )
                }
            )
        }

        is CarouselComponent -> {
            Carousel(
                title = component.title,
                items = component.items.map { it.toDsCarouselItem(onItemClick) },
                modifier = Modifier.padding(top = 16.dp),
            )
        }

        is BannerCarouselComponent -> {
            BannerCarousel(
                title = component.title,
                pageCount = component.items.size,
                pageContent = { index ->
                    val item = component.items[index]
                    BannerCard(
                        title = item.title,
                        subtitle = item.subtitle ?: "",
                        imageUrl = item.imageUrl,
                        onClick = { item.actionUrl?.let { onItemClick(it) } }
                    )
                }
            )
        }

        is BannerComponent -> {
            BannerCard(
                title = component.title,
                subtitle = component.subtitle ?: "",
                imageUrl = component.imageUrl,
                onClick = { component.actionUrl?.let { onItemClick(it) } }
            )
        }

        is AppTopBarComponent -> {
            AppTopBar(
                title = component.title,
                searchValue = searchText.ifEmpty { component.searchValue ?: "" },
                onSearchValueChange = {
                    onSearchTextChange(it)
                    onSearchChanged(it)
                }
            )
        }

        is BottomNavigationComponent -> {
            AppBottomNavigationBar(
                items = component.items.map { it.toNavigationTab() },
                selectedRoute = component.selectedRoute ?: "",
                onTabSelected = onTabSelected
            )
        }

        else -> {}
    }
}

private fun BottomNavItem.toNavigationTab() = AppNavigationTab(
    label = label,
    route = route,
    selectedIcon = iconIdentifier.toIcon(),
    unselectedIcon = iconIdentifier.toIcon()
)

private fun ChipItem.toAppChip(onFilterTypeChanged: (String?) -> Unit) = AppChip(
    label = label,
    hasDropDown = hasDropDown,
    isFilter = isFilter,
    onClick = { onFilterTypeChanged(id) }
)

private fun CardComponent.toDsCarouselItem(onItemClick: (String) -> Unit) = DsCarouselItem(
    title = title,
    subtitle = subtitle ?: "",
    imageUrl = imageUrl,
    rank = rank,
    onClick = { actionUrl?.let { onItemClick(it) } }
)

private fun String?.toIcon(): ImageVector = when (this) {
    "home" -> Icons.Default.Home
    "search" -> Icons.Default.Search
    "favorite" -> Icons.Default.Favorite
    "person" -> Icons.Default.Person
    else -> Icons.Default.Home
}

@Composable
fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(message: String?) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = message ?: "An unknown error occurred")
    }
}
