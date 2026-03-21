package com.diegoferreiracaetano.dlearn.navigation

class NavigationManager {
    private var onNavigate: ((String) -> Unit)? = null

    /**
     * Registra o handler de navegação (NavHostController).
     */
    fun registerNavigator(navigator: (String) -> Unit) {
        onNavigate = navigator
    }

    /**
     * Remove o handler para evitar leaks.
     */
    fun unregisterNavigator() {
        onNavigate = null
    }

    /**
     * Executa a navegação direta.
     */
    fun navigateTo(route: String) {
        onNavigate?.invoke(route)
    }
}
