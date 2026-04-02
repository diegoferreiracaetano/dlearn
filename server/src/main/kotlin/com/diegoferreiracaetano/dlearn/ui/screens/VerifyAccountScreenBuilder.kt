package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.Constants
import com.diegoferreiracaetano.dlearn.Constants.EMPTY_STRING
import com.diegoferreiracaetano.dlearn.ui.sdui.AppFeedbackComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSectionTitleComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTextFieldComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTextFieldType
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class VerifyAccountScreenBuilder(private val i18n: I18nProvider) {
    fun build(lang: String): Screen =
        Screen(
            components =
            listOf(
                AppSectionTitleComponent(
                    title = i18n.getString(AppStringType.VERIFY_ACCOUNT_TITLE, lang),
                ),
                AppTextFieldComponent(
                    value = EMPTY_STRING,
                    placeholder = AppStringType.FIELD_PASSWORD,
                    label = AppStringType.FIELD_PASSWORD,
                    key = Constants.OTP_CODE_KEY,
                    fieldType = AppTextFieldType.NONE,
                ),
                AppFeedbackComponent(
                    title = i18n.getString(AppStringType.VERIFY_ACCOUNT_FEEDBACK_TITLE, lang),
                    description = i18n.getString(AppStringType.VERIFY_ACCOUNT_FEEDBACK_DESC, lang),
                    primaryText = i18n.getString(AppStringType.VERIFY_ACCOUNT_PRIMARY_BUTTON, lang),
                    secondaryText = i18n.getString(AppStringType.VERIFY_ACCOUNT_SECONDARY_BUTTON, lang),
                ),
            ),
        )
}
