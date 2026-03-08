package com.diegoferreiracaetano.dlearn.domain.profile

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfile(language: String? = null): Flow<Screen>
}
