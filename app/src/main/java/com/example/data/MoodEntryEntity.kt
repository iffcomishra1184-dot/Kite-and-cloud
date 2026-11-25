package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_entries")
data class MoodEntryEntity(
    @PrimaryKey val dateIso: String,
    val emotionIdsCsv: String,
    val note: String = "",
    val stickerId: String? = null,
    val updatedAt: Long = System.currentTimeMillis()
)
