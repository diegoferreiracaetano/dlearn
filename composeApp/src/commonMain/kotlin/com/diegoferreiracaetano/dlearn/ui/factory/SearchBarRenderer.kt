package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.search.AppSearchBar
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.SearchBarComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class SearchBarRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val searchBar = component as? SearchBarComponent ?: return

        AppSearchBar(
            modifier = modifier,
            query = actions.searchText,
            onQueryChange = actions.onSearchTextChange,
            onSearch = actions.onSearchChanged,
            active = searchBar.active,
            onActiveChange = { /* Handle active change if needed in actions */ },
            placeholder = searchBar.placeholder.orEmpty()
        )
    }
}
