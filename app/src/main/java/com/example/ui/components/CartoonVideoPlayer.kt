package com.example.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.ClosedCaption
import androidx.compose.material.icons.rounded.ClosedCaptionDisabled
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.Replay
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.VoiceOverOff
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.audio.AmbientSoundEngine
import com.example.audio.StoryVoiceNarrator
import com.example.model.AnimatedSceneFrame
import com.example.model.CartoonSceneBackdrop
import com.example.model.CartoonStoryVideo
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CartoonVideoPlayer(
    story: CartoonStoryVideo,
    soundEngine: AmbientSoundEngine,
    voiceNarrator: StoryVoiceNarrator,
    soundEnabled: Boolean,
    voiceEnabled: Boolean,
    subtitlesEnabled: Boolean,
    reducedMotion: Boolean,
    onToggleVoice: (Boolean) -> Unit,
    onToggleSubtitles: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember(story.id) { mutableStateOf(false) }
    var elapsedSeconds by remember(story.id) { mutableFloatStateOf(0f) }
    var localMuted by remember(story.id) { mutableStateOf(false) }
    var replayNonce by remember(story.id) { mutableIntStateOf(0) }

    val totalSeconds = remember(story) { story.scenes.sumOf { it.durationSeconds }.coerceAtLeast(1) }

    // Find current scene from elapsedSeconds
    val currentSceneInfo = remember(story, elapsedSeconds) {
        var acc = 0
        var chosenIndex = 0
        for ((index, scene) in story.scenes.withIndex()) {
            val end = acc + scene.durationSeconds
            if (elapsedSeconds < end || index == story.scenes.lastIndex) {
                chosenIndex = index
                break
            }
            acc = end
        }
        chosenIndex to story.scenes[chosenIndex]
    }
    val sceneIndex = currentSceneInfo.first
    val currentScene = currentSceneInfo.second

    // Playback loop (30fps smooth ticks)
    LaunchedEffect(isPlaying, story.id) {
        if (isPlaying) {
            if (soundEnabled && !localMuted) {
                soundEngine.startStoryMusic(story.musicMood)
            }
            while (isPlaying) {
                delay(33L)
                elapsedSeconds += 0.033f
                if (elapsedSeconds >= totalSeconds) {
                    elapsedSeconds = totalSeconds.toFloat()
                    isPlaying = false
                    soundEngine.stopAll()
                }
            }
        } else {
            soundEngine.stopAll()
        }
    }

    LaunchedEffect(localMuted, soundEnabled, isPlaying) {
        if (!isPlaying || !soundEnabled || localMuted) {
            soundEngine.stopAll()
        } else {
            soundEngine.startStoryMusic(story.musicMood)
        }
    }

    LaunchedEffect(isPlaying, sceneIndex, story.id, soundEnabled, voiceEnabled, localMuted, replayNonce) {
        val shouldSpeak = isPlaying && soundEnabled && voiceEnabled && !localMuted
        soundEngine.setDuckingForVoice(shouldSpeak)
        if (shouldSpeak) {
            voiceNarrator.speakScene(
                utteranceId = "${story.id}_scene_${sceneIndex}_$replayNonce",
                text = currentScene.subtitle,
                isComfortStory = story.isComfortStory
            )
        } else {
            voiceNarrator.stop()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            soundEngine.setDuckingForVoice(false)
            soundEngine.stopAll()
            voiceNarrator.stop()
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
        tonalElevation = 3.dp
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (story.isComfortStory) "Comfort Story • 2D Animation" else "Celebration Story • 2D Animation",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = story.title,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = "Scene ${sceneIndex + 1}/${story.scenes.size}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Animated 16:9 Cartoon Stage
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9.5f)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFFDFEEFF)),
                contentAlignment = Alignment.BottomCenter
            ) {
                val animTick = if (reducedMotion) 0f else elapsedSeconds
                AnimatedContent(
                    targetState = currentScene,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "scene_transition"
                ) { scene ->
                    CartoonSceneCanvas(
                        scene = scene,
                        timeSeconds = animTick,
                        isCelebration = !story.isComfortStory,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Subtitles overlay
                if (subtitlesEnabled) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xCC1E222E)
                    ) {
                        Text(
                            text = currentScene.subtitle,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }

                // Big play overlay when paused at start
                if (!isPlaying && elapsedSeconds < 0.2f) {
                    Surface(
                        onClick = { isPlaying = true },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(64.dp)
                            .testTag("play_cartoon_video_button")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Rounded.PlayArrow,
                                contentDescription = "Play animated story",
                                tint = Color.White,
                                modifier = Modifier.size(38.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Progress bar & time
            LinearProgressIndicator(
                progress = { (elapsedSeconds / totalSeconds).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${formatSecs(elapsedSeconds.toInt())} / ${formatSecs(totalSeconds)}",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            elapsedSeconds = 0f
                            replayNonce++
                            isPlaying = true
                        },
                        modifier = Modifier.testTag("replay_video_button")
                    ) {
                        Icon(Icons.Rounded.Replay, contentDescription = "Replay story")
                    }
                    FilledTonalIconButton(
                        onClick = {
                            if (elapsedSeconds >= totalSeconds) {
                                elapsedSeconds = 0f
                                replayNonce++
                            }
                            isPlaying = !isPlaying
                        },
                        modifier = Modifier.testTag("toggle_play_video_button")
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Rounded.Pause else Icons.Rounded.PlayArrow,
                            contentDescription = if (isPlaying) "Pause story" else "Play story"
                        )
                    }
                    IconButton(
                        onClick = {
                            var acc = 0f
                            for (i in 0..sceneIndex) {
                                acc += story.scenes[i].durationSeconds
                            }
                            elapsedSeconds = if (sceneIndex < story.scenes.lastIndex) acc else 0f
                        },
                        modifier = Modifier.testTag("next_scene_button")
                    ) {
                        Icon(Icons.Rounded.SkipNext, contentDescription = "Next scene")
                    }
                    IconButton(
                        onClick = { onToggleVoice(!voiceEnabled) },
                        modifier = Modifier.testTag("toggle_voice_button")
                    ) {
                        Icon(
                            imageVector = if (voiceEnabled) Icons.Rounded.RecordVoiceOver else Icons.Rounded.VoiceOverOff,
                            contentDescription = "Toggle voice narration"
                        )
                    }
                    IconButton(
                        onClick = { onToggleSubtitles(!subtitlesEnabled) },
                        modifier = Modifier.testTag("toggle_subtitles_button")
                    ) {
                        Icon(
                            imageVector = if (subtitlesEnabled) Icons.Rounded.ClosedCaption else Icons.Rounded.ClosedCaptionDisabled,
                            contentDescription = "Toggle subtitles"
                        )
                    }
                    IconButton(
                        onClick = { localMuted = !localMuted },
                        modifier = Modifier.testTag("toggle_video_sound_button")
                    ) {
                        Icon(
                            imageVector = if (localMuted || !soundEnabled) Icons.AutoMirrored.Rounded.VolumeOff else Icons.AutoMirrored.Rounded.VolumeUp,
                            contentDescription = "Toggle story audio"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    text = story.takeawayMessage,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun CartoonSceneCanvas(
    scene: AnimatedSceneFrame,
    timeSeconds: Float,
    isCelebration: Boolean,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val gentleFloat = sin(timeSeconds * 2.1f) * (h * 0.025f)
        val blink = ((timeSeconds * 10f).toInt() % 37) == 0

        when (scene.backdrop) {
            CartoonSceneBackdrop.RAINY_WINDOW_BOATS -> {
                drawRect(Brush.verticalGradient(listOf(Color(0xFFC8E0F4), Color(0xFFEAF3FA))))
                // Warm indoor window frame
                drawRect(Color(0xFFF6E7D7), Offset(0f, 0f), Size(w * 0.1f, h))
                drawRect(Color(0xFFF6E7D7), Offset(w * 0.9f, 0f), Size(w * 0.1f, h))
                drawRect(Color(0xFFDFC7AF), Offset(0f, h * 0.72f), Size(w, h * 0.28f))
                // Raindrops
                for (i in 0..16) {
                    val rx = w * (0.12f + (i * 0.048f))
                    val ry = ((timeSeconds * 95f + i * 37f) % (h * 0.7f))
                    drawLine(Color(0xFF7FB3D5), Offset(rx, ry), Offset(rx - 4f, ry + 14f), 3f, StrokeCap.Round)
                }
                // Paper boat bobbing on puddle
                val boatX = w * 0.34f + sin(timeSeconds * 1.1f) * 18f
                val boatY = h * 0.76f + cos(timeSeconds * 2.5f) * 4f
                val boat = Path().apply {
                    moveTo(boatX - 28f, boatY)
                    lineTo(boatX + 28f, boatY)
                    lineTo(boatX + 16f, boatY + 14f)
                    lineTo(boatX - 16f, boatY + 14f)
                    close()
                }
                drawPath(boat, Color.White)
                drawPath(boat, Color(0xFF282B38), style = Stroke(2.5f))
                val sail = Path().apply {
                    moveTo(boatX - 10f, boatY)
                    lineTo(boatX, boatY - 22f)
                    lineTo(boatX + 12f, boatY)
                    close()
                }
                drawPath(sail, Color(0xFFFFD86B))
            }
            CartoonSceneBackdrop.SUNNY_BALCONY_CHAI -> {
                drawRect(Brush.verticalGradient(listOf(Color(0xFFFFF3C7), Color(0xFFFFDFC4))))
                drawCircle(Color(0xFFFFD257), w * 0.09f, Offset(w * 0.82f, h * 0.22f))
                // Bunting flags
                for (i in 0..6) {
                    val fx = w * (0.08f + i * 0.13f)
                    val flagColor = if (i % 2 == 0) Color(0xFFE56B55) else Color(0xFF4FB286)
                    val flag = Path().apply {
                        moveTo(fx, h * 0.04f)
                        lineTo(fx + w * 0.08f, h * 0.04f)
                        lineTo(fx + w * 0.04f, h * 0.16f + sin(timeSeconds * 3f + i) * 3f)
                        close()
                    }
                    drawPath(flag, flagColor)
                }
                // Terrace wall
                drawRect(Color(0xFFF3D5B5), Offset(0f, h * 0.68f), Size(w, h * 0.32f))
                drawLine(Color(0xFFD1A47B), Offset(0f, h * 0.68f), Offset(w, h * 0.68f), 5f)
            }
            CartoonSceneBackdrop.STARRY_TERRACE_LANTERNS -> {
                drawRect(Brush.verticalGradient(listOf(Color(0xFF283459), Color(0xFF535E8A))))
                for (i in 0..14) {
                    val sx = w * ((i * 19 % 92 + 4) / 100f)
                    val sy = h * ((i * 23 % 45 + 6) / 100f)
                    val alpha = 0.45f + 0.5f * ((sin(timeSeconds * 2.5f + i) + 1f) / 2f)
                    drawCircle(Color(0xFFFFF4B8).copy(alpha = alpha), 3.5f, Offset(sx, sy))
                }
                // Glowing lanterns
                for (i in 0..2) {
                    val lx = w * (0.22f + i * 0.28f) + sin(timeSeconds * 1.4f + i) * 6f
                    val ly = h * 0.18f
                    drawLine(Color(0xFF202438), Offset(lx, 0f), Offset(lx, ly), 2f)
                    drawRoundRect(Color(0xFFFFB86B), Offset(lx - 14f, ly), Size(28f, 34f), CornerRadius(10f, 10f))
                }
                drawRect(Color(0xFF394263), Offset(0f, h * 0.7f), Size(w, h * 0.3f))
            }
            CartoonSceneBackdrop.COZY_DESK_FIREFLIES -> {
                drawRect(Brush.verticalGradient(listOf(Color(0xFF3A4468), Color(0xFF666C92))))
                // Warm lamp glow
                drawCircle(Color(0xFFFFE599).copy(alpha = 0.28f), w * 0.28f, Offset(w * 0.24f, h * 0.45f))
                // Desk
                drawRect(Color(0xFFB38464), Offset(0f, h * 0.7f), Size(w, h * 0.3f))
                // Drifting fireflies
                for (i in 0..7) {
                    val fx = w * (0.15f + i * 0.1f) + sin(timeSeconds * 1.7f + i) * 14f
                    val fy = h * 0.35f + cos(timeSeconds * 1.4f + i * 2) * 18f
                    drawCircle(Color(0xFFFFF08A), 4.5f, Offset(fx, fy))
                }
            }
            CartoonSceneBackdrop.WINDY_KITE_HILL -> {
                drawRect(Brush.verticalGradient(listOf(Color(0xFFBAE2FF), Color(0xFFE7F6FF))))
                // Puffy drifting cloud
                val cx = (w * 0.2f + timeSeconds * 16f) % (w + 100f) - 50f
                drawCircle(Color.White.copy(alpha = 0.85f), 28f, Offset(cx, h * 0.2f))
                drawCircle(Color.White.copy(alpha = 0.85f), 22f, Offset(cx + 24f, h * 0.22f))
                // Green meadow hill
                drawOval(Color(0xFF9BE0A5), Offset(-w * 0.1f, h * 0.62f), Size(w * 1.2f, h * 0.6f))
                // Kite
                val kx = w * 0.72f + sin(timeSeconds * 2.2f) * 12f
                val ky = h * 0.24f + cos(timeSeconds * 1.8f) * 8f
                val kite = Path().apply {
                    moveTo(kx, ky - 22f)
                    lineTo(kx + 18f, ky)
                    lineTo(kx, ky + 26f)
                    lineTo(kx - 18f, ky)
                    close()
                }
                drawPath(kite, Color(0xFFFF8F6B))
                drawLine(Color(0xFF5A6072), Offset(kx, ky + 26f), Offset(w * 0.54f, h * 0.64f), 2f)
            }
            CartoonSceneBackdrop.QUIET_BOOK_NOOK -> {
                drawRect(Brush.verticalGradient(listOf(Color(0xFFFFEFE0), Color(0xFFFFF9F0))))
                // Cozy bookshelf silhouette
                drawRoundRect(Color(0xFFE5C4A1), Offset(w * 0.08f, h * 0.18f), Size(w * 0.22f, h * 0.56f), CornerRadius(8f, 8f))
                drawRect(Color(0xFF6ABEA3), Offset(w * 0.11f, h * 0.24f), Size(14f, 34f))
                drawRect(Color(0xFFE57A66), Offset(w * 0.16f, h * 0.26f), Size(16f, 32f))
                drawRect(Color(0xFFF3C65F), Offset(w * 0.21f, h * 0.22f), Size(14f, 36f))
                // Floor rug
                drawOval(Color(0xFFFFD6C4), Offset(w * 0.15f, h * 0.68f), Size(w * 0.7f, h * 0.24f))
            }
        }

        // Celebration confetti sparkles
        if (isCelebration) {
            val confettiColors = listOf(Color(0xFFE56B55), Color(0xFFF5A623), Color(0xFF2B8A6E), Color(0xFF5D9CEC))
            for (i in 0..11) {
                val x = w * ((i * 17 % 90 + 5) / 100f)
                val y = ((timeSeconds * 42f + i * 31f) % (h * 0.78f))
                drawCircle(confettiColors[i % confettiColors.size], 5f, Offset(x, y))
            }
        }

        // Draw primary and companion cartoon characters
        val charY = h * 0.62f + gentleFloat
        if (scene.companionCharacter != null) {
            drawCharacterOnCanvas(
                character = scene.primaryCharacter,
                center = Offset(w * 0.38f, charY),
                radius = h * 0.16f,
                mood = scene.characterMood,
                isBlinking = blink
            )
            drawCharacterOnCanvas(
                character = scene.companionCharacter,
                center = Offset(w * 0.63f, h * 0.63f - gentleFloat * 0.7f),
                radius = h * 0.145f,
                mood = "warm",
                isBlinking = blink
            )
        } else {
            drawCharacterOnCanvas(
                character = scene.primaryCharacter,
                center = Offset(w * 0.5f, charY),
                radius = h * 0.17f,
                mood = scene.characterMood,
                isBlinking = blink
            )
        }
    }
}

private fun formatSecs(secs: Int): String {
    val m = secs / 60
    val s = secs % 60
    return "$m:${s.toString().padStart(2, '0')}"
}
