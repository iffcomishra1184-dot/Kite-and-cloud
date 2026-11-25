package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM mood_entries ORDER BY dateIso DESC")
    fun observeAllMoodEntries(): Flow<List<MoodEntryEntity>>

    @Query("SELECT * FROM mood_entries WHERE dateIso = :dateIso LIMIT 1")
    fun observeMoodEntry(dateIso: String): Flow<MoodEntryEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMoodEntry(entry: MoodEntryEntity)

    @Query("DELETE FROM mood_entries WHERE dateIso = :dateIso")
    suspend fun deleteMoodEntry(dateIso: String)

    @Query("DELETE FROM mood_entries")
    suspend fun deleteAllMoodEntries()

    @Query("SELECT * FROM garden_plants ORDER BY createdAt DESC")
    fun observeAllPlants(): Flow<List<GardenPlantEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlant(plant: GardenPlantEntity)

    @Query("DELETE FROM garden_plants")
    suspend fun deleteAllPlants()
}
