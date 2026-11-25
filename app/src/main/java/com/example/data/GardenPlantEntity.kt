package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "garden_plants")
data class GardenPlantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val flowerType: String,
    val reflectionText: String,
    val plantedDateIso: String,
    val createdAt: Long = System.currentTimeMillis()
)
