package com.diegoferreiracaetano.dlearn.infrastructure.services

interface FeatureToggleService {
    fun isEnabled(feature: Feature): Boolean
}

enum class Feature {
    EXTERNAL_AUTH_SYNC,
    MEMBER_SECTION
}

class InMemoryFeatureToggleService : FeatureToggleService {
    private val toggles = mapOf(
        Feature.EXTERNAL_AUTH_SYNC to true,
        Feature.MEMBER_SECTION to false // Definido como desativado conforme solicitado
    )

    override fun isEnabled(feature: Feature): Boolean {
        return toggles[feature] ?: false
    }
}
