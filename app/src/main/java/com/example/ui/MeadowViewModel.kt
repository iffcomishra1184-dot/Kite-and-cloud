package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.audio.AmbientSoundEngine
import com.example.audio.StoryVoiceNarrator
import com.example.data.GardenPlantEntity
import com.example.data.MeadowDatabase
import com.example.data.MeadowDateUtils
import com.example.data.MoodEntryEntity
import com.example.data.MoodRepository
import com.example.data.PrebuiltContentLibrary
import com.example.data.SettingsRepository
import com.example.data.UserSettings
import com.example.model.CartoonStoryVideo
import com.example.model.DailyDiscoveryPack
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MeadowUiState(
    val todayIso: String = MeadowDateUtils.todayIso(),
    val todayEntry: MoodEntryEntity? = null,
    val allEntries: List<MoodEntryEntity> = emptyList(),
    val gardenPlants: List<GardenPlantEntity> = emptyList(),
    val settings: UserSettings = UserSettings(),
    val dailyDiscovery: DailyDiscoveryPack = PrebuiltContentLibrary.dailyDiscoveryPacks[0],
    val draftEmotionIds: List<String> = emptyList(),
    val draftNote: String = "",
    val selectedDateForCheckIn: String = MeadowDateUtils.todayIso(),
    val maxLimitNotice: Boolean = false,
    val lastSavedStory: CartoonStoryVideo? = null,
    val lastSavedStickerId: String? = null
)

class MeadowViewModel(application: Application) : AndroidViewModel(application) {
    private val db = MeadowDatabase.getInstance(application)
    private val moodRepo = MoodRepository(db.appDao())
    private val settingsRepo = SettingsRepository(application)
    val soundEngine = AmbientSoundEngine()
    val voiceNarrator = StoryVoiceNarrator(application)

    private val selectedCheckInDate = MutableStateFlow(MeadowDateUtils.todayIso())
    private val draftIds = MutableStateFlow<List<String>>(emptyList())
    private val draftNote = MutableStateFlow("")
    private val maxLimitNotice = MutableStateFlow(false)
    private val lastSavedStory = MutableStateFlow<CartoonStoryVideo?>(null)
    private val lastSavedStickerId = MutableStateFlow<String?>(null)

    private val baseDataFlow = combine(
        moodRepo.allMoodEntries,
        moodRepo.allGardenPlants,
        settingsRepo.settingsFlow,
        selectedCheckInDate
    ) { entries, plants, settings, checkInDate ->
        Quadruple(entries, plants, settings, checkInDate)
    }

    private val draftFlow = combine(
        draftIds,
        draftNote,
        maxLimitNotice,
        lastSavedStory,
        lastSavedStickerId
    ) { ids, note, limitNotice, story, sticker ->
        DraftTuple(ids, note, limitNotice, story, sticker)
    }

    val uiState: StateFlow<MeadowUiState> = combine(baseDataFlow, draftFlow) { base, draft ->
        val today = MeadowDateUtils.todayIso()
        val todayEntry = base.entries.find { it.dateIso == today }
        val avoidIndex = if (base.settings.lastDiscoveryDate != today && base.settings.lastDiscoveryIndex >= 0) {
            base.settings.lastDiscoveryIndex
        } else {
            null
        }
        val discovery = PrebuiltContentLibrary.discoveryForDate(
            dayOfYear = MeadowDateUtils.dayOfYear(),
            avoidDayIndex = avoidIndex
        )
        soundEngine.updateSettings(base.settings.soundEnabled, base.settings.musicVolume)

        MeadowUiState(
            todayIso = today,
            todayEntry = todayEntry,
            allEntries = base.entries,
            gardenPlants = base.plants,
            settings = base.settings,
            dailyDiscovery = discovery,
            draftEmotionIds = draft.ids,
            draftNote = draft.note,
            selectedDateForCheckIn = base.checkInDate,
            maxLimitNotice = draft.limitNotice,
            lastSavedStory = draft.story ?: todayEntry?.let { entry ->
                PrebuiltContentLibrary.storyForEmotions(
                    entry.emotionIdsCsv.split(","),
                    MeadowDateUtils.dayOfYear()
                )
            },
            lastSavedStickerId = draft.sticker ?: todayEntry?.stickerId
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MeadowUiState()
    )

    init {
        viewModelScope.launch {
            settingsRepo.settingsFlow.collect { settings ->
                val today = MeadowDateUtils.todayIso()
                if (settings.lastDiscoveryDate != today) {
                    val avoid = if (settings.lastDiscoveryIndex >= 0) settings.lastDiscoveryIndex else null
                    val pack = PrebuiltContentLibrary.discoveryForDate(MeadowDateUtils.dayOfYear(), avoid)
                    settingsRepo.recordDailyRotation(today, pack.dayIndex)
                }
            }
        }
    }

    fun startCheckInForDate(dateIso: String = MeadowDateUtils.todayIso()) {
        selectedCheckInDate.value = dateIso
        val existing = uiState.value.allEntries.find { it.dateIso == dateIso }
        draftIds.value = existing?.emotionIdsCsv
            ?.split(",")
            ?.filter { it.isNotBlank() }
            ?.take(4)
            ?: emptyList()
        draftNote.value = existing?.note ?: ""
        maxLimitNotice.value = false
    }

    fun toggleEmotionSelection(emotionId: String) {
        val current = draftIds.value
        if (emotionId in current) {
            draftIds.value = current - emotionId
            maxLimitNotice.value = false
            soundEngine.playSoftPop()
        } else if (current.size < 4) {
            draftIds.value = current + emotionId
            maxLimitNotice.value = false
            soundEngine.playSoftPop()
        } else {
            maxLimitNotice.value = true
        }
    }

    fun dismissMaxLimitNotice() {
        maxLimitNotice.value = false
    }

    fun updateDraftNote(note: String) {
        draftNote.value = note.take(180)
    }

    fun saveCheckIn(onSaved: () -> Unit) {
        val ids = draftIds.value
        if (ids.isEmpty()) return
        val dateIso = selectedCheckInDate.value
        val daySeed = MeadowDateUtils.dayOfYear() + dateIso.hashCode()
        val stickerId = moodRepo.pickStickerForEmotions(ids, daySeed)
        val story = PrebuiltContentLibrary.storyForEmotions(ids, daySeed)

        viewModelScope.launch {
            moodRepo.saveDailyEmotions(dateIso, ids, draftNote.value, stickerId)
            settingsRepo.unlockSticker(stickerId)
            lastSavedStory.value = story
            lastSavedStickerId.value = stickerId
            soundEngine.playGentleChime()
            onSaved()
        }
    }

    fun deleteEntryForDate(dateIso: String) {
        viewModelScope.launch {
            moodRepo.deleteMoodEntry(dateIso)
        }
    }

    fun plantThoughtFlower(flowerType: String, reflectionText: String) {
        val cleaned = reflectionText.trim()
        if (cleaned.isEmpty()) return
        viewModelScope.launch {
            moodRepo.plantThoughtFlower(flowerType, cleaned, MeadowDateUtils.todayIso())
            settingsRepo.unlockSticker("cozy_sprout")
            soundEngine.playGentleChime()
        }
    }

    fun markTinyResetCompleted() {
        viewModelScope.launch {
            settingsRepo.incrementTinyResets()
            settingsRepo.unlockSticker("calm_cloud")
            soundEngine.playGentleChime()
        }
    }

    fun markDailyChallengeDone() {
        viewModelScope.launch {
            settingsRepo.markChallengeDone(MeadowDateUtils.todayIso())
            soundEngine.playGentleChime()
        }
    }

    fun openDailySurprise(stickerId: String) {
        viewModelScope.launch {
            settingsRepo.markSurpriseOpened(MeadowDateUtils.todayIso(), stickerId)
            soundEngine.playGentleChime()
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        if (!enabled) voiceNarrator.stop()
        viewModelScope.launch { settingsRepo.setSoundEnabled(enabled) }
    }

    fun setVoiceNarrationEnabled(enabled: Boolean) {
        if (!enabled) voiceNarrator.stop()
        viewModelScope.launch { settingsRepo.setVoiceNarrationEnabled(enabled) }
    }

    fun setMusicVolume(volume: Float) {
        viewModelScope.launch { settingsRepo.setMusicVolume(volume) }
    }

    fun setSubtitlesEnabled(enabled: Boolean) {
        viewModelScope.launch { settingsRepo.setSubtitlesEnabled(enabled) }
    }

    fun setReducedMotion(enabled: Boolean) {
        viewModelScope.launch { settingsRepo.setReducedMotion(enabled) }
    }

    fun setHighContrast(enabled: Boolean) {
        viewModelScope.launch { settingsRepo.setHighContrast(enabled) }
    }

    fun setLargerText(enabled: Boolean) {
        viewModelScope.launch { settingsRepo.setLargerText(enabled) }
    }

    fun clearAllMoodEntries() {
        viewModelScope.launch { moodRepo.clearAllMoodEntries() }
    }

    fun clearThoughtGarden() {
        viewModelScope.launch { moodRepo.clearAllPlants() }
    }

    fun resetAllAppData() {
        viewModelScope.launch {
            soundEngine.stopAll()
            voiceNarrator.stop()
            moodRepo.clearAllMoodEntries()
            moodRepo.clearAllPlants()
            settingsRepo.resetAllPreferences()
            draftIds.value = emptyList()
            draftNote.value = ""
            lastSavedStory.value = null
            lastSavedStickerId.value = null
        }
    }

    override fun onCleared() {
        soundEngine.stopAll()
        voiceNarrator.release()
        super.onCleared()
    }
}

private data class Quadruple<A, B, C, D>(
    val entries: A,
    val plants: B,
    val settings: C,
    val checkInDate: D
)

private data class DraftTuple(
    val ids: List<String>,
    val note: String,
    val limitNotice: Boolean,
    val story: CartoonStoryVideo?,
    val sticker: String?
)
