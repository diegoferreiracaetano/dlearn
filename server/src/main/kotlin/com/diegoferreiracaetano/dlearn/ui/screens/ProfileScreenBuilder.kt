package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.ProfileMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.Localization

class ProfileScreenBuilder(private val mapper: ProfileMapper) {
    fun build(data: ProfileDomainData, appVersion: Int, lang: String): Screen {
        val components = mutableListOf<Component>()

        components.add(mapper.toHeader(data))
        
        mapper.toPremiumBanner(data, lang)?.let {
            components.add(it)
        }

        components.add(mapper.toAccountSection(lang))
        components.add(mapper.toGeneralSection(data, lang))
        components.add(mapper.toMoreSection(lang))
        components.add(mapper.toFooter(lang))

        val container = AppContainerComponent(
            topBar = AppTopBarComponent(title = Localization.getString("profile_title", lang)),
            bottomBar = BottomNavigationComponent(
                items = listOf(
                    BottomNavItem(label = "Home", route = "home", iconIdentifier = "home"),
                    BottomNavItem(label = "Busca", route = "search", iconIdentifier = "search"),
                    BottomNavItem(label = "Favoritos", route = "favorite", iconIdentifier = "favorite"),
                    BottomNavItem(label = "Perfil", route = "profile", iconIdentifier = "person")
                ),
                selectedRoute = "profile"
            ),
            components = components
        )

        return Screen(
            id = "profile",
            components = listOf(container)
        )
    }
}
