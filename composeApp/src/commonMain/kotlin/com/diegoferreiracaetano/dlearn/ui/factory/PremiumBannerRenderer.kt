package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.banner.AppBanner
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.PremiumBannerComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class PremiumBannerRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val banner = component as? PremiumBannerComponent ?: return
        AppBanner(
            title = banner.title,
            description = banner.description,
            icon = when (banner.iconIdentifier) {
                "premium" -> Icons.Default.WorkspacePremium
                else -> null
            },
            modifier = modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clickable { banner.actionUrl?.let { actions.onItemClick(it) } }
        )
    }
}
