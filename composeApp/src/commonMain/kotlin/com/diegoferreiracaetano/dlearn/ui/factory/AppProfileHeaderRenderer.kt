package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.profile.AppProfileHeader
import com.diegoferreiracaetano.dlearn.ui.sdui.AppProfileHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.toImageSource

class AppProfileHeaderRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        val header = component as? AppProfileHeaderComponent ?: return

        AppProfileHeader(
            name = header.name,
            email = header.email,
            imageSource = header.imageUrl.toImageSource(),
            onImagePicked = { bytes ->
                header.onImagePickedAction?.let { actions.onItemClick(it) }
            },
            modifier = modifier,
        )
    }
}
