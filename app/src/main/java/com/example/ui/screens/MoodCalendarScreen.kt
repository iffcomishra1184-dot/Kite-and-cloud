package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.data.MeadowDateUtils
import com.example.data.PrebuiltContentLibrary
import com.example.model.EmotionCatalog
import com.example.ui.MeadowUiState
import com.example.ui.components.CartoonEmotionFace

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodCalendarScreen(
    uiState: MeadowUiState,
    onEditDate: (String) -> Unit,
    onDeleteDate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var monthOffset by remember { mutableIntStateOf(0) }
    var selectedDateIso by remember { mutableStateOf(uiState.todayIso) }
    var pendingDeleteIso by remember { mutableStateOf<String?>(null) }

    val (year, monthZero) = remember(monthOffset) { MeadowDateUtils.monthOffsetIso(monthOffset) }
    val monthTitle = remember(year, monthZero) { MeadowDateUtils.formatMonthYear(year, monthZero) }
    val gridCells = remember(year, monthZero) { MeadowDateUtils.daysInMonthGrid(year, monthZero) }
    val entriesByDate = remember(uiState.allEntries) { uiState.allEntries.associateBy { it.dateIso } }

    val selectedEntry = entriesByDate[selectedDateIso]
    val selectedEmotions = remember(selectedEntry) {
        EmotionCatalog.fromCsv(selectedEntry?.emotionIdsCsv.orEmpty())
    }

    if (pendingDeleteIso != null) {
        AlertDialog(
            onDismissRequest = { pendingDeleteIso = null },
            title = { Text("Delete this mood entry?") },
            text = { Text("This removes your saved emotions and note for ${MeadowDateUtils.formatFriendly(pendingDeleteIso!!)} from this device.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteDate(pendingDeleteIso!!)
                        pendingDeleteIso = null
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { pendingDeleteIso = null }) {
                    Text("Keep")
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
                Column {
                    Text("Mood Calendar", style = MaterialTheme.typography.headlineMedium)
                    Text(
                        text = "Your private emotional timeline. Empty days are simply quiet days—zero streaks or guilt.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Calendar card
            item {
                Surface(
                    shape = RoundedCornerShape(26.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { monthOffset-- },
                                modifier = Modifier.testTag("calendar_prev_month")
                            ) {
                                Icon(Icons.AutoMirrored.Rounded.KeyboardArrowLeft, contentDescription = "Previous month")
                            }
                            Text(
                                text = monthTitle,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            IconButton(
                                onClick = { monthOffset++ },
                                modifier = Modifier.testTag("calendar_next_month")
                            ) {
                                Icon(Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = "Next month")
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        val weekdays = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
                        Row(modifier = Modifier.fillMaxWidth()) {
                            weekdays.forEach { dayName ->
                                Text(
                                    text = dayName,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        gridCells.chunked(7).forEach { week ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                week.forEach { dateIso ->
                                    if (dateIso == null) {
                                        Spacer(modifier = Modifier.weight(1f).height(48.dp))
                                    } else {
                                        val entry = entriesByDate[dateIso]
                                        val emotions = EmotionCatalog.fromCsv(entry?.emotionIdsCsv.orEmpty())
                                        val isSelected = dateIso == selectedDateIso
                                        val isToday = dateIso == uiState.todayIso
                                        Surface(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(48.dp)
                                                .clickable { selectedDateIso = dateIso }
                                                .testTag("calendar_day_$dateIso"),
                                            shape = RoundedCornerShape(12.dp),
                                            color = when {
                                                isSelected -> MaterialTheme.colorScheme.primaryContainer
                                                emotions.isNotEmpty() -> emotions.first().cardColor.copy(alpha = 0.65f)
                                                else -> Color.Transparent
                                            },
                                            border = when {
                                                isSelected -> BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                                                isToday -> BorderStroke(1.5.dp, MaterialTheme.colorScheme.secondary)
                                                else -> null
                                            }
                                        ) {
                                            Column(
                                                modifier = Modifier.fillMaxSize(),
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Text(
                                                    text = MeadowDateUtils.dayNumberFromIso(dateIso).toString(),
                                                    style = MaterialTheme.typography.labelMedium,
                                                    color = MaterialTheme.colorScheme.onSurface,
                                                    fontWeight = if (isToday || isSelected) FontWeight.Bold else FontWeight.Normal
                                                )
                                                if (emotions.isNotEmpty()) {
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                                        emotions.forEach { emo ->
                                                            Box(
                                                                modifier = Modifier
                                                                    .size(6.dp)
                                                                    .clip(CircleShape)
                                                                    .background(emo.accentColor)
                                                            )
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Selected Day Details Card
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = MeadowDateUtils.formatFriendly(selectedDateIso),
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (selectedEntry == null || selectedEmotions.isEmpty()) {
                            Text(
                                text = "No check-in saved for this date yet.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            OutlinedButton(
                                onClick = { onEditDate(selectedDateIso) },
                                modifier = Modifier.height(48.dp),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(Icons.Rounded.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Add Check-In for This Day")
                            }
                        } else {
                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                selectedEmotions.forEach { emo ->
                                    Surface(
                                        shape = RoundedCornerShape(16.dp),
                                        color = emo.cardColor
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            CartoonEmotionFace(
                                                faceStyle = emo.faceStyle,
                                                baseColor = emo.cardColor,
                                                accentColor = emo.accentColor,
                                                size = 24.dp
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(emo.label, style = MaterialTheme.typography.labelLarge, color = Color(0xFF232533))
                                        }
                                    }
                                }
                            }
                            if (selectedEntry.note.isNotBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = "\"${selectedEntry.note}\"",
                                        style = MaterialTheme.typography.bodyMedium,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                OutlinedButton(
                                    onClick = { onEditDate(selectedDateIso) },
                                    modifier = Modifier.height(48.dp),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Icon(Icons.Rounded.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Edit")
                                }
                                Button(
                                    onClick = { pendingDeleteIso = selectedDateIso },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                        contentColor = MaterialTheme.colorScheme.onErrorContainer
                                    ),
                                    modifier = Modifier
                                        .height(48.dp)
                                        .testTag("delete_mood_entry_button"),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Icon(Icons.Rounded.DeleteOutline, contentDescription = null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Delete")
                                }
                            }
                        }
                    }
                }
            }

            // Collectible Sticker Shelf
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Pocket Sticker Shelf (${uiState.settings.unlockedStickers.size}/${PrebuiltContentLibrary.stickers.size})",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            PrebuiltContentLibrary.stickers.forEach { sticker ->
                                val unlocked = sticker.id in uiState.settings.unlockedStickers
                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = if (unlocked) Color(sticker.colorHex).copy(alpha = 0.65f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Rounded.Star,
                                            contentDescription = null,
                                            tint = if (unlocked) Color(0xFF282B38) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                            modifier = Modifier.size(18.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = if (unlocked) sticker.name else "Locked Sticker",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (unlocked) Color(0xFF282B38) else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
