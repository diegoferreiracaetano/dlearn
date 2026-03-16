package com.diegoferreiracaetano.dlearn.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import org.koin.compose.koinInject

@Composable
fun SearchContent(
    onSearch: (String) -> Unit,
    searchQuery: String,
    modifier: Modifier = Modifier,
    viewModel: SearchContentViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(searchQuery) {
        viewModel.fetchSearch(searchQuery)
    }

    val actions = remember(onSearch, searchQuery, viewModel) {
        ComponentActions(
            onSearch = onSearch,
            searchQuery = searchQuery
        )
    }

    uiState.Render(
        actions = actions,
        modifier = modifier
    )
}
