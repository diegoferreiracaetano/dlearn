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
    private val faqData: Map<String, FaqItem> by lazy {
        val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream("faq_content.json")
            ?: throw IllegalStateException("faq_content.json not found")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        Json.decodeFromString<Map<String, FaqItem>>(jsonString)
    }

    fun fetchFaqContent(reference: String): FaqItem? {
        return faqData[reference]
    }
}
