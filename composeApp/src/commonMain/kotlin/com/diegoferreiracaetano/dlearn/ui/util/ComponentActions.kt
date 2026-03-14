package com.diegoferreiracaetano.dlearn.ui.util

data class ComponentActions(
    val onItemClick: (String) -> Unit = {},
    val onFilterTypeChanged: (String?) -> Unit = {},
    val onCategoryChanged: (String?, String?) -> Unit = { _, _ -> },
    val onSearchChanged: (String) -> Unit = {},
    val onTabSelected: (String) -> Unit = {},
    val onBackClick: () -> Unit = {},
    val onClose: () -> Unit = {},
    val onShowSearchChanged: (Boolean) -> Unit = {},
    val onRetry: () -> Unit = {},
    val currentRoute: String = "",
    val isSearchVisible: Boolean = false,
    val searchText: String = "",
    val onSearchTextChange: (String) -> Unit = {},
    val isLoading: Boolean = false,
    val error: Throwable? = null
)
