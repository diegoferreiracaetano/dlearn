package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.infrastructure.services.HomeDataService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetHomeDataUseCaseTest {

    private val homeDataService = mockk<HomeDataService>(relaxed = true)
    private val useCase = GetHomeDataUseCase(homeDataService)

    @Test
    fun `given valid parameters when execute is called should return home data from the service`() = runTest {
        val expectedData = mockk<HomeDomainData>()
        coEvery { homeDataService.fetchHomeData("pt-BR", HomeFilterType.ALL, "user1") } returns expectedData

        val result = useCase.execute("pt-BR", HomeFilterType.ALL, "user1")

        assertEquals(expectedData, result)
    }
}
