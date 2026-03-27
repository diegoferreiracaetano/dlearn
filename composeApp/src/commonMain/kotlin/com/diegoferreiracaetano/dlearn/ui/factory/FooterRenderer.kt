package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.button.AppButton
import com.diegoferreiracaetano.dlearn.designsystem.components.button.ButtonType
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.FooterComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class FooterRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val footer = component as? FooterComponent ?: return

        AppButton(
            text = footer.label,
            onClick = {
                footer.closeUrl?.let { actions.onClose() }
                    ?: footer.actionUrl?.let { actions.onItemClick(it) }
            },
            type = ButtonType.PRIMARY,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 32.dp)
                .padding(16.dp)
        )
    }
}
