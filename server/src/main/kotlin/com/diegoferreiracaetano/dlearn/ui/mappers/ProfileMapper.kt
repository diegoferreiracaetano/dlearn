package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.services.Feature
import com.diegoferreiracaetano.dlearn.infrastructure.services.FeatureToggleService
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.ui.sdui.AppIconType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppProfileHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTextFieldComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTextFieldType
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.FooterComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.PremiumBannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ProfileRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.SectionComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.SectionItem
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class ProfileMapper(
    private val i18n: I18nProvider,
    private val featureToggleService: FeatureToggleService,
) {
    fun toHeader(data: User): ProfileRowComponent =
        ProfileRowComponent(
            name = data.name,
            email = data.email,
            imageUrl = data.imageUrl,
            editActionUrl = AppNavigationRoute.PROFILE_EDIT,
        )

    fun toEditHeader(data: User): AppProfileHeaderComponent =
        AppProfileHeaderComponent(
            name = data.name,
            email = data.email,
            imageUrl = data.imageUrl,
            onImagePickedAction = "/v1/profile/image",
        )

    fun toEditFields(data: User): List<Component> =
        listOf(
            AppTextFieldComponent(
                key = "full_name",
                value = data.name,
                label = AppStringType.FIELD_FULL_NAME,
                placeholder = AppStringType.FIELD_FULL_NAME,
                fieldType = AppTextFieldType.NONE,
            ),
            AppTextFieldComponent(
                key = "email",
                value = data.email,
                label = AppStringType.FIELD_EMAIL,
                placeholder = AppStringType.FIELD_EMAIL,
                fieldType = AppTextFieldType.EMAIL,
            ),
            AppTextFieldComponent(
                key = "phone",
                value = data.phoneNumber ?: "",
                label = AppStringType.FIELD_PHONE_NUMBER,
                placeholder = AppStringType.FIELD_PHONE_NUMBER,
                fieldType = AppTextFieldType.PHONE,
            ),
        )

    fun toSaveButton(lang: String): FooterComponent =
        FooterComponent(
            label = i18n.getString(AppStringType.SAVE_CHANGES, lang),
            actionUrl = AppNavigationRoute.PROFILE_UPDATE,
        )

    fun toPremiumBanner(
        data: User,
        lang: String,
    ): PremiumBannerComponent? {
        if (!data.isPremium) return null
        return PremiumBannerComponent(
            title = i18n.getString(AppStringType.PREMIUM_MEMBER, lang),
            description = i18n.getString(AppStringType.PREMIUM_DESCRIPTION, lang),
            icon = AppIconType.PERSON,
        )
    }

    fun toAccountSection(lang: String): SectionComponent {
        val items = mutableListOf<SectionItem>()

        if (featureToggleService.isEnabled(Feature.MEMBER_SECTION)) {
            items.add(
                SectionItem(
                    id = "member",
                    label = i18n.getString(AppStringType.ITEM_MEMBER, lang),
                    icon = AppIconType.PERSON,
                    actionUrl = AppNavigationRoute.MEMBER,
                ),
            )
        }

        items.add(
            SectionItem(
                id = "password",
                label = i18n.getString(AppStringType.ITEM_PASSWORD, lang),
                icon = AppIconType.LOCK,
                actionUrl = AppNavigationRoute.PROFILE_CHANGE_PASSWORD,
            ),
        )

        return SectionComponent(
            title = i18n.getString(AppStringType.SECTION_ACCOUNT, lang),
            items = items,
        )
    }

    fun toGeneralSection(
        currentLang: String,
        currentCountry: String?,
    ): SectionComponent {
        val languageType =
            when (currentLang.lowercase().replace("-", "_")) {
                "pt_br" -> AppStringType.LANGUAGE_PT_BR
                "en_us" -> AppStringType.LANGUAGE_EN_US
                "es_es" -> AppStringType.LANGUAGE_ES_ES
                else -> AppStringType.UNKNOWN
            }
        val languageDisplayName =
            if (languageType != AppStringType.UNKNOWN) {
                i18n.getString(languageType, currentLang)
            } else {
                currentLang
            }

        val countryType =
            when (currentCountry?.lowercase()) {
                "br" -> AppStringType.COUNTRY_BR
                "us" -> AppStringType.COUNTRY_US
                "es" -> AppStringType.COUNTRY_ES
                else -> AppStringType.UNKNOWN
            }
        val countryDisplayName =
            if (countryType != AppStringType.UNKNOWN) {
                i18n.getString(countryType, currentLang)
            } else {
                currentCountry ?: ""
            }

        return SectionComponent(
            title = i18n.getString(AppStringType.SECTION_GENERAL, currentLang),
            items =
                listOf(
                    SectionItem(
                        id = "notification",
                        label = i18n.getString(AppStringType.ITEM_NOTIFICATION, currentLang),
                        icon = AppIconType.NOTIFICATIONS,
                        actionUrl = AppNavigationRoute.SETTINGS_NOTIFICATIONS,
                    ),
                    SectionItem(
                        id = "language",
                        label = i18n.getString(AppStringType.ITEM_LANGUAGE, currentLang),
                        value = languageDisplayName,
                        icon = AppIconType.LANGUAGE,
                        actionUrl = AppNavigationRoute.SETTINGS_LANGUAGE,
                    ),
                    SectionItem(
                        id = "country",
                        label = i18n.getString(AppStringType.ITEM_COUNTRY, currentLang),
                        value = countryDisplayName,
                        icon = AppIconType.PUBLIC,
                        actionUrl = AppNavigationRoute.SETTINGS_COUNTRY,
                    ),
                    SectionItem(
                        id = "clear_cache",
                        label = i18n.getString(AppStringType.ITEM_CLEAR_CACHE, currentLang),
                        icon = AppIconType.DELETE,
                        actionUrl = AppNavigationRoute.SETTINGS_CLEAR_CACHE,
                    ),
                ),
        )
    }

    fun toMoreSection(lang: String): SectionComponent =
        SectionComponent(
            title = i18n.getString(AppStringType.SECTION_MORE, lang),
            items =
                listOf(
                    SectionItem(
                        id = "legal",
                        label = i18n.getString(AppStringType.ITEM_LEGAL, lang),
                        icon = AppIconType.POLICY,
                        actionUrl =
                            AppPath(
                                path = AppNavigationRoute.FAQ,
                                params = mapOf(AppQueryParam.REF to AppNavigationRoute.FAQ_PRIVACY_POLICY),
                            ),
                    ),
                    SectionItem(
                        id = "help",
                        label = i18n.getString(AppStringType.ITEM_HELP, lang),
                        icon = AppIconType.HELP,
                        actionUrl =
                            AppPath(
                                path = AppNavigationRoute.FAQ,
                                params = mapOf(AppQueryParam.REF to AppNavigationRoute.FAQ_HELP_FEEDBACK),
                            ),
                    ),
                    SectionItem(
                        id = "about",
                        label = i18n.getString(AppStringType.ITEM_ABOUT, lang),
                        icon = AppIconType.INFO,
                        actionUrl =
                            AppPath(
                                path = AppNavigationRoute.FAQ,
                                params = mapOf(AppQueryParam.REF to AppNavigationRoute.FAQ_ABOUT_US),
                            ),
                    ),
                ),
        )

    fun toFooter(lang: String): FooterComponent =
        FooterComponent(
            label = i18n.getString(AppStringType.LOGOUT, lang),
            closeUrl = AppNavigationRoute.LOGOUT,
        )
}
