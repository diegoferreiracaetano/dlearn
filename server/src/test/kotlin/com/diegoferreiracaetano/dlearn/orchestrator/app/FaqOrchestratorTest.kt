package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.infrastructure.services.FaqDataService
import com.diegoferreiracaetano.dlearn.infrastructure.services.FaqItem
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.FaqScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FaqOrchestratorTest {

    private lateinit var subject: FaqOrchestrator
    private val faqDataService = mockk<FaqDataService>(relaxed = true)
    private val screenBuilder = mockk<FaqScreenBuilder>(relaxed = true)

    private val userId = "user123"
    private val lang = "pt-BR"
    private val header = AppHeader(paramLanguage = lang)

    @Before
    fun setup() {
        subject = FaqOrchestrator(faqDataService, screenBuilder)
    }

    @Test
    fun `given a faq request with reference when execute is called should return the faq screen`() = runTest {
        val reference = "help"
        val request = AppRequest(path = "faq", params = mapOf(AppQueryParam.REF to reference))
        val faqItem = mockk<FaqItem>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { faqDataService.fetchFaqContent(reference, lang) } returns faqItem
        every { screenBuilder.build(faqItem) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a faq request when the content is not found should throw an IllegalArgumentException`() = runTest {
        val request = AppRequest(path = "faq")

        coEvery { faqDataService.fetchFaqContent(any(), lang) } returns null

        assertFailsWith<IllegalArgumentException> {
            subject.execute(request, header, userId).first()
        }
    }
}
