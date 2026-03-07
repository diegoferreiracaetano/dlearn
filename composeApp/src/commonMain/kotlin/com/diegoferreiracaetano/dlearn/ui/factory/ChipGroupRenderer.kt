package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.chip.AppChip
import com.diegoferreiracaetano.dlearn.designsystem.components.chip.AppChipGroup
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class ChipGroupRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val chipGroup = component as? ChipGroupComponent ?: return
        AppChipGroup(
            items = chipGroup.items.map { item ->
                AppChip(
                    label = item.label,
                    onClick = { actions.onFilterTypeChanged(item.id) },
                    hasDropDown = item.hasDropDown,
                    isFilter = item.isFilter
                )
            },
            onFilterChanged = actions.onFilterTypeChanged
        )
    }
}
