package com.diegoferreiracaetano.dlearn.ui.viewmodel.movie

sealed interface MovieDetailAction {
    /** Ação disparada por um componente (URL do backend); mantém o conteúdo na tela. */
    data class Open(
        val url: String,
    ) : MovieDetailAction

    data object Retry : MovieDetailAction
}
