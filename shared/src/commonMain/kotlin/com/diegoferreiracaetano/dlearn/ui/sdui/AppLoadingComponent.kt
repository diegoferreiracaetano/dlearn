package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data object AppLoadingComponent : Component {
    override val isFullScreen: Boolean get() = true
}
