package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class ProfileMapper(private val i18n: I18nProvider) {
    fun toHeader(data: ProfileDomainData): ProfileRowComponent {
        return ProfileRowComponent(
            name = data.name,
            email = data.email,
            imageUrl = data.imageUrl,
            editActionUrl = "/edit-profile"
        )
    }

    fun toPremiumBanner(data: ProfileDomainData, lang: String): PremiumBannerComponent? {
        if (!data.isPremium) return null
        return PremiumBannerComponent(
            title = i18n.getString(AppStringType.PREMIUM_MEMBER, lang),
            description = i18n.getString(AppStringType.PREMIUM_DESCRIPTION, lang),
            icon = AppIconType.PERSON,
            actionUrl = "/premium"
        )
    }

    fun toAccountSection(lang: String): SectionComponent {
        return SectionComponent(
            title = i18n.getString(AppStringType.SECTION_ACCOUNT, lang),
            items = listOf(
                SectionItem(
                    id = "member",
                    label = i18n.getString(AppStringType.ITEM_MEMBER, lang),
                    icon = AppIconType.PERSON,
                    actionUrl = "/member"
                ),
                SectionItem(
                    id = "password",
                    label = i18n.getString(AppStringType.ITEM_PASSWORD, lang),
                    icon = AppIconType.LOCK,
                    actionUrl = "/change-password"
                )
            )
        )
    }

    fun toGeneralSection(data: ProfileDomainData, lang: String): SectionComponent {
        return SectionComponent(
            title = i18n.getString(AppStringType.SECTION_GENERAL, lang),
            items = listOf(
                SectionItem(
                    id = "notification",
                    label = i18n.getString(AppStringType.ITEM_NOTIFICATION, lang),
                    icon = AppIconType.NOTIFICATIONS,
                    actionUrl = "/notifications"
                ),
                SectionItem(
                    id = "language",
                    label = i18n.getString(AppStringType.ITEM_LANGUAGE, lang),
                    value = data.language,
                    icon = AppIconType.LANGUAGE,
                    actionUrl = "/language"
                ),
                SectionItem(
                    id = "country",
                    label = i18n.getString(AppStringType.ITEM_COUNTRY, lang),
                    value = data.country,
                    icon = AppIconType.PUBLIC,
                    actionUrl = "/country"
                ),
                SectionItem(
                    id = "clear_cache",
                    label = i18n.getString(AppStringType.ITEM_CLEAR_CACHE, lang),
                    icon = AppIconType.DELETE,
                    actionUrl = "/clear-cache"
                )
            )
        )
    }

    fun toMoreSection(lang: String): SectionComponent {
        return SectionComponent(
            title = i18n.getString(AppStringType.SECTION_MORE, lang),
            items = listOf(
                SectionItem(
                    id = "legal",
                    label = i18n.getString(AppStringType.ITEM_LEGAL, lang),
                    icon = AppIconType.POLICY,
                    actionUrl = "/legal"
                ),
                SectionItem(
                    id = "help",
                    label = i18n.getString(AppStringType.ITEM_HELP, lang),
                    icon = AppIconType.HELP,
                    actionUrl = "/help"
                ),
                SectionItem(
                    id = "about",
                    label = i18n.getString(AppStringType.ITEM_ABOUT, lang),
                    icon = AppIconType.INFO,
                    actionUrl = "/about"
                )
            )
        )
    }

    fun toFooter(lang: String): FooterComponent {
        return FooterComponent(
            label = i18n.getString(AppStringType.LOGOUT, lang),
            actionUrl = "/logout"
        )
    }
}
