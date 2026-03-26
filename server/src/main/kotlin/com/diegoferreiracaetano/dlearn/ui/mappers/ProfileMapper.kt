package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class ProfileMapper(private val i18n: I18nProvider) {
    fun toHeader(data: User): ProfileRowComponent {
        return ProfileRowComponent(
            name = data.name,
            email = data.email,
            imageUrl = data.imageUrl,
            editActionUrl = AppNavigationRoute.PROFILE_EDIT
        )
    }

    fun toEditHeader(data: User): AppProfileHeaderComponent {
        return AppProfileHeaderComponent(
            name = data.name,
            email = data.email,
            imageUrl = data.imageUrl,
            onImagePickedAction = "/v1/profile/image"
        )
    }

    fun toEditFields(data: User): List<Component> {
        return listOf(
            AppTextFieldComponent(
                key = "full_name",
                value = data.name,
                label = AppStringType.FIELD_FULL_NAME,
                placeholder = AppStringType.FIELD_FULL_NAME,
                fieldType = AppTextFieldType.NONE
            ),
            AppTextFieldComponent(
                key = "email",
                value = data.email,
                label = AppStringType.FIELD_EMAIL,
                placeholder = AppStringType.FIELD_EMAIL,
                fieldType = AppTextFieldType.EMAIL
            ),
            AppTextFieldComponent(
                key = "phone",
                value = data.phoneNumber ?: "",
                label = AppStringType.FIELD_PHONE_NUMBER,
                placeholder = AppStringType.FIELD_PHONE_NUMBER,
                fieldType = AppTextFieldType.PHONE
            )
        )
    }

    fun toSaveButton(lang: String): FooterComponent {
        return FooterComponent(
            label = i18n.getString(AppStringType.SAVE_CHANGES, lang),
            actionUrl = AppNavigationRoute.PROFILE_UPDATE
        )
    }

    fun toPremiumBanner(data: User, lang: String): PremiumBannerComponent? {
        if (!data.isPremium) return null
        return PremiumBannerComponent(
            title = i18n.getString(AppStringType.PREMIUM_MEMBER, lang),
            description = i18n.getString(AppStringType.PREMIUM_DESCRIPTION, lang),
            icon = AppIconType.PERSON
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
                    actionUrl = AppNavigationRoute.MEMBER
                ),
                SectionItem(
                    id = "password",
                    label = i18n.getString(AppStringType.ITEM_PASSWORD, lang),
                    icon = AppIconType.LOCK,
                    actionUrl = AppNavigationRoute.PROFILE_CHANGE_PASSWORD
                )
            )
        )
    }

    fun toGeneralSection(currentLang: String, currentCountry: String?): SectionComponent {
        // Busca o nome traduzido do idioma (ex: language_pt_br)
        val langKey = "language_${currentLang.replace("-", "_").lowercase()}"
        val languageDisplayName = i18n.getRawString(langKey, currentLang) ?: currentLang

        // Busca o nome traduzido do país (ex: country_br)
        val countryKey = "country_${currentCountry?.lowercase()}"
        val countryDisplayName = currentCountry?.let { i18n.getRawString(countryKey, currentLang) } ?: currentCountry ?: ""

        return SectionComponent(
            title = i18n.getString(AppStringType.SECTION_GENERAL, currentLang),
            items = listOf(
                SectionItem(
                    id = "notification",
                    label = i18n.getString(AppStringType.ITEM_NOTIFICATION, currentLang),
                    icon = AppIconType.NOTIFICATIONS,
                    actionUrl = AppNavigationRoute.SETTINGS_NOTIFICATIONS
                ),
                SectionItem(
                    id = "language",
                    label = i18n.getString(AppStringType.ITEM_LANGUAGE, currentLang),
                    value = languageDisplayName,
                    icon = AppIconType.LANGUAGE,
                    actionUrl = AppNavigationRoute.SETTINGS_LANGUAGE
                ),
                SectionItem(
                    id = "country",
                    label = i18n.getString(AppStringType.ITEM_COUNTRY, currentLang),
                    value = countryDisplayName,
                    icon = AppIconType.PUBLIC,
                    actionUrl = AppNavigationRoute.SETTINGS_COUNTRY
                ),
                SectionItem(
                    id = "clear_cache",
                    label = i18n.getString(AppStringType.ITEM_CLEAR_CACHE, currentLang),
                    icon = AppIconType.DELETE,
                    actionUrl = AppNavigationRoute.SETTINGS_CLEAR_CACHE
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
                    actionUrl = AppPath(AppNavigationRoute.FAQ, mapOf(AppQueryParam.REF to "privacy-policy"))
                ),
                SectionItem(
                    id = "help",
                    label = i18n.getString(AppStringType.ITEM_HELP, lang),
                    icon = AppIconType.HELP,
                    actionUrl = AppPath(AppNavigationRoute.FAQ, mapOf(AppQueryParam.REF to "help-feedback"))
                ),
                SectionItem(
                    id = "about",
                    label = i18n.getString(AppStringType.ITEM_ABOUT, lang),
                    icon = AppIconType.INFO,
                    actionUrl = AppPath(AppNavigationRoute.FAQ, mapOf(AppQueryParam.REF to "about-us"))
                )
            )
        )
    }

    fun toFooter(lang: String): FooterComponent {
        return FooterComponent(
            label = i18n.getString(AppStringType.LOGOUT, lang),
            closeUrl = AppNavigationRoute.LOGOUT
        )
    }
}
