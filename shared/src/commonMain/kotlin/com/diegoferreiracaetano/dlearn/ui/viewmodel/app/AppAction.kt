package com.diegoferreiracaetano.dlearn.ui.viewmodel.app

sealed interface AppAction {
    /** Abre a tela de [path]; mostra `Loading`. */
    data class Load(
        val path: String,
        val params: Map<String, String>? = null,
    ) : AppAction

    /** Ação disparada por um componente (URL do backend); mantém o conteúdo na tela. */
    data class Open(
        val url: String,
    ) : AppAction

    data object Retry : AppAction
}
