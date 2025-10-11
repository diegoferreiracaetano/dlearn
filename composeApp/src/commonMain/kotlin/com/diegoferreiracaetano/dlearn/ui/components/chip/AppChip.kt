package com.diegoferreiracaetano.dlearn.ui.components.chip

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.ui.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.theme.Shapes
import org.jetbrains.compose.ui.tooling.preview.Preview

data class AppChip(
    val label: String,
    val onClick: () -> Unit = {},
    val hasDropDown: Boolean = false,
    val isFilter: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppChipGroup(
    items: List<AppChip>,
    modifier: Modifier = Modifier,
    onFilterChanged: (String?) -> Unit
) {
    var selectedFilterLabel by remember { mutableStateOf<String?>(null) }

    val visibleChips = if (selectedFilterLabel == null) {
        items
    } else {
        items.filter { it.label == selectedFilterLabel || !it.isFilter }
    }

    Row(
        modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (selectedFilterLabel != null) {
            IconButton(onClick = {
                selectedFilterLabel = null
                onFilterChanged(null)
            }) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Clear selection",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(visibleChips) { chip ->
                val isSelected = chip.label == selectedFilterLabel
                FilterChip(
                    shape = Shapes.extraLarge,
                    selected = isSelected,
                    onClick = {
                        if (chip.isFilter) {
                            val newFilter = if (isSelected) null else chip.label
                            selectedFilterLabel = newFilter
                            onFilterChanged(newFilter)
                        }
                        chip.onClick()
                    },
                    label = { Text(chip.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.Transparent,
                        selectedContainerColor = MaterialTheme.colorScheme.onSurface,
                        labelColor = MaterialTheme.colorScheme.onSurface,
                        selectedLabelColor = MaterialTheme.colorScheme.surface,
                        iconColor = MaterialTheme.colorScheme.onSurface,
                        selectedTrailingIconColor = MaterialTheme.colorScheme.surface
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        selectedBorderColor = Color.Transparent,
                        enabled = true,
                        selected = isSelected
                    ),
                    trailingIcon = if (chip.hasDropDown) {
                        {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null
                            )
                        }
                    } else null
                )
            }
        }
    }
}

@Preview
@Composable
fun AppChipGroupPreview() {
    DLearnTheme(darkTheme = true) {
        AppChipGroup(
            items = listOf(
                AppChip(label = "Séries"),
                AppChip(label = "Filmes"),
                AppChip(label = "Categorias", hasDropDown = true, isFilter = false)
            ),
            onFilterChanged = {}
        )
    }
}