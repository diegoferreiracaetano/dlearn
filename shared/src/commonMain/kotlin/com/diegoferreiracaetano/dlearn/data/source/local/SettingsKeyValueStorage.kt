@file:OptIn(com.russhwolf.settings.ExperimentalSettingsApi::class)

package com.diegoferreiracaetano.dlearn.data.source.local

import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.coroutines.FlowSettings
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow

@Suppress("UNCHECKED_CAST")
class SettingsKeyValueStorage(
    private val settings: Settings,
) : KeyValueStorage {
    private val flowSettings: FlowSettings = (settings as ObservableSettings).toFlowSettings()

    override fun <T : Any> get(
        key: String,
        defaultValue: T,
    ): T =
        when (defaultValue) {
            is String -> settings.getString(key, defaultValue) as T
            is Boolean -> settings.getBoolean(key, defaultValue) as T
            is Int -> settings.getInt(key, defaultValue) as T
            is Long -> settings.getLong(key, defaultValue) as T
            is Float -> settings.getFloat(key, defaultValue) as T
            is Double -> settings.getDouble(key, defaultValue) as T
            else -> throw IllegalArgumentException("Unsupported type")
        }

    override fun <T : Any> getFlow(
        key: String,
        defaultValue: T,
    ): Flow<T> =
        when (defaultValue) {
            is String -> flowSettings.getStringFlow(key, defaultValue) as Flow<T>
            is Boolean -> flowSettings.getBooleanFlow(key, defaultValue) as Flow<T>
            is Int -> flowSettings.getIntFlow(key, defaultValue) as Flow<T>
            is Long -> flowSettings.getLongFlow(key, defaultValue) as Flow<T>
            is Float -> flowSettings.getFloatFlow(key, defaultValue) as Flow<T>
            is Double -> flowSettings.getDoubleFlow(key, defaultValue) as Flow<T>
            else -> throw IllegalArgumentException("Unsupported type")
        }

    override fun <T : Any> put(
        key: String,
        value: T,
    ) {
        when (value) {
            is String -> settings.putString(key, value)
            is Boolean -> settings.putBoolean(key, value)
            is Int -> settings.putInt(key, value)
            is Long -> settings.putLong(key, value)
            is Float -> settings.putFloat(key, value)
            is Double -> settings.putDouble(key, value)
            else -> throw IllegalArgumentException("Unsupported type")
        }
    }

    override fun remove(key: String) {
        settings.remove(key)
    }

    override fun clear() {
        settings.clear()
    }
}
