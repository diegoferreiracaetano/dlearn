package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import kotlinx.serialization.Serializable

@Serializable
data class FaqItem(
    val title: String,
    val content: String,
)

class FaqDataService(
    private val i18n: I18nProvider,
) {
    fun fetchFaqContent(
        reference: String,
        lang: String,
    ): FaqItem? {
        val (titleKey, contentKey) =
            when (reference) {
                "privacy-policy" -> AppStringType.LEGAL_PRIVACY_TITLE to AppStringType.LEGAL_PRIVACY_CONTENT
                "terms-of-service" -> AppStringType.LEGAL_TERMS_TITLE to AppStringType.LEGAL_TERMS_CONTENT
                "about-us" -> AppStringType.ABOUT_US_TITLE to AppStringType.ABOUT_US_CONTENT
                "help-feedback" -> AppStringType.HELP_FEEDBACK_TITLE to AppStringType.HELP_FEEDBACK_CONTENT
                else -> return null
            }

        return FaqItem(
            title = i18n.getString(titleKey, lang),
            content = i18n.getString(contentKey, lang),
        )
    }
}
