package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.chip.AppChipGroup
import com.diegoferreiracaetano.dlearn.designsystem.components.chip.AppChipItem
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipItem
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.jetbrains.compose.ui.tooling.preview.Preview

class ChipGroupRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        val chipGroup = component as? ChipGroupComponent ?: return

        AppChipGroup(
            modifier = modifier,
            items =
            chipGroup.items.map { item ->
                AppChipItem(
                    label = item.label,
                    onClick = {
                        if (!item.hasDropDown) {
                            actions.onAction(item.actionUrl)
                        }
                    },
                    hasDropDown = item.hasDropDown,
                    isFilter = item.isFilter,
                    isSelected = item.isSelected,
                    dropDownOptions = item.options?.map { it.label } ?: emptyList(),
                    onOptionSelected = { label ->
                        val selectedOption = item.options?.find { it.label == label }
                        selectedOption?.actionUrl?.let { url ->
                            actions.onAction(url)
                        }
                    },
                )
            },
            onFilterChanged = { label ->
                if (label == null) {
                    actions.onAction(chipGroup.cleanUrl)
                }
            },
        )
    }
}

@Preview
@Composable
fun ChipGroupRendererPreview() {
    DLearnTheme {
        ChipGroupRenderer().Render(
            component = ChipGroupComponent(
                items = listOf(
                    ChipItem(id = "1", label = "Season 1", actionUrl = "url1", isSelected = true),
                    ChipItem(id = "2", label = "Season 2", actionUrl = "url2")
                ),
                cleanUrl = "clean"
            ),
            actions = ComponentActions(),
            modifier = Modifier
        )
    }
}
