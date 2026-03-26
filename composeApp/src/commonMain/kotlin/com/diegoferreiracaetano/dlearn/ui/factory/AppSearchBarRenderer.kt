package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.diegoferreiracaetano.dlearn.designsystem.components.search.AppSearchBar
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
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
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        AppSearchBar(
            query = actions.searchQuery,
            onQueryChange = actions.onQueryChange,
            onSearch = actions.onSearch,
            onBackClick = actions.onBackClick,
            placeholder = searchComponent.placeholder,
            modifier = modifier.focusRequester(focusRequester),
            content = {
                RenderComponents(
                    components = searchComponent.components,
                    actions = actions
                )
            }
        )
    }
}
