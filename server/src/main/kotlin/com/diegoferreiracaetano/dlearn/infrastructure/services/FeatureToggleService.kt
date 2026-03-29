package com.diegoferreiracaetano.dlearn.infrastructure.services

interface FeatureToggleService {
    fun isEnabled(feature: Feature): Boolean
}

enum class Feature {
    EXTERNAL_AUTH_SYNC
}

class InMemoryFeatureToggleService : FeatureToggleService {
    private val toggles = mapOf(
        Feature.EXTERNAL_AUTH_SYNC to true
    )

    override fun isEnabled(feature: Feature): Boolean {
        return toggles[feature] ?: false
    }
}
