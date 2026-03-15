package com.diegoferreiracaetano.dlearn.ui.sdui

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import kotlinx.serialization.Serializable

@Serializable
data class AppContainerComponent(
    val topBar: AppTopBarComponent? = null,
    val searchBar: Component? = null,
    val chipGroup: ChipGroupComponent? = null,
    val bottomBar: BottomNavigationComponent? = null,
    val components: List<Component> = emptyList()
) : Component {

    fun getTopBarForRoute(route: String): AppTopBarComponent? {
        val currentTopBar = topBar ?: return null
        
        // Se a busca estiver desativada, provavelmente é uma tela de detalhe ou específica.
        // Nesse caso, respeitamos o título que o servidor enviou.
        if (!currentTopBar.showSearch) {
            return currentTopBar
        }

        // Se estivermos na Home, mantemos a topBar original (com perfil, subtítulo, etc)
        if (route == NavigationRoutes.HOME) {
            return currentTopBar
        }

        // Para outras rotas da Main (News, Favorites, Profile), buscamos o rótulo na bottomBar
        val currentNavItem = bottomBar?.items?.find { it.route == route }
        return if (currentNavItem != null) {
            currentTopBar.copy(
                title = currentNavItem.label,
                subtitle = null,
                imageUrl = null
            )
        } else {
            currentTopBar
        }
    }
}
