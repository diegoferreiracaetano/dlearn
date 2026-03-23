package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.usecases.GetProfileDataUseCase
import com.diegoferreiracaetano.dlearn.domain.usecases.UpdateProfileDataUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCache
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.EditProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.screens.ProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSnackbarType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.getLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.minutes

class ProfileOrchestrator(
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val updateProfileDataUseCase: UpdateProfileDataUseCase,
    private val screenBuilder: ProfileScreenBuilder,
    private val editScreenBuilder: EditProfileScreenBuilder
) : Orchestrator {
    private val profileCache = InMemoryCache<String, Screen>(5.minutes)

    override fun execute(
        request: AppRequest,
        header: AppHeader
    ): Flow<Screen> {
        val path = NavigationRoutes.extractPath(request.path)
        val userId = header.userId ?: "guest"
        val language = header.language
        
        return when (path) {
            NavigationRoutes.PROFILE -> getProfileData(userId, header.userAgent.appVersion, language, header.country)
            NavigationRoutes.EDIT_PROFILE -> getEditProfileData(userId, language)
            NavigationRoutes.UPDATE_PROFILE -> updateProfile(userId, request.params ?: emptyMap(), language)
            else -> throw IllegalArgumentException("Invalid profile path: $path")
        }
    }

    private fun getProfileData(userId: String, appVersion: String, lang: String, country: String?): Flow<Screen> = flow {
        val screen = profileCache.getOrPut("$userId-$appVersion-$lang-$country") {
            val domainData = getProfileDataUseCase.execute(userId)
            screenBuilder.build(domainData, lang, country)
        }
        emit(screen)
    }

    private fun getEditProfileData(userId: String, lang: String): Flow<Screen> = flow {
        val domainData = getProfileDataUseCase.execute(userId)
        emit(editScreenBuilder.build(domainData, lang))
    }

    private fun updateProfile(userId: String, data: Map<String, String>, lang: String): Flow<Screen> = flow {
        try {
            val domainData = updateProfileDataUseCase.execute(userId, data)
            profileCache.clear()
            emit(
                editScreenBuilder.build(
                    data = domainData,
                    lang = lang,
                    status = AppStringType.UPDATE_PROFILE_SUCCESS,
                    type = AppSnackbarType.SUCCESS
                )
            )
        } catch (e: Exception) {
            getLogger().d("Error Profile", e.message.toString())
            val domainData = getProfileDataUseCase.execute(userId)
            emit(
                editScreenBuilder.build(
                    data = domainData,
                    lang = lang,
                    status = AppStringType.UPDATE_PROFILE_ERROR,
                    type = AppSnackbarType.ERROR
                )
            )
        }
    }
}
