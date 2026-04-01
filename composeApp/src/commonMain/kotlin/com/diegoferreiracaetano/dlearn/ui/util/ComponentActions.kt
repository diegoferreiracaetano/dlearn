package com.diegoferreiracaetano.dlearn.ui.util

data class ComponentActions(
    val onItemClick: (String) -> Unit = {},
    val onMovieClick: (String) -> Unit = {},
    val onSelectChanged: (String?, String?) -> Unit = { _, _ -> },
    val onSearchClick: (() -> Unit)? = null,
    val onTabSelected: (String) -> Unit = {},
    val onBackClick: () -> Unit = {},
    val onClose: () -> Unit = {},
    val onRetry: () -> Unit = {},
    val currentRoute: String = "",
    val onAction: (String) -> Unit = {},
    val onQueryChange: (String) -> Unit = {}
)
