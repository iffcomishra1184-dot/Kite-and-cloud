package com.example

import com.example.data.PrebuiltContentLibrary
import com.example.model.EmotionCatalog
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class MeadowLogicUnitTest {

    @Test
    fun catalog_containsAllTwentyRequiredEmotions() {
        val required = listOf(
            "happy", "excited", "calm", "grateful", "proud", "hopeful", "loved",
            "confident", "tired", "stressed", "anxious", "overwhelmed", "sad",
            "lonely", "irritated", "angry", "confused", "disappointed", "nervous",
            "unmotivated"
        )
        val ids = EmotionCatalog.all.map { it.id }
        required.forEach { id ->
            assertTrue("Missing emotion: $id", id in ids)
        }
        assertEquals(20, EmotionCatalog.all.size)
    }

    @Test
    fun fromCsv_clampsToMaxFourUniqueEmotions() {
        val parsed = EmotionCatalog.fromCsv("happy,calm,proud,grateful,excited,sad")
        assertEquals(4, parsed.size)
        assertEquals(listOf("happy", "calm", "proud", "grateful"), parsed.map { it.id })
    }

    @Test
    fun storyRouting_selectsComfortForDifficultAndCelebrationForBright() {
        val comfort = PrebuiltContentLibrary.storyForEmotions(listOf("anxious", "overwhelmed"), 1)
        assertTrue(comfort.isComfortStory)
        assertTrue(comfort.scenes.sumOf { it.durationSeconds } in 30..90)

        val celebration = PrebuiltContentLibrary.storyForEmotions(listOf("happy", "proud"), 1)
        assertFalse(celebration.isComfortStory)
        assertTrue(celebration.scenes.sumOf { it.durationSeconds } in 30..90)
    }

    @Test
    fun dailyDiscovery_avoidsRepeatingPreviousDayIndex() {
        val first = PrebuiltContentLibrary.discoveryForDate(dayOfYear = 12, avoidDayIndex = null)
        val next = PrebuiltContentLibrary.discoveryForDate(dayOfYear = 12, avoidDayIndex = first.dayIndex)
        assertNotEquals(first.dayIndex, next.dayIndex)
    }
}
