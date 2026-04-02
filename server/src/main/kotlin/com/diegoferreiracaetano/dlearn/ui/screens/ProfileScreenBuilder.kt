package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.ui.mappers.ProfileMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

class ProfileScreenBuilder(
    private val mapper: ProfileMapper,
) {
    fun build(
        data: User,
        lang: String,
        country: String?,
    ): Screen {
        val components = mutableListOf<Component>()

        components.addAll(buildContent(data, lang, country))

        return Screen(
            components = components,
        )
    }

    fun buildContent(
        data: User,
        lang: String,
        country: String?,
    ): List<Component> {
        val components = mutableListOf<Component>()

        components.add(mapper.toHeader(data))

        mapper.toPremiumBanner(data, lang)?.let {
            components.add(it)
        }

        components.add(mapper.toAccountSection(lang))
        components.add(mapper.toGeneralSection(lang, country))
        components.add(mapper.toMoreSection(lang))
        components.add(mapper.toFooter(lang))

        return components
    }
}
