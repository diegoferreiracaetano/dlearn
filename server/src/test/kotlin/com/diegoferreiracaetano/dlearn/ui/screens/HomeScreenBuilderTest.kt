package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.BannerCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.FullScreenBannerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieCarouselComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HomeScreenBuilderTest {

    private val mapper = mockk<HomeMapper>(relaxed = true)
    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val builder = HomeScreenBuilder(mapper, i18n)

    @Test
    fun `given home domain data when build is called should return screen with all expected SDUI components`() {
        val data = HomeDomainData(
            banner = mockk(),
            top10 = listOf(mockk()),
            popular = listOf(mockk()),
            categories = mapOf("Action" to listOf(mockk()))
        )
        
        every { mapper.toBannerMain(any()) } returns mockk<FullScreenBannerComponent>()
        every { mapper.toCarousel(any(), any(), any(), any()) } returns mockk<MovieCarouselComponent>()
        every { mapper.toBannerCarousel(any(), any(), any()) } returns mockk<BannerCarouselComponent>()

        val screen = builder.build(data, "en", HomeFilterType.ALL)

        assertTrue(screen.components.any { it is ChipGroupComponent })
        assertTrue(screen.components.any { it is FullScreenBannerComponent })
        assertTrue(screen.components.any { it is MovieCarouselComponent })
        assertTrue(screen.components.any { it is BannerCarouselComponent })
        assertEquals(5, screen.components.size) // Chips + Banner + Top10 + Popular + Category
    }
}
