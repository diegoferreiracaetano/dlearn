package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.search.AppSearchBar
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSearchBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppSearchBarRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val searchComponent = component as? AppSearchBarComponent ?: return

        AppSearchBar(
            query = actions.searchQuery,
            onQueryChange = actions.onQueryChange,
            onSearch = actions.onSearch,
            onBackClick = actions.onBackClick,
            placeholder = searchComponent.placeholder,
            modifier = modifier,
            content = {
                RenderComponentFactory.Render(
                    components = searchComponent.components,
                    actions = actions
                )
            }
        )
    }
}
