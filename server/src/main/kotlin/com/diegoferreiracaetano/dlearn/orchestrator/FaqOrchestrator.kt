package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.infrastructure.services.FaqDataService
import com.diegoferreiracaetano.dlearn.ui.screens.FaqScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FaqOrchestrator(
    private val faqDataService: FaqDataService,
    private val faqScreenBuilder: FaqScreenBuilder
) {
    fun getFaqData(reference: String): Flow<Screen> = flow {
        val faqItem = faqDataService.fetchFaqContent(reference)
            ?: throw IllegalArgumentException("FAQ not found for reference: $reference")
        emit(faqScreenBuilder.build(faqItem))
    }
}
