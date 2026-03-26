package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.ui.sdui.*

class UserListScreenBuilder {
    fun build(users: List<User>): Screen {
        val components = mutableListOf<Component>()

        components.add(
            AppTopBarComponent(
                title = "Users"
            )
        )

        val userItems = users.map { user ->
            UserRowComponent(
                name = user.name,
                role = if (user.isPremium) "Premium Member" else "Free Member",
                imageUrl = user.imageUrl,
                actionUrl = AppPath(AppNavigationRoute.PROFILE, mapOf(AppQueryParam.USER_ID to user.id))
            )
        }

        // Envolve os itens em um AppListComponent para que o front-end saiba como renderizar a lista
        components.add(
            AppListComponent(
                components = userItems
            )
        )

        return Screen(
            components = components
        )
    }
}
