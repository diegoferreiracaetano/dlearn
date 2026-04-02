package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.list.AppTextRow
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSwitchRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.toIcon

class AppSwitchRowRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        val switchRow = component as? AppSwitchRowComponent ?: return

        AppTextRow(
            label = switchRow.title,
            value = switchRow.subtitle,
            leadingIcon = switchRow.icon.toIcon(),
            isEnabled = switchRow.isChecked,
            onCheckedChange = { isChecked ->
                actions.onSelectChanged(switchRow.preferenceKey, isChecked.toString())
            },
            modifier = modifier,
        )
    }
}
