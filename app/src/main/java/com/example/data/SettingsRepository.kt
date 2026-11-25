package com.example.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "meadow_prefs")

data class UserSettings(
    val soundEnabled: Boolean = true,
    val voiceNarrationEnabled: Boolean = true,
    val musicVolume: Float = 0.6f,
    val subtitlesEnabled: Boolean = true,
    val reducedMotion: Boolean = false,
    val highContrast: Boolean = false,
    val largerText: Boolean = false,
    val unlockedStickers: Set<String> = emptySet(),
    val completedChallengeDates: Set<String> = emptySet(),
    val surpriseOpenedDates: Set<String> = emptySet(),
    val tinyResetsCompleted: Int = 0,
    val lastDiscoveryDate: String = "",
    val lastDiscoveryIndex: Int = -1
)

class SettingsRepository(private val context: Context) {
    private object Keys {
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VOICE_NARRATION_ENABLED = booleanPreferencesKey("voice_narration_enabled")
        val MUSIC_VOLUME = floatPreferencesKey("music_volume")
        val SUBTITLES_ENABLED = booleanPreferencesKey("subtitles_enabled")
        val REDUCED_MOTION = booleanPreferencesKey("reduced_motion")
        val HIGH_CONTRAST = booleanPreferencesKey("high_contrast")
        val LARGER_TEXT = booleanPreferencesKey("larger_text")
        val UNLOCKED_STICKERS = stringSetPreferencesKey("unlocked_stickers")
        val COMPLETED_CHALLENGES = stringSetPreferencesKey("completed_challenges")
        val SURPRISE_OPENED = stringSetPreferencesKey("surprise_opened")
        val TINY_RESETS_COUNT = intPreferencesKey("tiny_resets_count")
        val LAST_DISCOVERY_DATE = stringPreferencesKey("last_discovery_date")
        val LAST_DISCOVERY_INDEX = intPreferencesKey("last_discovery_index")
    }

    val settingsFlow: Flow<UserSettings> = context.dataStore.data.map { prefs ->
        UserSettings(
            soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true,
            voiceNarrationEnabled = prefs[Keys.VOICE_NARRATION_ENABLED] ?: true,
            musicVolume = prefs[Keys.MUSIC_VOLUME] ?: 0.6f,
            subtitlesEnabled = prefs[Keys.SUBTITLES_ENABLED] ?: true,
            reducedMotion = prefs[Keys.REDUCED_MOTION] ?: false,
            highContrast = prefs[Keys.HIGH_CONTRAST] ?: false,
            largerText = prefs[Keys.LARGER_TEXT] ?: false,
            unlockedStickers = prefs[Keys.UNLOCKED_STICKERS] ?: emptySet(),
            completedChallengeDates = prefs[Keys.COMPLETED_CHALLENGES] ?: emptySet(),
            surpriseOpenedDates = prefs[Keys.SURPRISE_OPENED] ?: emptySet(),
            tinyResetsCompleted = prefs[Keys.TINY_RESETS_COUNT] ?: 0,
            lastDiscoveryDate = prefs[Keys.LAST_DISCOVERY_DATE] ?: "",
            lastDiscoveryIndex = prefs[Keys.LAST_DISCOVERY_INDEX] ?: -1
        )
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SOUND_ENABLED] = enabled }
    }

    suspend fun setVoiceNarrationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.VOICE_NARRATION_ENABLED] = enabled }
    }

    suspend fun setMusicVolume(volume: Float) {
        context.dataStore.edit { it[Keys.MUSIC_VOLUME] = volume.coerceIn(0f, 1f) }
    }

    suspend fun setSubtitlesEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SUBTITLES_ENABLED] = enabled }
    }

    suspend fun setReducedMotion(enabled: Boolean) {
        context.dataStore.edit { it[Keys.REDUCED_MOTION] = enabled }
    }

    suspend fun setHighContrast(enabled: Boolean) {
        context.dataStore.edit { it[Keys.HIGH_CONTRAST] = enabled }
    }

    suspend fun setLargerText(enabled: Boolean) {
        context.dataStore.edit { it[Keys.LARGER_TEXT] = enabled }
    }

    suspend fun unlockSticker(stickerId: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.UNLOCKED_STICKERS] ?: emptySet()
            prefs[Keys.UNLOCKED_STICKERS] = current + stickerId
        }
    }

    suspend fun markChallengeDone(dateIso: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.COMPLETED_CHALLENGES] ?: emptySet()
            prefs[Keys.COMPLETED_CHALLENGES] = current + dateIso
        }
    }

    suspend fun markSurpriseOpened(dateIso: String, stickerId: String) {
        context.dataStore.edit { prefs ->
            val opened = prefs[Keys.SURPRISE_OPENED] ?: emptySet()
            prefs[Keys.SURPRISE_OPENED] = opened + dateIso
            val stickers = prefs[Keys.UNLOCKED_STICKERS] ?: emptySet()
            prefs[Keys.UNLOCKED_STICKERS] = stickers + stickerId
        }
    }

    suspend fun incrementTinyResets() {
        context.dataStore.edit { prefs ->
            prefs[Keys.TINY_RESETS_COUNT] = (prefs[Keys.TINY_RESETS_COUNT] ?: 0) + 1
        }
    }

    suspend fun recordDailyRotation(dateIso: String, discoveryIndex: Int) {
        context.dataStore.edit { prefs ->
            prefs[Keys.LAST_DISCOVERY_DATE] = dateIso
            prefs[Keys.LAST_DISCOVERY_INDEX] = discoveryIndex
        }
    }

    suspend fun resetAllPreferences() {
        context.dataStore.edit { it.clear() }
    }
}
