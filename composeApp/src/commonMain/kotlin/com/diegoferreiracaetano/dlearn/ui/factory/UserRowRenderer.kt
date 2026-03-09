package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.components.profile.AppUserRow
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.UserRowComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class UserRowRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val userRow = component as? UserRowComponent ?: return
        
        AppUserRow(
            name = userRow.name,
            role = userRow.role,
            imageSource = userRow.imageUrl?.let { AppImageSource.Url(it) },
            onClick = userRow.actionUrl?.let { url -> { actions.onItemClick(url) } },
            modifier = modifier
        )
    }
}
