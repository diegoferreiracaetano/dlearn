package com.diegoferreiracaetano.dlearn.ui.screens.profile

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.profile.ProfileRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchProfile()
    }

    fun retry() {
        fetchProfile()
    }

    fun fetchProfile() {
        val language = Locale.current.language
        _uiState.value = UIState.Loading
        viewModelScope.launch {
            repository.getProfile(language)
                .catch { e ->
                    _uiState.value = UIState.Error(e)
                }
                .collect { profile ->
                    _uiState.value = UIState.Success(profile)
                }
        }
    }
}
