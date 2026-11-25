package com.example.data

import com.example.model.EmotionCatalog
import com.example.model.EmotionGroup
import kotlinx.coroutines.flow.Flow

class MoodRepository(private val dao: AppDao) {
    val allMoodEntries: Flow<List<MoodEntryEntity>> = dao.observeAllMoodEntries()
    val allGardenPlants: Flow<List<GardenPlantEntity>> = dao.observeAllPlants()

    fun observeMoodForDate(dateIso: String): Flow<MoodEntryEntity?> =
        dao.observeMoodEntry(dateIso)

    suspend fun saveDailyEmotions(
        dateIso: String,
        selectedIds: List<String>,
        note: String,
        stickerId: String?
    ) {
        val clamped = selectedIds
            .mapNotNull { EmotionCatalog.byId(it)?.id }
            .distinct()
            .take(4)
        if (clamped.isEmpty()) return

        val entry = MoodEntryEntity(
            dateIso = dateIso,
            emotionIdsCsv = clamped.joinToString(","),
            note = note.trim().take(180),
            stickerId = stickerId,
            updatedAt = System.currentTimeMillis()
        )
        dao.upsertMoodEntry(entry)
    }

    suspend fun deleteMoodEntry(dateIso: String) {
        dao.deleteMoodEntry(dateIso)
    }

    suspend fun clearAllMoodEntries() {
        dao.deleteAllMoodEntries()
    }

    suspend fun plantThoughtFlower(flowerType: String, reflectionText: String, dateIso: String) {
        dao.insertPlant(
            GardenPlantEntity(
                flowerType = flowerType,
                reflectionText = reflectionText.trim().take(160),
                plantedDateIso = dateIso
            )
        )
    }

    suspend fun clearAllPlants() {
        dao.deleteAllPlants()
    }

    fun pickStickerForEmotions(selectedIds: List<String>, daySeed: Int): String {
        val emotions = selectedIds.mapNotNull { EmotionCatalog.byId(it) }
        val hasBright = emotions.any { it.group == EmotionGroup.BRIGHT }
        val pool = if (hasBright) {
            listOf("sunbeam_star", "sparkle_note", "teacup_pal", "lantern_keeper")
        } else {
            listOf("paper_boat", "calm_cloud", "kite_rider", "cozy_sprout")
        }
        return pool[Math.floorMod(daySeed, pool.size)]
    }
}
