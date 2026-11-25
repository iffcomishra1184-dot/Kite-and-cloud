package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Brush
import androidx.compose.material.icons.rounded.CardGiftcard
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.audio.AmbientSoundEngine
import com.example.audio.StoryVoiceNarrator
import com.example.data.MeadowDateUtils
import com.example.ui.MeadowUiState
import com.example.ui.components.AnimatedCartoonBuddy
import com.example.ui.components.CartoonVideoPlayer

@Composable
fun DailyDiscoveriesScreen(
    uiState: MeadowUiState,
    soundEngine: AmbientSoundEngine,
    voiceNarrator: StoryVoiceNarrator,
    onToggleVoice: (Boolean) -> Unit,
    onToggleSubtitles: (Boolean) -> Unit,
    onMarkChallengeDone: () -> Unit,
    onOpenSurprise: (String) -> Unit,
    onOpenDoodle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pack = uiState.dailyDiscovery
    val challengeDoneToday = uiState.todayIso in uiState.settings.completedChallengeDates
    val surpriseOpenedToday = uiState.todayIso in uiState.settings.surpriseOpenedDates

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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column {
                    Text(
                        text = "Daily Discoveries",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Fresh pre-made mix for ${MeadowDateUtils.formatFriendly(uiState.todayIso)} — rotates daily!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Today's featured cartoon character
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.72f),
                    border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AnimatedCartoonBuddy(
                            character = pack.character,
                            mood = "happy",
                            reducedMotion = uiState.settings.reducedMotion,
                            size = 70.dp
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Today's Host: ${pack.character.displayName} (${pack.character.roleTag})",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = pack.characterGreeting,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            }

            // Today's Animated Story Video
            item {
                CartoonVideoPlayer(
                    story = pack.storyVideo,
                    soundEngine = soundEngine,
                    voiceNarrator = voiceNarrator,
                    soundEnabled = uiState.settings.soundEnabled,
                    voiceEnabled = uiState.settings.voiceNarrationEnabled,
                    subtitlesEnabled = uiState.settings.subtitlesEnabled,
                    reducedMotion = uiState.settings.reducedMotion,
                    onToggleVoice = onToggleVoice,
                    onToggleSubtitles = onToggleSubtitles
                )
            }

            // Funny Mini-Story
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFFFFF3CC),
                    border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.MenuBook, contentDescription = null, tint = Color(0xFF7B5600))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Funny Mini-Story: ${pack.funnyMiniStoryTitle}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF282B38)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = pack.funnyMiniStoryBody,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF3E4257)
                        )
                    }
                }
            }

            // Today's Tiny Challenge
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Today's Tiny Challenge: ${pack.tinyChallengeTitle}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = pack.tinyChallengeSteps,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = onMarkChallengeDone,
                            enabled = !challengeDoneToday,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("daily_challenge_done_button"),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = if (challengeDoneToday) Icons.Rounded.CheckCircle else Icons.Rounded.AutoAwesome,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(if (challengeDoneToday) "Completed Today!" else "Mark Tiny Challenge Done")
                        }
                    }
                }
            }

            // Creative Prompt + Gratitude + Breathing Rhythm
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Creative Spark: ${pack.creativePromptTitle}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = pack.creativePromptBody,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = onOpenDoodle,
                            modifier = Modifier.height(48.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Rounded.Brush, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Open Mini Doodle Pad")
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Gratitude Spark: ${pack.gratitudePrompt}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Today's Breath Rhythm: ${pack.breathingTitle} (${pack.breathingRhythm})",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Daily Surprise Box
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.78f),
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.35f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Rounded.CardGiftcard,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Today's Pocket Surprise",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (!surpriseOpenedToday) {
                            Text(
                                text = "A tiny sticker & pocket note tucked away for today.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Button(
                                onClick = { onOpenSurprise(pack.surpriseSticker.id) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("open_daily_surprise_button"),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Unwrap Today's Surprise")
                            }
                        } else {
                            Text(
                                text = "Unlocked: ${pack.surpriseSticker.name}!",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = pack.surpriseNote,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
