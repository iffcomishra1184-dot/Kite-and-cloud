package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.audio.AmbientSoundEngine
import com.example.ui.MeadowUiState

enum class RelaxTab(val label: String) {
    TINY_RESET("Tiny Reset"),
    BUBBLE_POP("Bubble Pop"),
    BREATHING("Breathing"),
    THOUGHT_GARDEN("Thought Garden"),
    MINI_DOODLE("Mini Doodle"),
    SOUND_CORNER("Sound Corner"),
    COMPANION("Mood Companion")
}

@Composable
fun RelaxZoneScreen(
    uiState: MeadowUiState,
    soundEngine: AmbientSoundEngine,
    selectedTab: RelaxTab,
    onSelectTab: (RelaxTab) -> Unit,
    onCompletedTinyReset: () -> Unit,
    onPlantFlower: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 700.dp),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Relax Zone",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Low-pressure interactive breaks whenever your brain needs air.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RelaxTab.entries.forEach { tab ->
                        FilterChip(
                            selected = selectedTab == tab,
                            onClick = { onSelectTab(tab) },
                            label = { Text(tab.label) },
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("relax_tab_${tab.name.lowercase()}")
                        )
                    }
                }
            }

            item {
                when (selectedTab) {
                    RelaxTab.TINY_RESET -> TinyResetSection(
                        soundEngine = soundEngine,
                        reducedMotion = uiState.settings.reducedMotion,
                        completedCount = uiState.settings.tinyResetsCompleted,
                        onCompletedReset = onCompletedTinyReset
                    )
                    RelaxTab.BUBBLE_POP -> BubblePopActivity(
                        soundEngine = soundEngine,
                        reducedMotion = uiState.settings.reducedMotion
                    )
                    RelaxTab.BREATHING -> BreathingAnimationActivity(
                        reducedMotion = uiState.settings.reducedMotion
                    )
                    RelaxTab.THOUGHT_GARDEN -> ThoughtGardenActivity(
                        plants = uiState.gardenPlants,
                        onPlantFlower = onPlantFlower
                    )
                    RelaxTab.MINI_DOODLE -> MiniDoodleActivity()
                    RelaxTab.SOUND_CORNER -> SoundCornerActivity(
                        soundEngine = soundEngine,
                        soundEnabled = uiState.settings.soundEnabled
                    )
                    RelaxTab.COMPANION -> MoodCompanionActivity(
                        soundEngine = soundEngine,
                        reducedMotion = uiState.settings.reducedMotion
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
