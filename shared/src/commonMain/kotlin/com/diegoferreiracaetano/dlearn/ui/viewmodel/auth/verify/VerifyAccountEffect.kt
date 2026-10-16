package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify

sealed interface VerifyAccountEffect {
    data object Verified : VerifyAccountEffect
}
