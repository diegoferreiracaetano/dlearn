package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.infrastructure.services.FaqDataService
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.FaqScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FaqOrchestrator(
    private val faqDataService: FaqDataService,
    private val screenBuilder: FaqScreenBuilder,
) : Orchestrator {
    override fun execute(
        request: AppRequest,
        header: AppHeader,
        userId: String,
    ): Flow<Screen> =
        flow {
            val language = header.language
            val reference = request.params?.get(AppQueryParam.REF) ?: "default"
            val domainData =
                faqDataService.fetchFaqContent(reference, language)
                    ?: throw IllegalArgumentException("FAQ not found for reference: $reference")

            emit(screenBuilder.build(domainData))
        }
}
