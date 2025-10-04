package com.diegoferreiracaetano.dlearn.ui.components.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.theme.Shapes
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.google
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class ButtonType {
    PRIMARY,
    SECONDARY,
    TERTIARY,
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    type: ButtonType = ButtonType.PRIMARY,
    image: DrawableResource? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    when (type) {
        ButtonType.PRIMARY -> AppButonPrimary(text, onClick, image, modifier, enabled)
        ButtonType.SECONDARY -> AppButonSecondary(text, onClick, image, modifier, enabled)
        ButtonType.TERTIARY -> AppButonTertiary(text, onClick, image, modifier, enabled)
    }
}

@Composable
fun AppButonPrimary(
    text: String,
    onClick: () -> Unit,
    image: DrawableResource? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        shape = Shapes.extraLarge,
        enabled = enabled,
        colors =
            ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        modifier =
            modifier
                .fillMaxWidth()
                .height(56.dp),
    ) {
        AppButtonContent(text, image)
    }
}

@Composable
fun AppButonSecondary(
    text: String,
    onClick: () -> Unit,
    image: DrawableResource? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        shape = Shapes.extraLarge,
        enabled = enabled,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary,
            ),
        modifier =
            modifier
                .fillMaxWidth()
                .height(56.dp),
    ) {
        AppButtonContent(text, image)
    }
}

@Composable
fun AppButonTertiary(
    text: String,
    onClick: () -> Unit,
    image: DrawableResource? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    OutlinedButton(
        onClick = onClick,
        shape = Shapes.extraLarge,
        enabled = enabled,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.surface),
        colors =
            ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.onSurface,
                contentColor = MaterialTheme.colorScheme.surface,
            ),
        modifier =
            modifier
                .fillMaxWidth()
                .height(56.dp),
    ) {
        AppButtonContent(text, image)
    }
}

@Composable
fun AppButtonContent(
    text: String,
    image: DrawableResource? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (image != null) {
            Image(
                painterResource(image),
                contentDescription = text,
                modifier = Modifier.size(28.dp),
            )

            Spacer(modifier = Modifier.width(16.dp))
        }

        Text(
            text = text,
            style = typography.labelLarge,
        )
    }
}

@Composable
fun ElementButton(
    text: String,
    background: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = Shapes.large
    val height = 56.dp

    Button(
        onClick = onClick,
        shape = shape,
        elevation =
            ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp,
                disabledElevation = 0.dp,
            ),
        colors =
            ButtonDefaults.buttonColors(
                containerColor = background,
                contentColor = textColor,
            ),
        modifier =
            modifier
                .fillMaxWidth()
                .height(height),
    ) {
        Text(
            text = text,
            style = typography.labelLarge,
        )
    }
}

@Preview
@Composable
fun AppButtonPrimaryPreview() {
    DLearnTheme {
        Column(
            modifier =
                Modifier
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AppButton(
                text = "Primary",
                onClick = {},
            )

            AppButton(
                text = "Primary disable",
                onClick = {},
                enabled = false,
            )

            AppButton(
                text = "Secondary",
                onClick = {},
                type = ButtonType.SECONDARY,
            )

            AppButton(
                text = "Secondary disable",
                onClick = {},
                enabled = false,
                type = ButtonType.SECONDARY,
            )

            AppButton(
                text = "Tertiary",
                onClick = {},
                type = ButtonType.TERTIARY,
                image = Res.drawable.google,
            )
        }
    }
}
