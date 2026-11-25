package com.example.ui.screens

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.DeleteForever
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.MeadowUiState

@Composable
fun SettingsScreen(
    uiState: MeadowUiState,
    onSoundEnabledChange: (Boolean) -> Unit,
    onVoiceNarrationChange: (Boolean) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onSubtitlesChange: (Boolean) -> Unit,
    onReducedMotionChange: (Boolean) -> Unit,
    onHighContrastChange: (Boolean) -> Unit,
    onLargerTextChange: (Boolean) -> Unit,
    onClearMoodEntries: () -> Unit,
    onClearGarden: () -> Unit,
    onResetAllData: () -> Unit,
    onShowSupportSheet: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    var confirmAction by remember { mutableStateOf<String?>(null) }

    if (confirmAction != null) {
        val (title, body, action) = when (confirmAction) {
            "moods" -> Triple(
                "Clear all mood entries?",
                "This deletes your entire mood calendar history on this device.",
                onClearMoodEntries
            )
            "garden" -> Triple(
                "Clear Thought Garden?",
                "This removes all planted flowers and reflections from your garden.",
                onClearGarden
            )
            else -> Triple(
                "Reset all Pocket Meadow data?",
                "This clears all mood check-ins, flowers, stickers, and preferences on this device.",
                onResetAllData
            )
        }
        AlertDialog(
            onDismissRequest = { confirmAction = null },
            title = { Text(title) },
            text = { Text(body) },
            confirmButton = {
                TextButton(
                    onClick = {
                        action()
                        confirmAction = null
                    }
                ) {
                    Text("Confirm", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmAction = null }) {
                    Text("Cancel")
                }
            }
        )
    }

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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("settings_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Settings & Privacy", style = MaterialTheme.typography.headlineSmall)
                }
            }

            // Sound Settings
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("Sound Settings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        SettingSwitchRow(
                            title = "Gentle Music & Sound Effects",
                            subtitle = "Ambient rain, wind, chimes, and bubble pops",
                            checked = uiState.settings.soundEnabled,
                            onCheckedChange = onSoundEnabledChange,
                            testTag = "switch_sound_enabled"
                        )
                        SettingSwitchRow(
                            title = "Story Voice Narration",
                            subtitle = "Read animated cartoon stories aloud with calm voice",
                            checked = uiState.settings.voiceNarrationEnabled,
                            onCheckedChange = onVoiceNarrationChange,
                            testTag = "switch_voice_narration"
                        )
                        Column {
                            Text(
                                text = "Volume: ${(uiState.settings.musicVolume * 100).toInt()}%",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Slider(
                                value = uiState.settings.musicVolume,
                                onValueChange = onVolumeChange,
                                enabled = uiState.settings.soundEnabled
                            )
                        }
                    }
                }
            }

            // Accessibility Options
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Accessibility", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        SettingSwitchRow(
                            title = "Video Subtitles",
                            subtitle = "Show captions on animated cartoon stories",
                            checked = uiState.settings.subtitlesEnabled,
                            onCheckedChange = onSubtitlesChange,
                            testTag = "switch_subtitles"
                        )
                        SettingSwitchRow(
                            title = "Reduced Motion",
                            subtitle = "Pause floating/bouncing character animations",
                            checked = uiState.settings.reducedMotion,
                            onCheckedChange = onReducedMotionChange,
                            testTag = "switch_reduced_motion"
                        )
                        SettingSwitchRow(
                            title = "Larger Text",
                            subtitle = "Increase readable text size across screens",
                            checked = uiState.settings.largerText,
                            onCheckedChange = onLargerTextChange,
                            testTag = "switch_larger_text"
                        )
                        SettingSwitchRow(
                            title = "High Contrast",
                            subtitle = "Sharper contrast for text and borders",
                            checked = uiState.settings.highContrast,
                            onCheckedChange = onHighContrastChange,
                            testTag = "switch_high_contrast"
                        )
                    }
                }
            }

            // Privacy & Data Control
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Rounded.Lock, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Privacy & Reset", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Text(
                            text = "Pocket Meadow stores your check-ins strictly on this device. No account, no cloud sharing, no AI tracking, and no diagnosis labels.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        OutlinedButton(
                            onClick = { confirmAction = "moods" },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Delete All Mood Entries")
                        }
                        OutlinedButton(
                            onClick = { confirmAction = "garden" },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Text("Clear Thought Garden Flowers")
                        }
                        Button(
                            onClick = { confirmAction = "all" },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("reset_all_data_button"),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Icon(Icons.Rounded.DeleteForever, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reset All App Data")
                        }
                    }
                }
            }

            // Discreet Trusted Adult / Crisis Support
            item {
                Surface(
                    onClick = onShowSupportSheet,
                    modifier = Modifier.fillMaxWidth().testTag("settings_support_card"),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.SupportAgent, contentDescription = null, tint = MaterialTheme.colorScheme.onTertiaryContainer)
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Trusted Adult & Support Resources",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onTertiaryContainer
                            )
                            Text(
                                text = "Discreet guidance and confidential teen helplines whenever an app isn't enough.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag)
        )
    }
}

@Composable
fun SupportResourcesDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("You Don't Have to Carry Heavy Things Alone") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Pocket Meadow is for everyday calm and small breaks—not medical care. If feelings get too heavy or unsafe, talking to a trusted person really helps.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "• Trusted Adult: A parent, school counselor, teacher, coach, or older relative you feel safe around.",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "• US / Canada: Call or text 988 (Suicide & Crisis Lifeline) — free, 24/7, confidential.",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "• Crisis Text Line: Text HOME to 741741 (US/CA) or 85258 (UK).",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "• India: Tele-MANAS 14416 or Childline 1098 (free 24/7 support).",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "• Elsewhere: Check findahelpline.com with a trusted adult or call local emergency services if you're in immediate danger.",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
