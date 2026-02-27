package com.diegoferreiracaetano.dlearn.ui.components.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.ui.components.alert.AppSnackbarHost
import com.diegoferreiracaetano.dlearn.ui.components.chip.AppChip
import com.diegoferreiracaetano.dlearn.ui.components.chip.AppChipGroup
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import kotlinx.coroutines.flow.distinctUntilChanged
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun AppScaffoldContent(
    modifier: Modifier = Modifier,
    topBar: AppTopBar? = null,
    collapsibleContent: @Composable (() -> Unit)? = null,
    bottomBar: AppBottomNavigation? = null,
    snackBarHostState: SnackbarHostState,
    scrollBehavior: TopAppBarScrollBehavior,
    content: LazyListScope.() -> Unit
) {
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { AppSnackbarHost(hostState = snackBarHostState) },
        topBar = {
            topBar?.let {
                AppTopBarFactory(
                    config = it,
                    scrollBehavior = scrollBehavior
                )
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
        val listState = rememberLazyListState()
        var collapsibleContentVisible by remember { mutableStateOf(true) }

        LaunchedEffect(listState) {
            var previousState = listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset

            snapshotFlow { listState.firstVisibleItemIndex to listState.firstVisibleItemScrollOffset }
                .distinctUntilChanged()
                .collect { currentState ->
                    val (previousIndex, previousOffset) = previousState
                    val (currentIndex, currentOffset) = currentState

                    val isScrollingDown = if (previousIndex != currentIndex) {
                        currentIndex > previousIndex
                    } else {
                        currentOffset > previousOffset
                    }

                    collapsibleContentVisible = !(isScrollingDown && (currentIndex > 0 || currentOffset > 0))

                    @Suppress("UNUSED_ASSIGNMENT")
                    previousState = currentState
                }
        }

        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            LazyColumn(
                state = listState,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .systemBarsPadding()
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                collapsibleContent?.let {
                    stickyHeader {
                        AnimatedVisibility(
                            visible = collapsibleContentVisible,
                            enter = slideInVertically(initialOffsetY = { -it }),
                            exit = slideOutVertically(targetOffsetY = { -it }),
                        ) {
                            Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                                it()
                            }
                        }
                    }
                }
                content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContainer(
    modifier: Modifier = Modifier,
    topBar: AppTopBar? = null,
    collapsibleContent: @Composable (() -> Unit)? = null,
    bottomBar: AppBottomNavigation? = null,
    snackBarHostState: SnackbarHostState = remember { SnackbarHostState() },
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState()),
    content: LazyListScope.() -> Unit
) {
    AppScaffoldContent(
        modifier = modifier,
        topBar = topBar,
        collapsibleContent = collapsibleContent,
        bottomBar = bottomBar,
        snackBarHostState = snackBarHostState,
        scrollBehavior = scrollBehavior,
        content = content
    )
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
            collapsibleContent = {
                AppChipGroup(
                    items = listOf(
                        AppChip(label = "Séries"),
                        AppChip(label = "Filmes"),
                        AppChip(label = "Categorias", hasDropDown = true, isFilter = false)
                    ),
                    onFilterChanged = {}
                )
            }
        ) {
            items(100) { index ->
                Text(
                    text = "Item $index",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}