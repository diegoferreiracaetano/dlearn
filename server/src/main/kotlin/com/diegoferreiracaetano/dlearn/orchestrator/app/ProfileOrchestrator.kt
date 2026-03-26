package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCache
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.EditProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.screens.ProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.getLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.minutes

class ProfileOrchestrator(
    private val userRepository: UserRepository,
    private val screenBuilder: ProfileScreenBuilder,
    private val editScreenBuilder: EditProfileScreenBuilder
) : Orchestrator {
    private val profileCache = InMemoryCache<String, Screen>(5.minutes)

    override fun execute(
        request: AppRequest,
        header: AppHeader
    ): Flow<Screen> {
        val path = AppPath.parse(request.path).path
        val language = header.language
        val userId = header.userId
        
        return when (path) {
            AppNavigationRoute.PROFILE -> getProfileData(userId, header.userAgent.appVersion, language, header.country)
            AppNavigationRoute.PROFILE_EDIT -> getEditProfileData(userId, language)
            AppNavigationRoute.PROFILE_UPDATE -> updateProfile(userId, language)
            else -> throw IllegalArgumentException("Invalid profile path: $path")
        }
    }

    private fun getProfileData(userId: String, appVersion: String, lang: String, country: String?): Flow<Screen> = flow {
        val screen = profileCache.getOrPut("$userId-$appVersion-$lang-$country") {
            val user = userRepository.findById(userId) ?: throw Exception("User not found")
            screenBuilder.build(user, lang, country)
        }
        emit(screen)
    }

    private fun getEditProfileData(userId: String, lang: String): Flow<Screen> = flow {
        val user = userRepository.findById(userId) ?: throw Exception("User not found")
        emit(editScreenBuilder.build(user, lang))
    }

    private fun updateProfile(userId: String, lang: String): Flow<Screen> = flow {
        try {
            val user = userRepository.findById(userId) ?: throw Exception("User not found")
            profileCache.clear()
            emit(
                editScreenBuilder.build(
                    data = user,
                    lang = lang,
                    status = AppStringType.UPDATE_PROFILE_SUCCESS,
                    type = AppSnackbarType.SUCCESS
                )
            )
        } catch (e: Exception) {
            getLogger().d("Error Profile", e.message.toString())
            val user = userRepository.findById(userId) ?: throw Exception("User not found")
            emit(
                editScreenBuilder.build(
                    data = user,
                    lang = lang,
                    status = AppStringType.UPDATE_PROFILE_ERROR,
                    type = AppSnackbarType.ERROR
                )
            )
        }
    }
}
