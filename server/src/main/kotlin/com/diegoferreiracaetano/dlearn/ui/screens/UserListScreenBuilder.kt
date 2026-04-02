package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.ui.sdui.AppListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UserRowComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class UserListScreenBuilder(private val i18n: I18nProvider) {
    fun build(
        users: List<User>,
        lang: String,
    ): Screen {
        val components = mutableListOf<Component>()

        components.add(
            AppTopBarComponent(
                title = i18n.getString(AppStringType.USER_LIST_TITLE, lang),
            ),
        )

        val userItems =
            users.map { user ->
                UserRowComponent(
                    name = user.name,
                    role =
                        if (user.isPremium) {
                            i18n.getString(AppStringType.USER_ROLE_PREMIUM, lang)
                        } else {
                            i18n.getString(AppStringType.USER_ROLE_FREE, lang)
                        },
                    imageUrl = user.imageUrl,
                    actionUrl = AppPath(AppNavigationRoute.PROFILE, mapOf(AppQueryParam.USER_ID to user.id)),
                )
            }

        components.add(
            AppListComponent(
                components = userItems,
            ),
        )

        return Screen(
            components = components,
        )
    }
}
