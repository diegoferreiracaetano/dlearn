package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.ui.screens.search.SearchContent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSearchContentComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppSearchContentRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        if (component !is AppSearchContentComponent) return

        SearchContent(
            onSearch = actions.onSearch,
            searchQuery = actions.searchQuery,
            modifier = modifier
        )
    }
}
