package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.usecases.GetProfileDataUseCase
import com.diegoferreiracaetano.dlearn.domain.usecases.UpdateProfileDataUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCache
import com.diegoferreiracaetano.dlearn.ui.screens.EditProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.screens.ProfileScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSnackbarType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.getLogger
import kotlin.time.Duration.Companion.minutes

class ProfileOrchestrator(
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val updateProfileDataUseCase: UpdateProfileDataUseCase,
    private val screenBuilder: ProfileScreenBuilder,
    private val editScreenBuilder: EditProfileScreenBuilder
) {
    private val profileCache = InMemoryCache<String, Screen>(5.minutes)

    suspend fun getProfileData(userId: String, appVersion: Int, lang: String): Screen {
        return profileCache.getOrPut("$userId-$appVersion-$lang") {
            val domainData = getProfileDataUseCase.execute(userId)
            screenBuilder.build(domainData, appVersion, lang)
        }
    }

    suspend fun getEditProfileData(userId: String, lang: String): Screen {
        val domainData = getProfileDataUseCase.execute(userId)
        return editScreenBuilder.build(domainData, lang)
    }

    suspend fun updateProfile(userId: String, data: Map<String, String>, lang: String): Screen {
        return try {
            val domainData = updateProfileDataUseCase.execute(userId, data)
            profileCache.clear()
            editScreenBuilder.build(
                data = domainData,
                lang = lang,
                status = AppStringType.UPDATE_PROFILE_SUCCESS,
                type = AppSnackbarType.SUCCESS
            )
        } catch (e: Exception) {
            getLogger().d("Error Profile", e.message.toString())
            val domainData = getProfileDataUseCase.execute(userId)
            editScreenBuilder.build(
                data = domainData,
                lang = lang,
                status = AppStringType.UPDATE_PROFILE_ERROR,
                type = AppSnackbarType.ERROR
            )
        }
    }
}
