package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.ProfileMapper
import com.diegoferreiracaetano.dlearn.ui.screens.EditProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.screens.ProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSnackbarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSnackbarType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class ProfileAppOrchestratorTest {

    private val service = StubProfileDataService()
    private val getUseCase = GetProfileDataUseCase(service)
    private val updateUseCase = UpdateProfileDataUseCase(service)
    private val i18n = StubI18nProvider()
    private val mapper = ProfileMapper(i18n)
    private val profileBuilder = ProfileScreenBuilder(mapper, i18n)
    private val editBuilder = EditProfileScreenBuilder(mapper, i18n)

    private lateinit var orchestrator: ProfileOrchestrator

    @Before
    fun setup() {
        orchestrator = ProfileOrchestrator(
            getUseCase,
            updateUseCase,
            profileBuilder,
            editBuilder
        )
    }

    @Test
    fun `updateProfile should return success snackbar when successful`() = runTest {
        val userId = "user123"
        val lang = "pt"
        val updateData = mapOf("full_name" to "Diego Novo")

        val screen = orchestrator.updateProfile(userId, updateData, lang)

        val container = screen.components.first() as AppContainerComponent
        val snackbar = container.components.filterIsInstance<AppSnackbarComponent>().firstOrNull()
        
        assertNotNull(snackbar)
        assertEquals(AppSnackbarType.SUCCESS, snackbar.snackbarType)
    }

    @Test
    fun `updateProfile should return error snackbar when exception occurs`() = runTest {
        val userId = "user123"
        val lang = "pt"
        val updateData = mapOf("force_error" to "true")

        val screen = orchestrator.updateProfile(userId, updateData, lang)

        val container = screen.components.first() as AppContainerComponent
        val snackbar = container.components.filterIsInstance<AppSnackbarComponent>().firstOrNull()

        assertNotNull(snackbar)
        assertEquals(AppSnackbarType.ERROR, snackbar.snackbarType)
    }

    private class StubProfileDataService : ProfileDataService() {
        override fun fetchProfileData(userId: String): ProfileDomainData {
            return ProfileDomainData(id = userId, name = "Test", email = "test@test.com")
        }

        override fun updateProfileData(userId: String, updates: Map<String, String>): ProfileDomainData {
            if (updates.containsKey("force_error")) throw RuntimeException("Simulated Error")
            return ProfileDomainData(id = userId, name = updates["full_name"] ?: "Test", email = "test@test.com")
        }
    }

    private class StubI18nProvider : I18nProvider() {
        override fun getString(key: com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType, language: String): String {
            return "Translated: ${key.name}"
        }
    }
}
