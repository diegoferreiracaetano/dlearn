package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.ProfileMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class EditProfileScreenBuilder(
    private val mapper: ProfileMapper,
    private val i18n: I18nProvider
) {
    fun build(
        data: ProfileDomainData,
        lang: String,
        status: AppStringType? = null,
        type: AppSnackbarType = AppSnackbarType.SUCCESS
    ): Screen {
        val components = mutableListOf<Component>()

        status?.let {
            components.add(
                AppSnackbarComponent(
                    message = i18n.getString(it, lang),
                    snackbarType = type
                )
            )
        }

        components.add(mapper.toEditHeader(data))
        components.addAll(mapper.toEditFields(data, lang))
        components.add(mapper.toSaveButton(lang))

        val topBar = AppTopBarComponent(
            title = i18n.getString(AppStringType.NAV_PROFILE, lang)
        )

        return Screen(
            components = listOf(
                AppContainerComponent(
                    topBar = topBar,
                    components = components
                )
            )
        )
    }
}
