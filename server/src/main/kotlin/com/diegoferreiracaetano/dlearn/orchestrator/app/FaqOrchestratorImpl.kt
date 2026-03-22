package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.infrastructure.services.FaqDataService
import com.diegoferreiracaetano.dlearn.ui.screens.FaqScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FaqOrchestratorImpl(
    private val faqDataService: FaqDataService,
    private val faqScreenBuilder: FaqScreenBuilder
) : AppOrchestrator {

    override fun execute(
        request: AppRequest,
        userId: String,
        lang: String,
        appVersion: Int
    ): Flow<Screen> {
        val reference = request.params?.get(NavigationRoutes.FAQ_REF_ARG)
            ?: throw IllegalArgumentException("FAQ reference missing")

        val faqItem = faqDataService.fetchFaqContent(reference, lang)
            ?: throw IllegalArgumentException("FAQ not found for reference: $reference")
        return flowOf(faqScreenBuilder.build(faqItem))
    }
}
