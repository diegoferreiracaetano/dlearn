package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.HomeFilterIds
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.home.HomeRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.screens.home.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentType: HomeFilterType = HomeFilterType.ALL
    private var currentCategoryId: String? = null
    private var currentSearch: String? = null

    init {
        loadHome()
    }

    fun loadHome() {
        viewModelScope.launch {
            homeRepository.getHome(
                type = currentType,
                search = currentSearch,
                categoryId = currentCategoryId
            ).onStart {
                _uiState.update { HomeUiState.Loading }
            }.catch { error ->
                _uiState.update { HomeUiState.Error(error) }
            }.collect { screen ->
                _uiState.update { 
                    HomeUiState.Success(applySelectionToScreen(screen))
                }
            }
        }
    }

    private fun applySelectionToScreen(screen: Screen): Screen {
        val updatedComponents = screen.components.map { component ->
            if (component is ChipGroupComponent) {
                component.copy(
                    items = component.items.map { item ->
                        val isSelected = when (item.id) {
                            HomeFilterIds.SERIES -> currentType == HomeFilterType.SERIES && currentCategoryId == null
                            HomeFilterIds.MOVIES -> currentType == HomeFilterType.MOVIE && currentCategoryId == null
                            HomeFilterIds.CATEGORIES -> currentCategoryId != null
                            else -> item.id == currentCategoryId
                        }
                        item.copy(isSelected = isSelected)
                    }
                )
            } else {
                component
            }
        }
        return screen.copy(components = updatedComponents)
    }

    fun onFilterTypeChanged(filterId: String?) {
        val newType = when (filterId) {
            HomeFilterIds.SERIES -> HomeFilterType.SERIES
            HomeFilterIds.MOVIES -> HomeFilterType.MOVIE
            else -> HomeFilterType.ALL
        }

        if (currentType != newType || currentCategoryId != null) {
            currentType = newType
            currentCategoryId = null
            loadHome()
        }
    }

    fun onCategoryChanged(categoryId: String?, categoryName: String?) {
        if (currentCategoryId != categoryId) {
            currentCategoryId = categoryId
            loadHome()
        }
    }

    fun onSearchChanged(query: String) {
        if (currentSearch != query) {
            currentSearch = query.ifBlank { null }
            loadHome()
        }
    }

    fun retry() {
        loadHome()
    }
}
