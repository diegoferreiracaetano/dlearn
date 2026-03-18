package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.ProfileMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class ProfileScreenBuilder(
    private val mapper: ProfileMapper,
    private val i18n: I18nProvider
) {
    fun build(data: ProfileDomainData, appVersion: Int, lang: String): Screen {
        val components = buildContent(data, lang)

        return Screen(
            id = "profile",
            components = components,
            showSearch = false,
            topBar = AppTopBarComponent(
                title = i18n.getString(AppStringType.NAV_PROFILE, lang),
                showSearch = false
            )
        )
    }

    fun buildContent(data: ProfileDomainData, lang: String): List<Component> {
        val components = mutableListOf<Component>()

        components.add(mapper.toHeader(data))
        
        mapper.toPremiumBanner(data, lang)?.let {
            components.add(it)
        }

        components.add(mapper.toAccountSection(lang))
        components.add(mapper.toGeneralSection(data, lang))
        components.add(mapper.toMoreSection(lang))
        components.add(mapper.toFooter(lang))

        return components
    }
}
