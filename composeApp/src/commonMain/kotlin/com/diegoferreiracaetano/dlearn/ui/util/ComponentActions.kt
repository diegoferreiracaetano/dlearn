package com.diegoferreiracaetano.dlearn.ui.util

import com.diegoferreiracaetano.dlearn.ui.sdui.AppAction

data class ComponentActions(
    val onItemClick: (String) -> Unit = {},
    val onFilterTypeChanged: (String?) -> Unit = {},
    val onCategoryChanged: (String?, String?) -> Unit = { _, _ -> },
    val onSearchClick: (() -> Unit)? = null,
    val onTabSelected: (String) -> Unit = {},
    val onBackClick: () -> Unit = {},
    val onClose: () -> Unit = {},
    val onRetry: () -> Unit = {},
    val onAction: (AppAction) -> Unit = {},
    val currentRoute: String = "",
    val onSearch: (String) -> Unit = {},
    val onQueryChange: (String) -> Unit = {},
    val searchQuery: String = ""
)
