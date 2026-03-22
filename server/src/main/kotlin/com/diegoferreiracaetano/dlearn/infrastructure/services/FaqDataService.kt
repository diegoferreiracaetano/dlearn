package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.util.fromJson
import com.diegoferreiracaetano.dlearn.util.getLogger
import kotlinx.serialization.Serializable
import java.io.InputStream

@Serializable
data class FaqItem(
    val title: String,
    val content: String,
)

class FaqDataService {
    private val faqData: Map<String, Map<String, FaqItem>> by lazy {
        try {
            val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream("faq_content.json")
                ?: throw IllegalStateException("faq_content.json not found")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            jsonString.fromJson<Map<String, Map<String, FaqItem>>>()
        } catch (e: Exception) {
            getLogger().d("FaqDataService", "Error loading faq_content.json: ${e.message}")
            emptyMap()
        }
    }

    fun fetchFaqContent(reference: String, lang: String,): FaqItem? {
        val langCode = normalizeLanguage(lang)
        
        return faqData[langCode]?.get(reference)
            ?: faqData["en"]?.get(reference) // Fallback apenas para inglês se não achar a chave no idioma solicitado
    }

    private fun normalizeLanguage(language: String): String {
        return language.split(",")
            .firstOrNull()
            ?.split(";")
            ?.firstOrNull()
            ?.split("-")
            ?.firstOrNull()
            ?.trim()
            ?.lowercase() ?: "en"
    }
}
