package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.textfield.AppTextField
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTextFieldComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.toResource
import com.diegoferreiracaetano.dlearn.ui.util.toTextFieldType
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.*

class AppTextFieldRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val textField = component as? AppTextFieldComponent ?: return
        
        AppTextField(
            value = textField.value,
            onValueChange = { newValue ->
                // SDUI text updates usually need a key to identify the field in the state
                actions.onQueryChange(newValue) 
            },
            label = textField.label.toResource(),
            placeholder = textField.placeholder.toResource() ?: Res.string.app_name, // Fallback
            type = textField.fieldType.toTextFieldType(),
            modifier = modifier
        )
    }
}
