package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.list.AppTextRow
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
        
        AppTextRow(
            label = selectionRow.title,
            value = selectionRow.subtitle,
            leadingIcon = if (selectionRow.isSelected) Icons.Default.Check else null,
            onClick = {
                actions.onAction("${selectionRow.preferenceKey}:${selectionRow.value}")
            },
            modifier = modifier
        )
    }
}
