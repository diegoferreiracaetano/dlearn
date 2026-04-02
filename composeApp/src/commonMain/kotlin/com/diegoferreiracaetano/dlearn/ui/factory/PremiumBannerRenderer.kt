package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.banner.AppBanner
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.PremiumBannerComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.toIcon

class PremiumBannerRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        val banner = component as? PremiumBannerComponent ?: return
        AppBanner(
            title = banner.title,
            description = banner.description,
            icon = banner.icon.toIcon(),
            modifier = modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}
