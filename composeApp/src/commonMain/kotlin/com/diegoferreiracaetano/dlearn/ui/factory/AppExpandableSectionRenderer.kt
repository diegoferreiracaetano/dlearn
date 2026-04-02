package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.list.AppSectionTitle
import com.diegoferreiracaetano.dlearn.designsystem.components.text.AppExpandableText
import com.diegoferreiracaetano.dlearn.ui.sdui.AppExpandableSectionComponent

class AppExpandableSectionRenderer {
    @Composable
    fun Render(
        component: AppExpandableSectionComponent,
        modifier: Modifier = Modifier,
    ) {
        Column(
            modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        ) {
            AppSectionTitle(
                title = component.title,
            )

            AppExpandableText(
                text = component.text,
                maxLines = component.maxLines,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
