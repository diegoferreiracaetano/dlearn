package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.*

class HomeScreenBuilder(private val mapper: HomeMapper) {
    fun build(data: HomeDomainData, appVersion: Int): Screen {
        val components = mutableListOf<Component>()

        // Adicionando Chips de Filtro
        components.add(
            ChipGroupComponent(
                id = "filters",
                items = listOf(
                    ChipItem(id = "series", label = "Séries"),
                    ChipItem(id = "movies", label = "Filmes"),
                    ChipItem(id = "categories", label = "Categorias")
                )
            )
        )

        // Banner Principal
        data.banner?.let {
            components.add(mapper.toBannerMain(it))
        }

        // Seção Top 10
        if (data.top10.isNotEmpty()) {
            components.add(mapper.toCarousel("Top 10", data.top10))
        }

        // Seção Populares
        if (data.popular.isNotEmpty()) {
            components.add(mapper.toBannerCarousel("Populares", data.popular))
        }

        // Categorias Dinâmicas
        data.categories.forEach { (categoryName, videos) ->
            if (videos.isNotEmpty()) {
                components.add(mapper.toCarousel(categoryName, videos))
            }
        }

        val container = AppContainerComponent(
            topBar = AppTopBarComponent(
                title = "DLearn",
                showSearch = true
            ),
            bottomBar = BottomNavigationComponent(
                items = listOf(
                    BottomNavItem(label = "Home", route = "home", iconIdentifier = "home"),
                    BottomNavItem(label = "Busca", route = "search", iconIdentifier = "search"),
                    BottomNavItem(label = "Favoritos", route = "favorite", iconIdentifier = "favorite"),
                    BottomNavItem(label = "Perfil", route = "person", iconIdentifier = "person")
                ),
                selectedRoute = "home"
            ),
            components = components
        )

        return Screen(
            id = "home",
            components = listOf(container)
        )
    }
}
