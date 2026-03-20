package com.diegoferreiracaetano.dlearn.ui.util

data class ComponentActions(
    val onItemClick: (String) -> Unit = {},
    val onFilterTypeChanged: (String?) -> Unit = {},
    val onCategoryChanged: (String?, String?) -> Unit = { _, _ -> },
    val onSearchClick: (() -> Unit)? = null,
    val onTabSelected: (String) -> Unit = {},
    val onBackClick: () -> Unit = {},
    val onClose: () -> Unit = {},
    val onRetry: () -> Unit = {},
    val onAction: (String) -> Unit = {},
    val currentRoute: String = "",
    val onSearch: (String) -> Unit = {},
    val onQueryChange: (String) -> Unit = {},
    val searchQuery: String = ""
)
