package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.MeadowDateUtils
import com.example.model.EmotionCatalog
import com.example.model.EmotionItem
import com.example.ui.MeadowUiState
import com.example.ui.components.CartoonEmotionFace

@Composable
fun EmotionCheckInScreen(
    uiState: MeadowUiState,
    onToggleEmotion: (String) -> Unit,
    onNoteChanged: (String) -> Unit,
    onSaveCheckIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedIds = uiState.draftEmotionIds

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 700.dp)
        ) {
            // Header & 4-emotion limit indicator
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Column(modifier = Modifier.padding(horizontal = 18.dp, vertical = 14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Emotion Check-In",
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "For ${MeadowDateUtils.formatFriendly(uiState.selectedDateForCheckIn)} • Pick up to 4",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (selectedIds.size == 4) {
                                MaterialTheme.colorScheme.secondaryContainer
                            } else {
                                MaterialTheme.colorScheme.primaryContainer
                            }
                        ) {
                            Text(
                                text = "${selectedIds.size} / 4 picked",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                                    .testTag("emotion_counter_badge")
                            )
                        }
                    }

                    AnimatedVisibility(visible = uiState.maxLimitNotice) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Info,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "You can pick up to 4 feelings per day—tap any selected card to swap it!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }

            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 148.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(EmotionCatalog.all, key = { it.id }) { emotion ->
                    val isSelected = emotion.id in selectedIds
                    EmotionTile(
                        emotion = emotion,
                        isSelected = isSelected,
                        reducedMotion = uiState.settings.reducedMotion,
                        onClick = { onToggleEmotion(emotion.id) }
                    )
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        OutlinedTextField(
                            value = uiState.draftNote,
                            onValueChange = onNoteChanged,
                            label = { Text("Optional tiny note (private on your device)") },
                            placeholder = { Text("e.g., Math quiz was tricky, but lunch with Sam helped") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("checkin_note_input"),
                            shape = RoundedCornerShape(18.dp),
                            maxLines = 3
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Button(
                            onClick = onSaveCheckIn,
                            enabled = selectedIds.isNotEmpty(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("save_checkin_button"),
                            shape = RoundedCornerShape(18.dp)
                        ) {
                            Text(
                                text = if (selectedIds.isEmpty()) {
                                    "Pick at least 1 emotion to save"
                                } else {
                                    "Save Today's Check-In (${selectedIds.size})"
                                },
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun EmotionTile(
    emotion: EmotionItem,
    isSelected: Boolean,
    reducedMotion: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected && !reducedMotion) 1.03f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "emotion_scale"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .clickable(onClick = onClick)
            .testTag("emotion_card_${emotion.id}"),
        shape = RoundedCornerShape(22.dp),
        color = if (isSelected) emotion.cardColor else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = if (isSelected) 2.5.dp else 1.2.dp,
            color = if (isSelected) emotion.accentColor else MaterialTheme.colorScheme.outline
        ),
        tonalElevation = if (isSelected) 4.dp else 1.dp
    ) {
        Box(modifier = Modifier.padding(12.dp)) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Rounded.CheckCircle,
                    contentDescription = "Selected",
                    tint = emotion.accentColor,
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.TopEnd)
                )
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CartoonEmotionFace(
                    faceStyle = emotion.faceStyle,
                    baseColor = emotion.cardColor,
                    accentColor = emotion.accentColor,
                    size = 52.dp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = emotion.label,
                    style = MaterialTheme.typography.titleMedium,
                    color = if (isSelected) Color(0xFF232533) else MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = emotion.gentleHint,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isSelected) Color(0xFF3E4257) else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    maxLines = 2
                )
            }
        }
    }
}
