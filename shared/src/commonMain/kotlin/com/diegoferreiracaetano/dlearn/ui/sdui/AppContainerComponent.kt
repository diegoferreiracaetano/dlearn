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

        if (!currentTopBar.showSearch || route == NavigationRoutes.HOME) {
            return currentTopBar
        }

        val currentNavItem = bottomBar?.items?.find { it.route == route }
        return currentNavItem?.let {
            currentTopBar.copy(
                title = it.label,
                subtitle = null,
                imageUrl = null
            )
        } ?: currentTopBar
    }
}
