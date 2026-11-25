package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MoodEntryEntity::class, GardenPlantEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MeadowDatabase : RoomDatabase() {
    abstract fun appDao(): AppDao

    companion object {
        @Volatile
        private var INSTANCE: MeadowDatabase? = null

        fun getInstance(context: Context): MeadowDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    MeadowDatabase::class.java,
                    "pocket_meadow.db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
