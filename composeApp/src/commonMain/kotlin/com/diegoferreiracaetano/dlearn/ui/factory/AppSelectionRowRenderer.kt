package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.list.AppSelectableRow
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSelectionRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppSelectionRowRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val selectionRow = component as? AppSelectionRowComponent ?: return

        AppSelectableRow(
            label = selectionRow.title,
            isSelected = selectionRow.isSelected,
            onClick = {
                println("DEBUG: AppSelectionRowRenderer - onClick ${selectionRow.toString()} ")
                actions.onSelectChanged(selectionRow.preferenceKey, selectionRow.value)
            },
            modifier = modifier
        )
    }
}
