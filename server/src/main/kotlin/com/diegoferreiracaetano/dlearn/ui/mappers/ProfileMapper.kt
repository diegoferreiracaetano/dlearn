package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.LocaleConstants
import com.diegoferreiracaetano.dlearn.ProfileConstants
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
            onImagePickedAction = ProfileConstants.ACTION_IMAGE_PICKER,
        )

    fun toEditFields(data: User): List<Component> =
        listOf(
            AppTextFieldComponent(
                key = ProfileConstants.KEY_FULL_NAME,
                value = data.name,
                label = AppStringType.FIELD_FULL_NAME,
                placeholder = AppStringType.FIELD_FULL_NAME,
                fieldType = AppTextFieldType.NONE,
            ),
            AppTextFieldComponent(
                key = ProfileConstants.KEY_EMAIL,
                value = data.email,
                label = AppStringType.FIELD_EMAIL,
                placeholder = AppStringType.FIELD_EMAIL,
                fieldType = AppTextFieldType.EMAIL,
            ),
            AppTextFieldComponent(
                key = ProfileConstants.KEY_PHONE,
                value = data.phoneNumber.orEmpty(),
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
                    id = ProfileConstants.ID_MEMBER,
                    label = i18n.getString(AppStringType.ITEM_MEMBER, lang),
                    icon = AppIconType.PERSON,
                    actionUrl = AppNavigationRoute.MEMBER,
                ),
            )
        }

        items.add(
            SectionItem(
                id = ProfileConstants.ID_PASSWORD,
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
            when (currentLang.trim()) {
                LocaleConstants.LANG_PT_BR -> AppStringType.LANGUAGE_PT_BR
                LocaleConstants.LANG_EN_US -> AppStringType.LANGUAGE_EN_US
                LocaleConstants.LANG_ES_ES -> AppStringType.LANGUAGE_ES_ES
                else -> AppStringType.UNKNOWN
            }
        val languageDisplayName =
            if (languageType != AppStringType.UNKNOWN) {
                i18n.getString(languageType, currentLang)
            } else {
                currentLang
            }

        val countryType =
            when (currentCountry?.trim()?.uppercase()) {
                LocaleConstants.COUNTRY_BR -> AppStringType.COUNTRY_BR
                LocaleConstants.COUNTRY_US -> AppStringType.COUNTRY_US
                LocaleConstants.COUNTRY_ES -> AppStringType.COUNTRY_ES
                else -> AppStringType.UNKNOWN
            }
        val countryDisplayName =
            if (countryType != AppStringType.UNKNOWN) {
                i18n.getString(countryType, currentLang)
            } else {
                currentCountry.orEmpty()
            }

        return SectionComponent(
            title = i18n.getString(AppStringType.SECTION_GENERAL, currentLang),
            items =
            listOf(
                SectionItem(
                    id = ProfileConstants.ID_NOTIFICATION,
                    label = i18n.getString(AppStringType.ITEM_NOTIFICATION, currentLang),
                    icon = AppIconType.NOTIFICATIONS,
                    actionUrl = AppNavigationRoute.SETTINGS_NOTIFICATIONS,
                ),
                SectionItem(
                    id = ProfileConstants.ID_LANGUAGE,
                    label = i18n.getString(AppStringType.ITEM_LANGUAGE, currentLang),
                    value = languageDisplayName,
                    icon = AppIconType.LANGUAGE,
                    actionUrl = AppNavigationRoute.SETTINGS_LANGUAGE,
                ),
                SectionItem(
                    id = ProfileConstants.ID_COUNTRY,
                    label = i18n.getString(AppStringType.ITEM_COUNTRY, currentLang),
                    value = countryDisplayName,
                    icon = AppIconType.PUBLIC,
                    actionUrl = AppNavigationRoute.SETTINGS_COUNTRY,
                ),
                SectionItem(
                    id = ProfileConstants.ID_CLEAR_CACHE,
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
                    id = ProfileConstants.ID_LEGAL,
                    label = i18n.getString(AppStringType.ITEM_LEGAL, lang),
                    icon = AppIconType.POLICY,
                    actionUrl =
                    AppPath(
                        path = AppNavigationRoute.FAQ,
                        params = mapOf(AppQueryParam.REF to AppNavigationRoute.FAQ_PRIVACY_POLICY),
                    ),
                ),
                SectionItem(
                    id = ProfileConstants.ID_HELP,
                    label = i18n.getString(AppStringType.ITEM_HELP, lang),
                    icon = AppIconType.HELP,
                    actionUrl =
                    AppPath(
                        path = AppNavigationRoute.FAQ,
                        params = mapOf(AppQueryParam.REF to AppNavigationRoute.FAQ_HELP_FEEDBACK),
                    ),
                ),
                SectionItem(
                    id = ProfileConstants.ID_ABOUT,
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
