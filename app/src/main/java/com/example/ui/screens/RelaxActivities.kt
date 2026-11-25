package com.example.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.automirrored.rounded.Undo
import androidx.compose.material.icons.rounded.DeleteOutline
import androidx.compose.material.icons.rounded.LocalFlorist
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.audio.AmbientSoundEngine
import com.example.audio.AmbientTrack
import com.example.data.GardenPlantEntity
import com.example.data.MeadowDateUtils
import com.example.data.PrebuiltContentLibrary
import com.example.model.CartoonCharacterId
import com.example.ui.components.AnimatedCartoonBuddy
import kotlinx.coroutines.delay
import kotlin.math.hypot
import kotlin.random.Random

private data class FloatingBubble(
    val id: Int,
    val xFrac: Float,
    val yFrac: Float,
    val radiusDp: Float,
    val color: Color,
    val speed: Float
)

@Composable
fun BubblePopActivity(
    soundEngine: AmbientSoundEngine,
    reducedMotion: Boolean
) {
    val palette = listOf(
        Color(0xFFA8E6CF), Color(0xFFFFD3B6), Color(0xFFB5DEFF),
        Color(0xFFE4C1F9), Color(0xFFFFF3B0)
    )
    var poppedCount by remember { mutableIntStateOf(0) }
    val bubbles = remember {
        mutableStateListOf<FloatingBubble>().apply {
            for (i in 0 until 10) {
                add(
                    FloatingBubble(
                        id = i,
                        xFrac = 0.12f + (i % 4) * 0.24f + Random.nextFloat() * 0.06f,
                        yFrac = 0.18f + (i / 4) * 0.26f,
                        radiusDp = 26f + (i % 3) * 8f,
                        color = palette[i % palette.size],
                        speed = 0.0025f + (i % 3) * 0.001f
                    )
                )
            }
        }
    }

    LaunchedEffect(reducedMotion) {
        if (!reducedMotion) {
            while (true) {
                delay(33L)
                for (i in bubbles.indices) {
                    val b = bubbles[i]
                    var ny = b.yFrac - b.speed
                    if (ny < 0.08f) ny = 0.92f
                    bubbles[i] = b.copy(yFrac = ny)
                }
            }
        }
    }

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Bubble Pop Meadow", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "Tap any floating bubble slowly. Popped: $poppedCount",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFFEAF7FF))
                    .pointerInput(Unit) {
                        detectTapGestures { tap ->
                            val w = size.width.toFloat().coerceAtLeast(1f)
                            val h = size.height.toFloat().coerceAtLeast(1f)
                            val hitIdx = bubbles.indexOfFirst { b ->
                                val bx = b.xFrac * w
                                val by = b.yFrac * h
                                hypot(tap.x - bx, tap.y - by) <= b.radiusDp * density + 18f
                            }
                            if (hitIdx >= 0) {
                                soundEngine.playSoftPop()
                                poppedCount++
                                val old = bubbles[hitIdx]
                                bubbles[hitIdx] = old.copy(
                                    xFrac = 0.14f + Random.nextFloat() * 0.72f,
                                    yFrac = 0.88f,
                                    color = palette.random()
                                )
                            }
                        }
                    }
                    .testTag("bubble_pop_canvas")
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    bubbles.forEach { b ->
                        val cx = b.xFrac * size.width
                        val cy = b.yFrac * size.height
                        val r = b.radiusDp * density
                        drawCircle(b.color.copy(alpha = 0.85f), r, Offset(cx, cy))
                        drawCircle(Color.White.copy(alpha = 0.9f), r, Offset(cx, cy), style = Stroke(3f))
                        drawCircle(Color.White.copy(alpha = 0.7f), r * 0.22f, Offset(cx - r * 0.35f, cy - r * 0.35f))
                    }
                }
            }
        }
    }
}

@Composable
fun BreathingAnimationActivity(reducedMotion: Boolean) {
    val rhythms = listOf(
        Triple("Cozy 4-2-6", Triple(4, 2, 6), "Gentle everyday calm"),
        Triple("Box 4-4-4", Triple(4, 4, 4), "Steady before a quiz"),
        Triple("Evening 4-7-8", Triple(4, 7, 8), "Deep wind-down")
    )
    var selectedIdx by remember { mutableIntStateOf(0) }
    var active by remember { mutableStateOf(false) }
    var secInCycle by remember { mutableIntStateOf(0) }

    val (_, times, desc) = rhythms[selectedIdx]
    val (inSec, holdSec, outSec) = times
    val totalCycle = inSec + holdSec + outSec
    val pos = secInCycle % totalCycle
    val phaseLabel = when {
        !active -> "Choose a rhythm and press Start"
        pos < inSec -> "Breathe In (${inSec - pos}s)"
        pos < inSec + holdSec -> "Hold Softly (${inSec + holdSec - pos}s)"
        else -> "Breathe Out (${totalCycle - pos}s)"
    }
    val petalScale = when {
        !active || reducedMotion -> 125.dp
        pos < inSec -> 185.dp
        pos < inSec + holdSec -> 185.dp
        else -> 100.dp
    }
    val animSize by animateDpAsState(petalScale, tween(900), label = "breath_flower")

    LaunchedEffect(active, selectedIdx) {
        if (active) {
            while (active) {
                delay(1000L)
                secInCycle++
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
            Text("Breathing Animation", style = MaterialTheme.typography.titleLarge)
            Text(desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(10.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                rhythms.forEachIndexed { idx, item ->
                    FilterChip(
                        selected = selectedIdx == idx,
                        onClick = {
                            selectedIdx = idx
                            secInCycle = 0
                        },
                        label = { Text(item.first) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = phaseLabel,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                Canvas(modifier = Modifier.size(animSize)) {
                    val r = size.minDimension / 2f
                    val center = Offset(r, r)
                    for (i in 0 until 6) {
                        val angle = i * (Math.PI / 3.0)
                        val px = center.x + (kotlin.math.cos(angle) * r * 0.42f).toFloat()
                        val py = center.y + (kotlin.math.sin(angle) * r * 0.42f).toFloat()
                        drawCircle(Color(0xFFC8EEDF).copy(alpha = 0.85f), r * 0.44f, Offset(px, py))
                    }
                    drawCircle(Color(0xFFFFF0B8), r * 0.34f, center)
                }
                AnimatedCartoonBuddy(
                    character = CartoonCharacterId.MOMO,
                    mood = if (active) "breathing" else "warm",
                    reducedMotion = reducedMotion,
                    size = 66.dp
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = { active = !active },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("breathing_toggle_button"),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(if (active) "Pause Breathing" else "Start Breathing")
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ThoughtGardenActivity(
    plants: List<GardenPlantEntity>,
    onPlantFlower: (String, String) -> Unit
) {
    val flowerTypes = listOf(
        "Sunburst Daisy" to Color(0xFFFFD86B),
        "Coral Tulip" to Color(0xFFFF9B85),
        "Lavender Sprout" to Color(0xFFCDB4FF),
        "Sky Bluebell" to Color(0xFF8FD3F4)
    )
    var selectedFlower by remember { mutableStateOf(flowerTypes.first().first) }
    var reflectionText by remember { mutableStateOf("") }
    val reflectionPrompts = listOf(
        "I survived a tough class today",
        "One small thing I'm glad happened",
        "Letting go of today's worry"
    )

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text("Thought Garden", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "Plant a virtual flower by sharing a tiny reflection. (${plants.size} planted)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Visual meadow of planted flowers
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFE7F7EA),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            ) {
                if (plants.isEmpty()) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Your garden patch is ready for its very first sprout!",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF285E45)
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        plants.take(6).forEach { plant ->
                            val petalColor = flowerTypes.find { it.first == plant.flowerType }?.second ?: Color(0xFFFFD86B)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(bottom = 10.dp)
                            ) {
                                Canvas(modifier = Modifier.size(44.dp)) {
                                    drawLine(Color(0xFF3E9C66), Offset(size.width / 2f, size.height), Offset(size.width / 2f, size.height * 0.4f), 6f, StrokeCap.Round)
                                    for (i in 0 until 5) {
                                        val angle = i * (2 * Math.PI / 5)
                                        val px = size.width / 2f + (kotlin.math.cos(angle) * 11f).toFloat()
                                        val py = size.height * 0.34f + (kotlin.math.sin(angle) * 11f).toFloat()
                                        drawCircle(petalColor, 8.5f, Offset(px, py))
                                    }
                                    drawCircle(Color(0xFFFFF4B8), 6f, Offset(size.width / 2f, size.height * 0.34f))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                flowerTypes.forEach { (name, _) ->
                    FilterChip(
                        selected = selectedFlower == name,
                        onClick = { selectedFlower = name },
                        label = { Text(name) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                reflectionPrompts.forEach { starter ->
                    Surface(
                        onClick = { reflectionText = starter },
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = starter,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = reflectionText,
                onValueChange = { reflectionText = it.take(140) },
                label = { Text("A short thought to plant") },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("thought_garden_input"),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Button(
                onClick = {
                    onPlantFlower(selectedFlower, reflectionText)
                    reflectionText = ""
                },
                enabled = reflectionText.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("plant_flower_button"),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Rounded.LocalFlorist, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Plant $selectedFlower")
            }

            if (plants.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                plants.take(3).forEach { plant ->
                    Text(
                        text = "• ${plant.flowerType} (${MeadowDateUtils.formatFriendly(plant.plantedDateIso)}): \"${plant.reflectionText}\"",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            }
        }
    }
}

private data class DoodleStroke(
    val points: List<Offset>,
    val color: Color,
    val width: Float
)

@Composable
fun MiniDoodleActivity() {
    val colors = listOf(
        Color(0xFF282B38), Color(0xFF2B8A6E), Color(0xFFE56B55),
        Color(0xFF3A7CA5), Color(0xFFF5A623), Color(0xFF8E63CE)
    )
    var activeColor by remember { mutableStateOf(colors[1]) }
    val strokes = remember { mutableStateListOf<DoodleStroke>() }
    var currentPoints by remember { mutableStateOf<List<Offset>>(emptyList()) }

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Mini Doodle Pad", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "Scribble freely—no art skills needed!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Row {
                    IconButton(
                        onClick = { if (strokes.isNotEmpty()) strokes.removeAt(strokes.lastIndex) },
                        modifier = Modifier.testTag("doodle_undo_button")
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.Undo, contentDescription = "Undo stroke")
                    }
                    IconButton(
                        onClick = { strokes.clear() },
                        modifier = Modifier.testTag("doodle_clear_button")
                    ) {
                        Icon(Icons.Rounded.DeleteOutline, contentDescription = "Clear canvas")
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                colors.forEach { c ->
                    Surface(
                        onClick = { activeColor = c },
                        shape = CircleShape,
                        color = c,
                        border = BorderStroke(if (activeColor == c) 3.dp else 1.dp, Color.White),
                        modifier = Modifier.size(34.dp)
                    ) {}
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFFFFDF7))
                    .pointerInput(activeColor) {
                        detectDragGestures(
                            onDragStart = { offset -> currentPoints = listOf(offset) },
                            onDrag = { change, _ ->
                                change.consume()
                                currentPoints = currentPoints + change.position
                            },
                            onDragEnd = {
                                if (currentPoints.size > 1) {
                                    strokes.add(DoodleStroke(currentPoints, activeColor, 10f))
                                }
                                currentPoints = emptyList()
                            }
                        )
                    }
                    .testTag("mini_doodle_canvas")
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    fun drawStroke(s: List<Offset>, color: Color, width: Float) {
                        if (s.size < 2) return
                        val p = Path().apply {
                            moveTo(s.first().x, s.first().y)
                            for (i in 1 until s.size) lineTo(s[i].x, s[i].y)
                        }
                        drawPath(p, color, style = Stroke(width = width, cap = StrokeCap.Round, join = StrokeJoin.Round))
                    }
                    strokes.forEach { drawStroke(it.points, it.color, it.width) }
                    drawStroke(currentPoints, activeColor, 10f)
                }
            }
        }
    }
}

@Composable
fun SoundCornerActivity(
    soundEngine: AmbientSoundEngine,
    soundEnabled: Boolean
) {
    var playingTrack by remember { mutableStateOf<AmbientTrack?>(null) }

    DisposableEffect(Unit) {
        onDispose { soundEngine.stopAll() }
    }

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text("Sound Corner", style = MaterialTheme.typography.titleLarge)
            Text(
                text = if (soundEnabled) "Offline calming sounds synthesized right on your device." else "Sound is muted in Settings — turn it on to listen.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            AmbientTrack.entries.forEach { track ->
                val isPlaying = playingTrack == track
                Surface(
                    onClick = {
                        if (!soundEnabled) return@Surface
                        if (isPlaying) {
                            soundEngine.stopAll()
                            playingTrack = null
                        } else {
                            soundEngine.startAmbient(track)
                            playingTrack = track
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("sound_track_${track.name.lowercase()}"),
                    shape = RoundedCornerShape(18.dp),
                    color = if (isPlaying) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MusicNote,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(track.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text(track.subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Icon(
                            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = if (isPlaying) "Pause ${track.title}" else "Play ${track.title}"
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MoodCompanionActivity(
    soundEngine: AmbientSoundEngine,
    reducedMotion: Boolean
) {
    var selectedPromptIndex by remember { mutableIntStateOf(0) }
    var messageStep by remember { mutableIntStateOf(0) }
    val prompts = PrebuiltContentLibrary.companionPrompts
    val currentMessages = prompts[selectedPromptIndex].second
    val activeMessage = currentMessages[messageStep % currentMessages.size]

    Surface(
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.img_buddy_momo),
                    contentDescription = "Momo Mood Companion",
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Momo • Mood Companion", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "Original cartoon buddy (100% pre-written, zero AI)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(14.dp))

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            ) {
                Text(
                    text = "\"$activeMessage\"",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("What do you feel like hearing?", style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                prompts.forEachIndexed { idx, (label, _) ->
                    FilterChip(
                        selected = selectedPromptIndex == idx,
                        onClick = {
                            soundEngine.playSoftPop()
                            if (selectedPromptIndex == idx) {
                                messageStep++
                            } else {
                                selectedPromptIndex = idx
                                messageStep = 0
                            }
                        },
                        label = { Text(label) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            OutlinedButton(
                onClick = {
                    soundEngine.playSoftPop()
                    messageStep++
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("companion_next_message_button"),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Show Another Note")
            }
        }
    }
}
