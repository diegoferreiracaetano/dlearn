package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.MovieDetailMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMovieDetailHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MovieDetailScreenBuilderTest {

    private val mapper = mockk<MovieDetailMapper>(relaxed = true)
    private val builder = MovieDetailScreenBuilder(mapper)

    @Test
    fun `given movie detail domain data when build is called should return screen with container and header components`() {
        val data = mockk<MovieDetailDomainData>(relaxed = true) {
            every { title } returns "Inception"
        }
        every { mapper.toHeader(any(), any()) } returns mockk<AppMovieDetailHeaderComponent>()

        val screen = builder.build(data, "en")

        val container = screen.components.first() as AppContainerComponent
        val topBar = container.topBar as AppTopBarComponent
        assertEquals("Inception", topBar.title)
        assertTrue(container.components.any { it is AppMovieDetailHeaderComponent })
    }
}
