package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.data.cache.CacheStrategy
import com.diegoferreiracaetano.dlearn.data.cache.toCache
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.EditProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.screens.ProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.getLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProfileOrchestrator(
    private val userRepository: UserRepository,
    private val screenBuilder: ProfileScreenBuilder,
    private val editScreenBuilder: EditProfileScreenBuilder
) : Orchestrator {

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

    private fun getProfileData(userId: String, appVersion: String, lang: String, country: String?): Flow<Screen> {
        val cacheKey = "profile_${userId}_${appVersion}_${lang}_${country}"
        return flow {
            val user = userRepository.findById(userId) ?: throw Exception("User not found")
            val screen = screenBuilder.build(user, lang, country)
            emit(screen)
        }.toCache(
            key = cacheKey,
            strategy = CacheStrategy.CACHE_FIRST
        )
    }

    private fun getEditProfileData(userId: String, lang: String): Flow<Screen> = flow {
        val user = userRepository.findById(userId) ?: throw Exception("User not found")
        emit(editScreenBuilder.build(user, lang))
    }

    private fun updateProfile(userId: String, lang: String): Flow<Screen> = flow {
        try {
            val user = userRepository.findById(userId) ?: throw Exception("User not found")
            // Nota: Para invalidar cache no novo sistema, poderíamos ter um método clear no CacheManager
            // Mas para o update, emitimos o novo estado diretamente
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
