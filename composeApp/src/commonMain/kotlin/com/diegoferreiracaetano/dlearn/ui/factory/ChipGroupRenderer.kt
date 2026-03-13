package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.HomeFilterIds
import com.diegoferreiracaetano.dlearn.designsystem.components.chip.AppChipGroup
import com.diegoferreiracaetano.dlearn.designsystem.components.chip.AppChipItem
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
            modifier = modifier,
            items = chipGroup.items.map { item ->
                AppChipItem(
                    label = item.label,
                    onClick = {
                        if (!item.hasDropDown) {
                            actions.onFilterTypeChanged(item.id)
                        }
                    },
                    hasDropDown = item.hasDropDown,
                    isFilter = item.isFilter,
                    isSelected = item.isSelected,
                    dropDownOptions = item.options?.map { it.label } ?: emptyList(),
                    onOptionSelected = { optionLabel ->
                        val selectedOption = item.options?.find { it.label == optionLabel }
                        if (selectedOption != null) {
                            actions.onCategoryChanged(selectedOption.id, optionLabel)
                        }
                    }
                )
            },
            onFilterChanged = { label ->
                if (label == null) {
                    actions.onFilterTypeChanged(null)
                    actions.onCategoryChanged(null, null)
                }
            }
        )
    }
}
