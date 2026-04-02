package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.textfield.AppTextField
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTextFieldComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.toResource
import com.diegoferreiracaetano.dlearn.ui.util.toTextFieldType
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.app_name

class AppTextFieldRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        val textField = component as? AppTextFieldComponent ?: return
        var text by remember { mutableStateOf(textField.value) }

        AppTextField(
            value = text,
            onValueChange = { newValue ->
                text = newValue
                actions.onQueryChange(textField.key + ":" + newValue)
            },
            label = textField.label.toResource(),
            placeholder = textField.placeholder.toResource() ?: Res.string.app_name,
            supportingText = textField.supportingText.toResource(),
            isError = textField.isError,
            type = textField.fieldType.toTextFieldType(),
            modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
        )
    }
}
