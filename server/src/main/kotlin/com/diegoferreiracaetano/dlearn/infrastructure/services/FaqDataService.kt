package com.diegoferreiracaetano.dlearn.infrastructure.services

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.io.InputStream

@Serializable
data class FaqItem(
    val title: String,
    val content: String
)

class FaqDataService {
    private val faqData: Map<String, Map<String, FaqItem>> by lazy {
        val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream("faq_content.json")
            ?: throw IllegalStateException("faq_content.json not found")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        Json.decodeFromString<Map<String, Map<String, FaqItem>>>(jsonString)
    }

    fun fetchFaqContent(reference: String, language: String): FaqItem? {
        val normalizedLang = normalizeLanguage(language)
        return faqData[normalizedLang]?.get(reference)
            ?: faqData["en"]?.get(reference)
    }

    private fun normalizeLanguage(language: String): String {
        // Handle both simple "en" and complex "pt-BR;q=0.9"
        val langCode = language.split(",")[0]
            .split(";")[0]
            .split("-")[0]
            .split("_")[0]
            .trim()
            .lowercase()

        return if (faqData.containsKey(langCode)) langCode else "en"
    }
}
