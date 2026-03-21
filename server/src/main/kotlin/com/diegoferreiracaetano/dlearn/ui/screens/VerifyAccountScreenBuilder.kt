package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ui.sdui.*

class VerifyAccountScreenBuilder {
    fun build(): Screen {
        return Screen(
            components = listOf(
                AppSectionTitleComponent(
                    title = "Verificação de Conta"
                ),
                AppTextFieldComponent(
                    value = "",
                    placeholder = AppStringType.FILTER_MOVIES, // Exemplo de string do projeto
                    label = AppStringType.FILTER_SERIES,
                    key = "otp_code",
                    fieldType = AppTextFieldType.NONE
                ),
                AppFeedbackComponent(
                    title = "Código Enviado",
                    description = "Enviamos um código para o seu e-mail cadastrado. Por favor, insira-o para continuar.",
                    primaryText = "Verificar",
                    secondaryText = "Reenviar Código"
                )
            )
        )
    }
}
