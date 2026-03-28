package com.diegoferreiracaetano.dlearn.ui.util

import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest

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
    val onSearch: (String) -> Unit = {},
    val onQueryChange: (String) -> Unit = {},
    val execute: (AppRequest) -> Unit = {}
)
