package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.SupportAgent
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.audio.AmbientSoundEngine
import com.example.audio.StoryVoiceNarrator
import com.example.data.PrebuiltContentLibrary
import com.example.model.CartoonCharacterId
import com.example.model.EmotionCatalog
import com.example.model.EmotionGroup
import com.example.ui.MeadowUiState
import com.example.ui.components.AnimatedCartoonBuddy
import com.example.ui.components.CartoonEmotionFace
import com.example.ui.components.CartoonVideoPlayer
import com.example.ui.components.MoodCompanionMessages

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyResponseScreen(
    uiState: MeadowUiState,
    soundEngine: AmbientSoundEngine,
    voiceNarrator: StoryVoiceNarrator,
    onToggleVoice: (Boolean) -> Unit,
    onToggleSubtitles: (Boolean) -> Unit,
    onOpenRelaxTinyReset: () -> Unit,
    onShowSupportSheet: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    val savedEmotions = remember(uiState.todayEntry, uiState.draftEmotionIds) {
        val csv = uiState.todayEntry?.emotionIdsCsv
        if (!csv.isNullOrBlank()) {
            EmotionCatalog.fromCsv(csv)
        } else {
            uiState.draftEmotionIds.mapNotNull { EmotionCatalog.byId(it) }
        }
    }
    val hasHeavy = savedEmotions.any { it.group == EmotionGroup.HEAVY }
    val hasBright = savedEmotions.any { it.group == EmotionGroup.BRIGHT }
    val story = uiState.lastSavedStory ?: PrebuiltContentLibrary.comfortStories.first()
    val sticker = PrebuiltContentLibrary.stickers.find { it.id == uiState.lastSavedStickerId }
        ?: PrebuiltContentLibrary.stickers.first()

    val headline = when {
        hasHeavy && hasBright -> "Mixed feelings are totally valid"
        hasHeavy -> "You don't have to carry today alone"
        else -> "Yay—let's celebrate this bright spark!"
    }

    val companionMessage = remember(hasHeavy, hasBright, savedEmotions) {
        when {
            hasHeavy && hasBright ->
                "It's completely normal to feel both ${savedEmotions.first().label.lowercase()} and ${savedEmotions.last().label.lowercase()} at once. Let's take a soft moment together."
            hasHeavy ->
                MoodCompanionMessages.comfortingMessages.first()
            else ->
                MoodCompanionMessages.celebrationMessages.first()
        }
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("response_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Your Daily Response",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Warm banner with story illustration & cheering/comforting character
            item {
                Surface(
                    shape = RoundedCornerShape(28.dp),
                    color = if (hasHeavy) {
                        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.72f)
                    } else {
                        Color(0xFFFFF2BD)
                    },
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column {
                        Image(
                            painter = painterResource(
                                id = if (hasHeavy) R.drawable.img_story_rainy_window else R.drawable.img_story_celebration
                            ),
                            contentDescription = if (hasHeavy) "Cozy rainy window buddies" else "Sunny terrace celebration",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(160.dp)
                                .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimatedCartoonBuddy(
                                character = if (hasHeavy) CartoonCharacterId.MOMO else CartoonCharacterId.TOTO,
                                mood = if (hasHeavy) "gentle" else "cheering",
                                reducedMotion = uiState.settings.reducedMotion,
                                size = 72.dp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = headline,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color(0xFF232533)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = companionMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = Color(0xFF3E4257)
                                )
                            }
                        }
                    }
                }
            }

            // Selected emotions summary pills
            if (savedEmotions.isNotEmpty()) {
                item {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        savedEmotions.forEach { emo ->
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = emo.cardColor,
                                border = BorderStroke(1.dp, emo.accentColor.copy(alpha = 0.5f))
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
                                    Text(
                                        text = emo.label,
                                        style = MaterialTheme.typography.labelLarge,
                                        color = Color(0xFF232533)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Interactive 35-45s 2D Cartoon Video Player
            item {
                CartoonVideoPlayer(
                    story = story,
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

            // Virtual Collectible Sticker Reward
            item {
                Surface(
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(sticker.colorHex),
                            modifier = Modifier.size(56.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (hasHeavy) Icons.Rounded.AutoAwesome else Icons.Rounded.Star,
                                    contentDescription = null,
                                    tint = Color(0xFF232533),
                                    modifier = Modifier.size(28.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Pocket Sticker Earned: ${sticker.name}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = sticker.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Next gentle steps
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onOpenRelaxTinyReset,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .testTag("response_tiny_reset_button"),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(Icons.Rounded.Bolt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Try a Tiny Reset")
                    }
                    if (hasHeavy) {
                        OutlinedButton(
                            onClick = onShowSupportSheet,
                            modifier = Modifier
                                .height(50.dp)
                                .testTag("response_support_button"),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(Icons.Rounded.SupportAgent, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Support")
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
