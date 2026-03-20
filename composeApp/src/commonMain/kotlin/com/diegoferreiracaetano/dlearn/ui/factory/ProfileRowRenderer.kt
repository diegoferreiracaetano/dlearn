package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.components.profile.AppProfileRow
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.ProfileRowComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class ProfileRowRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val profileRow = component as? ProfileRowComponent ?: return
        AppProfileRow(
            name = profileRow.name,
            email = profileRow.email,
            imageSource = profileRow.imageUrl?.let { AppImageSource.Url(it) },
            onEditClick = { profileRow.editActionUrl?.let { actions.onItemClick(it) } },
            modifier = modifier
        )
    }
}
