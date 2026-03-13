package com.diegoferreiracaetano.dlearn.ui.util

data class ComponentActions(
    val onItemClick: (String) -> Unit = {},
    val onFilterTypeChanged: (String?) -> Unit = {},
    val onCategoryChanged: (String?, String?) -> Unit = { _, _ -> },
    val onSearchChanged: (String) -> Unit = {},
    val onTabSelected: (String) -> Unit = {},
    val onBackClick: () -> Unit = {},
    val searchText: String = "",
    val onSearchTextChange: (String) -> Unit = {}
)
