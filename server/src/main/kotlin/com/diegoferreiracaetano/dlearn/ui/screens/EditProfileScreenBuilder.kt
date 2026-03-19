package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.ProfileMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class EditProfileScreenBuilder(
    private val mapper: ProfileMapper,
    private val i18n: I18nProvider
) {
    fun build(data: ProfileDomainData, lang: String): Screen {
        val components = mutableListOf<Component>()

        components.add(mapper.toEditHeader(data))
        components.addAll(mapper.toEditFields(data, lang))
        components.add(mapper.toSaveButton(lang))

        return Screen(
            components = components
        )
    }
}
