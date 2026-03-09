package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class MovieDetailScreenBuilder(private val i18n: I18nProvider) {

    private companion object {
        const val DEFAULT_AVATAR_URL = "https://cdn-icons-png.flaticon.com/512/149/149071.png"
        const val PLAY_SCHEME = "dlearn://play/"
    }

    fun build(data: MovieDetailDomainData, appVersion: Int, lang: String): Screen {
        val components = mutableListOf<Component>()

        components.add(createHeader(data))
        components.add(createActionButtons(data, lang))
        components.add(createStoryLine(data, lang))
        components.add(createCastAndCrew(data, lang))
        
        createEpisodes(data, lang)?.let { components.add(it) }

        return Screen(
            id = "movie_detail_${data.id}",
            components = listOf(
                AppContainerComponent(
                    topBar = AppTopBarComponent(
                        title = data.title,
                        showSearch = false
                    ),
                    components = components
                )
            )
        )
    }

    private fun createHeader(data: MovieDetailDomainData) = AppMovieDetailHeaderComponent(
        title = data.title,
        imageUrl = data.imageUrl,
        year = data.year,
        duration = data.duration,
        genre = data.genre,
        rating = data.rating.toDoubleOrNull()
    )

    private fun createActionButtons(data: MovieDetailDomainData, lang: String) = SectionComponent(
        title = "",
        items = listOf(
            SectionItem(
                id = "play",
                label = i18n.getString(AppStringType.DETAIL_TRAILER, lang),
                icon = AppIconType.PLAY_ARROW,
                actionUrl = "$PLAY_SCHEME${data.id}"
            ),
            SectionItem(id = "download", label = "", icon = AppIconType.DOWNLOAD),
            SectionItem(id = "share", label = "", icon = AppIconType.SHARE)
        )
    )

    private fun createStoryLine(data: MovieDetailDomainData, lang: String) = SectionComponent(
        title = i18n.getString(AppStringType.DETAIL_STORY_LINE, lang),
        items = listOf(
            SectionItem(
                id = "story_${data.id}",
                label = data.storyLine
            )
        )
    )

    private fun createCastAndCrew(data: MovieDetailDomainData, lang: String) = CarouselComponent(
        title = i18n.getString(AppStringType.DETAIL_CAST_CREW, lang),
        items = data.cast.map {
            UserRowComponent(
                name = it.name,
                role = it.role,
                imageUrl = it.imageUrl ?: DEFAULT_AVATAR_URL
            )
        }
    )

    private fun createEpisodes(data: MovieDetailDomainData, lang: String): Component? {
        val season1 = data.seasons.firstOrNull { it.number == 1 } ?: return null
        
        return SectionComponent(
            title = i18n.getString(AppStringType.DETAIL_EPISODE, lang),
            items = season1.episodes.map {
                SectionItem(
                    id = it.id,
                    label = it.title,
                    value = it.duration,
                    icon = if (it.isPremium) AppIconType.WORKSPACE_PREMIUM else null,
                    actionUrl = "$PLAY_SCHEME${it.id}"
                )
            }
        )
    }
}
