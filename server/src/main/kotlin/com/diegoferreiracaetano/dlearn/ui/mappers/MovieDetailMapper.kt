package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class MovieDetailMapper(private val i18n: I18nProvider) {

    private companion object {
        const val DEFAULT_AVATAR_URL = "https://cdn-icons-png.flaticon.com/512/149/149071.png"
        const val PLAY_SCHEME = "dlearn://play/"
        const val SHARE_SCHEME = "dlearn://share/"
        const val DOWNLOAD_SCHEME = "dlearn://download/"
    }

    fun toHeader(data: MovieDetailDomainData, lang: String): AppMovieDetailHeaderComponent {
        return AppMovieDetailHeaderComponent(
            title = data.title,
            imageUrl = data.imageUrl,
            year = data.year,
            duration = data.duration,
            genre = data.genre,
            rating = data.rating.toDoubleOrNull(),
            trailerId = data.trailerId,
            playText = i18n.getString(AppStringType.DETAIL_TRAILER, lang),
            downloadActionUrl = "$DOWNLOAD_SCHEME${data.id}",
            shareActionUrl = "$SHARE_SCHEME${data.id}"
        )
    }

    fun toStoryLine(data: MovieDetailDomainData, lang: String): AppExpandableSectionComponent {
        return AppExpandableSectionComponent(
            title = i18n.getString(AppStringType.DETAIL_STORY_LINE, lang),
            text = data.storyLine
        )
    }

    fun toCastCarousel(data: MovieDetailDomainData, lang: String): CarouselComponent {
        return CarouselComponent(
            title = i18n.getString(AppStringType.DETAIL_CAST_CREW, lang),
            items = data.cast.map {
                UserRowComponent(
                    name = it.name,
                    role = it.role,
                    imageUrl = it.imageUrl ?: DEFAULT_AVATAR_URL
                )
            }
        )
    }

    fun toEpisodesSection(data: MovieDetailDomainData, lang: String): SectionComponent? {
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
