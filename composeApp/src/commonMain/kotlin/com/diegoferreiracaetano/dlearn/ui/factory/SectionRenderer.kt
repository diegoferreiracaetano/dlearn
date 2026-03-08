package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.list.AppSectionTitle
import com.diegoferreiracaetano.dlearn.designsystem.components.list.AppTextRow
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.SectionComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.toIcon

class SectionRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val section = component as? SectionComponent ?: return
        
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            AppSectionTitle(
                title = section.title,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            section.items.forEach { item ->
                AppTextRow(
                    label = item.label,
                    value = item.value,
                    leadingIcon = item.icon.toIcon(),
                    onClick = { item.actionUrl?.let { actions.onItemClick(it) } }
                )
            }
        }
    }
}
