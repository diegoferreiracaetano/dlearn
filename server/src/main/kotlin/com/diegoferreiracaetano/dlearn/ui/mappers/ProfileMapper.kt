package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.Localization

class ProfileMapper {
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
            title = Localization.getString("premium_member", lang),
            description = Localization.getString("premium_description", lang),
            iconIdentifier = "premium",
            actionUrl = "/premium"
        )
    }

    fun toAccountSection(lang: String): SectionComponent {
        return SectionComponent(
            title = Localization.getString("section_account", lang),
            items = listOf(
                SectionItem(id = "member", label = Localization.getString("item_member", lang), iconIdentifier = "person", actionUrl = "/member"),
                SectionItem(id = "password", label = Localization.getString("item_password", lang), iconIdentifier = "lock", actionUrl = "/change-password")
            )
        )
    }

    fun toGeneralSection(data: ProfileDomainData, lang: String): SectionComponent {
        return SectionComponent(
            title = Localization.getString("section_general", lang),
            items = listOf(
                SectionItem(id = "notification", label = Localization.getString("item_notification", lang), iconIdentifier = "notifications", actionUrl = "/notifications"),
                SectionItem(id = "language", label = Localization.getString("item_language", lang), value = data.language, iconIdentifier = "language", actionUrl = "/language"),
                SectionItem(id = "country", label = Localization.getString("item_country", lang), value = data.country, iconIdentifier = "public", actionUrl = "/country"),
                SectionItem(id = "clear_cache", label = Localization.getString("item_clear_cache", lang), iconIdentifier = "delete", actionUrl = "/clear-cache")
            )
        )
    }

    fun toMoreSection(lang: String): SectionComponent {
        return SectionComponent(
            title = Localization.getString("section_more", lang),
            items = listOf(
                SectionItem(id = "legal", label = Localization.getString("item_legal", lang), iconIdentifier = "policy", actionUrl = "/legal"),
                SectionItem(id = "help", label = Localization.getString("item_help", lang), iconIdentifier = "help", actionUrl = "/help"),
                SectionItem(id = "about", label = Localization.getString("item_about", lang), iconIdentifier = "info", actionUrl = "/about")
            )
        )
    }

    fun toFooter(lang: String): FooterComponent {
        return FooterComponent(
            label = Localization.getString("logout", lang),
            actionUrl = "/logout"
        )
    }
}
