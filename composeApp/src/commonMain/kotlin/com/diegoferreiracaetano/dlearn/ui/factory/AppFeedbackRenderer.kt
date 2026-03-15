package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.feedback.AppFeedback
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.ui.sdui.AppFeedbackComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppFeedbackRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val feedbackComponent = component as? AppFeedbackComponent ?: return

        AppFeedback(
            title = feedbackComponent.title,
            description = feedbackComponent.description,
            imageSource = feedbackComponent.imageSource?.let { AppImageSource.Url(it) },
            primaryText = feedbackComponent.primaryText,
            onPrimary = actions.onRetry,
            secondaryText = feedbackComponent.secondaryText,
            onSecondary = actions.onClose,
            modifier = modifier
        )
    }
}
