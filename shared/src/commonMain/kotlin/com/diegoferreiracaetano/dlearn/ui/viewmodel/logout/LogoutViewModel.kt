package com.diegoferreiracaetano.dlearn.ui.viewmodel.logout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import kotlinx.coroutines.launch

class LogoutViewModel(
    private val sessionManager: SessionManager
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            sessionManager.logout()
        }
    }
}