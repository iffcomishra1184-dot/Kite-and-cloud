package com.example.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Air
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.SelfImprovement
import androidx.compose.material.icons.rounded.Spa
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.audio.AmbientSoundEngine
import com.example.model.CartoonCharacterId
import com.example.ui.components.AnimatedCartoonBuddy
import kotlinx.coroutines.delay

private enum class TinyResetMode(
    val title: String,
    val durationLabel: String,
    val icon: ImageVector
) {
    CLOUD_BREATH("Cloud Breath", "60 sec", Icons.Rounded.Air),
    PATTERN_MATCH("Calm Match", "45 sec", Icons.Rounded.GridView),
    THREE_GOOD("3 Good Things", "60 sec", Icons.Rounded.Favorite),
    TENSION_MELT("Shoulder Melt", "45 sec", Icons.Rounded.SelfImprovement)
}

@Composable
fun TinyResetSection(
    soundEngine: AmbientSoundEngine,
    reducedMotion: Boolean,
    completedCount: Int,
    onCompletedReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeMode by remember { mutableStateOf(TinyResetMode.CLOUD_BREATH) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f),
            border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.25f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AnimatedCartoonBuddy(
                    character = CartoonCharacterId.MOMO,
                    mood = "calm",
                    reducedMotion = reducedMotion,
                    size = 54.dp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Tiny Reset • Under 2 Minutes",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Text(
                        text = "Quick, low-effort resets between classes or study blocks. ($completedCount done so far)",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }

        // 2x2 Easy-access Activity Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TinyResetMode.entries.take(2).forEach { mode ->
                TinyResetChipCard(
                    mode = mode,
                    selected = activeMode == mode,
                    onClick = { activeMode = mode },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TinyResetMode.entries.drop(2).forEach { mode ->
                TinyResetChipCard(
                    mode = mode,
                    selected = activeMode == mode,
                    onClick = { activeMode = mode },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        when (activeMode) {
            TinyResetMode.CLOUD_BREATH -> QuickCloudBreathCard(
                reducedMotion = reducedMotion,
                onComplete = onCompletedReset
            )
            TinyResetMode.PATTERN_MATCH -> CalmPatternMatchCard(
                soundEngine = soundEngine,
                onComplete = onCompletedReset
            )
            TinyResetMode.THREE_GOOD -> ThreeGoodThingsCard(
                soundEngine = soundEngine,
                onComplete = onCompletedReset
            )
            TinyResetMode.TENSION_MELT -> TensionMeltCard(
                soundEngine = soundEngine,
                onComplete = onCompletedReset
            )
        }
    }
}

@Composable
private fun TinyResetChipCard(
    mode: TinyResetMode,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier.testTag("tiny_reset_tab_${mode.name.lowercase()}"),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = if (selected) 2.dp else 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
        )
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = mode.icon,
                contentDescription = null,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = mode.title,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = mode.durationLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun QuickCloudBreathCard(
    reducedMotion: Boolean,
    onComplete: () -> Unit
) {
    var running by remember { mutableStateOf(false) }
    var remainingSeconds by remember { mutableIntStateOf(60) }
    var finished by remember { mutableStateOf(false) }

    // 10s cycle: 4s in, 2s hold, 4s out
    val cyclePos = (60 - remainingSeconds) % 10
    val phaseText = when {
        !running && !finished -> "Tap Start for 6 gentle breaths"
        finished -> "Nice! You gave yourself a full minute."
        cyclePos < 4 -> "Breathe In..."
        cyclePos < 6 -> "Hold Softly..."
        else -> "Breathe Out..."
    }
    val targetDiameter = when {
        !running || reducedMotion -> 124.dp
        cyclePos < 4 -> 176.dp
        cyclePos < 6 -> 176.dp
        else -> 104.dp
    }
    val circleSize by animateDpAsState(
        targetValue = targetDiameter,
        animationSpec = tween(durationMillis = 950),
        label = "quick_breath_size"
    )

    LaunchedEffect(running) {
        if (running) {
            while (running && remainingSeconds > 0) {
                delay(1000L)
                remainingSeconds--
            }
            if (remainingSeconds == 0) {
                running = false
                finished = true
                onComplete()
            }
        }
    }

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("60-Second Cloud Breath", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = phaseText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier.size(190.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.size(circleSize)) {
                    drawCircle(color = Color(0xFFC8EEDF))
                    drawCircle(color = Color(0xFF9EE0C5), radius = size.minDimension * 0.36f)
                }
                Text(
                    text = "${remainingSeconds}s",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF0A382A),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(
                    onClick = {
                        if (finished) {
                            remainingSeconds = 60
                            finished = false
                        }
                        running = !running
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .testTag("tiny_breath_start_button"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (running) "Pause" else if (finished) "Do Another" else "Start 60s")
                }
                if (remainingSeconds < 60) {
                    OutlinedButton(
                        onClick = {
                            running = false
                            remainingSeconds = 60
                            finished = false
                        },
                        modifier = Modifier.height(48.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}

private data class MatchCardItem(
    val index: Int,
    val symbolId: Int,
    val label: String,
    val bgColor: Color,
    val icon: ImageVector,
    val isFaceUp: Boolean = false,
    val isMatched: Boolean = false
)

@Composable
private fun CalmPatternMatchCard(
    soundEngine: AmbientSoundEngine,
    onComplete: () -> Unit
) {
    fun newDeck(): List<MatchCardItem> {
        val base = listOf(
            Triple(0, "Sun", Icons.Rounded.WbSunny) to Color(0xFFFFF0B8),
            Triple(1, "Leaf", Icons.Rounded.Eco) to Color(0xFFD7F2E3),
            Triple(2, "Star", Icons.Rounded.Star) to Color(0xFFEAE3FF),
            Triple(3, "Bloom", Icons.Rounded.Spa) to Color(0xFFFFE5EE)
        )
        return (base + base).shuffled().mapIndexed { idx, pair ->
            MatchCardItem(
                index = idx,
                symbolId = pair.first.first,
                label = pair.first.second,
                bgColor = pair.second,
                icon = pair.first.third
            )
        }
    }

    var cards by remember { mutableStateOf(newDeck()) }
    var firstIndex by remember { mutableStateOf<Int?>(null) }
    var lockTap by remember { mutableStateOf(false) }
    var won by remember { mutableStateOf(false) }

    LaunchedEffect(cards) {
        val faceUpUnmatched = cards.filter { it.isFaceUp && !it.isMatched }
        if (faceUpUnmatched.size == 2) {
            lockTap = true
            delay(550L)
            val a = faceUpUnmatched[0]
            val b = faceUpUnmatched[1]
            cards = cards.map { card ->
                when {
                    card.index == a.index || card.index == b.index -> {
                        if (a.symbolId == b.symbolId) card.copy(isMatched = true)
                        else card.copy(isFaceUp = false)
                    }
                    else -> card
                }
            }
            if (a.symbolId == b.symbolId) {
                soundEngine.playGentleChime()
            }
            firstIndex = null
            lockTap = false
            if (cards.all { it.isMatched } && !won) {
                won = true
                onComplete()
            }
        }
    }

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Calm Pattern Match", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = if (won) "All 4 cozy pairs matched!" else "Tap two tiles to find matching meadow icons. No timer!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = {
                        cards = newDeck()
                        firstIndex = null
                        lockTap = false
                        won = false
                    },
                    modifier = Modifier.testTag("pattern_match_reset_button")
                ) {
                    Icon(Icons.Rounded.Refresh, contentDescription = "Shuffle match game")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 4x2 grid
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                for (row in 0..1) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        for (col in 0..3) {
                            val card = cards[row * 4 + col]
                            Surface(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(74.dp)
                                    .clickable(enabled = !lockTap && !card.isFaceUp && !card.isMatched) {
                                        soundEngine.playSoftPop()
                                        cards = cards.map {
                                            if (it.index == card.index) it.copy(isFaceUp = true) else it
                                        }
                                        if (firstIndex == null) firstIndex = card.index
                                    }
                                    .testTag("match_tile_${card.index}"),
                                shape = RoundedCornerShape(18.dp),
                                color = if (card.isFaceUp || card.isMatched) card.bgColor else MaterialTheme.colorScheme.surfaceVariant,
                                border = BorderStroke(
                                    width = if (card.isMatched) 2.dp else 1.2.dp,
                                    color = if (card.isMatched) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    if (card.isFaceUp || card.isMatched) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Icon(
                                                imageVector = card.icon,
                                                contentDescription = card.label,
                                                tint = Color(0xFF282B38),
                                                modifier = Modifier.size(28.dp)
                                            )
                                            Text(
                                                text = card.label,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = Color(0xFF282B38)
                                            )
                                        }
                                    } else {
                                        Text(
                                            text = "?",
                                            style = MaterialTheme.typography.titleLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ThreeGoodThingsCard(
    soundEngine: AmbientSoundEngine,
    onComplete: () -> Unit
) {
    val quickSuggestions = listOf(
        "Warm drink", "Good song", "Funny meme", "Finished a small task",
        "Cozy blanket", "Sunlight by window", "Kind friend", "Quiet break"
    )
    val picked = remember { mutableStateListOf<String>() }
    var customInput by remember { mutableStateOf("") }
    var savedDone by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("Three Good Things", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tap 3 tiny comforts from today—or type your own short one.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(10.dp))

            // 3 slots
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (slot in 0..2) {
                    val value = picked.getOrNull(slot)
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable(enabled = value != null && !savedDone) {
                                if (value != null) picked.remove(value)
                            },
                        shape = RoundedCornerShape(14.dp),
                        color = if (value != null) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = value ?: "${slot + 1}. ...",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!savedDone) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickSuggestions.forEach { chip ->
                        val isChosen = chip in picked
                        FilterChip(
                            selected = isChosen,
                            onClick = {
                                soundEngine.playSoftPop()
                                if (isChosen) {
                                    picked.remove(chip)
                                } else if (picked.size < 3) {
                                    picked.add(chip)
                                }
                            },
                            label = { Text(chip) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = customInput,
                        onValueChange = { customInput = it.take(40) },
                        label = { Text("Or type one") },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    OutlinedButton(
                        onClick = {
                            val trimmed = customInput.trim()
                            if (trimmed.isNotEmpty() && picked.size < 3) {
                                picked.add(trimmed)
                                customInput = ""
                                soundEngine.playSoftPop()
                            }
                        },
                        enabled = customInput.isNotBlank() && picked.size < 3,
                        modifier = Modifier.height(50.dp),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Add")
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = {
                        savedDone = true
                        onComplete()
                    },
                    enabled = picked.size == 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("three_good_complete_button"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (picked.size == 3) "Pocket These 3 Good Things" else "Pick ${3 - picked.size} more")
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(Icons.Rounded.CheckCircle, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tucked into today's reset! Little good things count.",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                picked.clear()
                                savedDone = false
                            }
                        ) {
                            Text("Again")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TensionMeltCard(
    soundEngine: AmbientSoundEngine,
    onComplete: () -> Unit
) {
    val steps = listOf(
        "1. Drop Your Shoulders" to "Let your shoulders slide way down away from your ears. Shake your hands out loosely.",
        "2. Unclench Your Jaw" to "Let a tiny gap stay between your teeth and soften your forehead.",
        "3. One Long Easy Sigh" to "Breathe in gently through your nose, then sigh it all the way out."
    )
    var stepIndex by remember { mutableIntStateOf(0) }
    var done by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("45-Second Shoulder Melt", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { if (done) 1f else (stepIndex + 1) / 3f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(14.dp))

            if (!done) {
                Text(
                    text = steps[stepIndex].first,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = steps[stepIndex].second,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        soundEngine.playSoftPop()
                        if (stepIndex < steps.lastIndex) {
                            stepIndex++
                        } else {
                            done = true
                            onComplete()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("tension_melt_next_button"),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(if (stepIndex < steps.lastIndex) "Done — Next Step" else "Finish Reset")
                }
            } else {
                Text(
                    text = "Aah, lighter already. You can come back here between any class or study session.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = {
                        stepIndex = 0
                        done = false
                    },
                    modifier = Modifier.height(48.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Do It Again")
                }
            }
        }
    }
}
