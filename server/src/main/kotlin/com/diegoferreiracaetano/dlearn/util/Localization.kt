package com.diegoferreiracaetano.dlearn.util

object Localization {
    private val translations = mapOf(
        "en" to mapOf(
            "profile_title" to "Profile",
            "premium_member" to "Premium Member",
            "premium_description" to "New movies are coming for you, Download Now!",
            "section_account" to "Account",
            "item_member" to "Member",
            "item_password" to "Change Password",
            "section_general" to "General",
            "item_notification" to "Notification",
            "item_language" to "Language",
            "item_country" to "Country",
            "item_clear_cache" to "Clear Cache",
            "section_more" to "More",
            "item_legal" to "Legal and Policies",
            "item_help" to "Help & Feedback",
            "item_about" to "About Us",
            "logout" to "Log Out"
        ),
        "pt" to mapOf(
            "profile_title" to "Perfil",
            "premium_member" to "Membro Premium",
            "premium_description" to "Novos filmes estão chegando para você, Baixe Agora!",
            "section_account" to "Conta",
            "item_member" to "Membro",
            "item_password" to "Alterar Senha",
            "section_general" to "Geral",
            "item_notification" to "Notificações",
            "item_language" to "Idioma",
            "item_country" to "País",
            "item_clear_cache" to "Limpar Cache",
            "section_more" to "Mais",
            "item_legal" to "Políticas e Legal",
            "item_help" to "Ajuda e Feedback",
            "item_about" to "Sobre Nós",
            "logout" to "Sair"
        )
    )

    fun getString(key: String, lang: String): String {
        val language = if (lang.startsWith("pt")) "pt" else "en"
        return translations[language]?.get(key) ?: translations["en"]?.get(key) ?: key
    }
}
