package com.example.model

import androidx.compose.ui.graphics.Color

enum class EmotionGroup {
    BRIGHT,
    HEAVY
}

enum class CartoonFaceStyle {
    HAPPY,
    EXCITED,
    CALM,
    GRATEFUL,
    PROUD,
    HOPEFUL,
    LOVED,
    CONFIDENT,
    TIRED,
    STRESSED,
    ANXIOUS,
    OVERWHELMED,
    SAD,
    LONELY,
    IRRITATED,
    ANGRY,
    CONFUSED,
    DISAPPOINTED,
    NERVOUS,
    UNMOTIVATED
}

data class EmotionItem(
    val id: String,
    val label: String,
    val group: EmotionGroup,
    val cardColor: Color,
    val accentColor: Color,
    val faceStyle: CartoonFaceStyle,
    val gentleHint: String
)

object EmotionCatalog {
    val all: List<EmotionItem> = listOf(
        EmotionItem(
            id = "happy",
            label = "Happy",
            group = EmotionGroup.BRIGHT,
            cardColor = Color(0xFFFFF0B8),
            accentColor = Color(0xFFE09112),
            faceStyle = CartoonFaceStyle.HAPPY,
            gentleHint = "Light, cheerful, or smiling inside"
        ),
        EmotionItem(
            id = "excited",
            label = "Excited",
            group = EmotionGroup.BRIGHT,
            cardColor = Color(0xFFFFE2CC),
            accentColor = Color(0xFFE56B38),
            faceStyle = CartoonFaceStyle.EXCITED,
            gentleHint = "Buzzy energy for something coming up"
        ),
        EmotionItem(
            id = "calm",
            label = "Calm",
            group = EmotionGroup.BRIGHT,
            cardColor = Color(0xFFD7F2E3),
            accentColor = Color(0xFF288A6B),
            faceStyle = CartoonFaceStyle.CALM,
            gentleHint = "Steady, peaceful, or breathing easy"
        ),
        EmotionItem(
            id = "grateful",
            label = "Grateful",
            group = EmotionGroup.BRIGHT,
            cardColor = Color(0xFFFFE5EE),
            accentColor = Color(0xFFD14D79),
            faceStyle = CartoonFaceStyle.GRATEFUL,
            gentleHint = "Appreciating a small good moment"
        ),
        EmotionItem(
            id = "proud",
            label = "Proud",
            group = EmotionGroup.BRIGHT,
            cardColor = Color(0xFFE5ECFF),
            accentColor = Color(0xFF4267B2),
            faceStyle = CartoonFaceStyle.PROUD,
            gentleHint = "You showed up or finished something!"
        ),
        EmotionItem(
            id = "hopeful",
            label = "Hopeful",
            group = EmotionGroup.BRIGHT,
            cardColor = Color(0xFFDDF3FA),
            accentColor = Color(0xFF2C82A6),
            faceStyle = CartoonFaceStyle.HOPEFUL,
            gentleHint = "Looking forward to better things"
        ),
        EmotionItem(
            id = "loved",
            label = "Loved",
            group = EmotionGroup.BRIGHT,
            cardColor = Color(0xFFFFDFEB),
            accentColor = Color(0xFFD64571),
            faceStyle = CartoonFaceStyle.LOVED,
            gentleHint = "Cared for, connected, or supported"
        ),
        EmotionItem(
            id = "confident",
            label = "Confident",
            group = EmotionGroup.BRIGHT,
            cardColor = Color(0xFFE6F4D7),
            accentColor = Color(0xFF4F8A2B),
            faceStyle = CartoonFaceStyle.CONFIDENT,
            gentleHint = "Trusting yourself today"
        ),
        EmotionItem(
            id = "tired",
            label = "Tired",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFE8E9F3),
            accentColor = Color(0xFF5E6488),
            faceStyle = CartoonFaceStyle.TIRED,
            gentleHint = "Low battery, sleepy, or drained"
        ),
        EmotionItem(
            id = "stressed",
            label = "Stressed",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFFFDFD6),
            accentColor = Color(0xFFC94B38),
            faceStyle = CartoonFaceStyle.STRESSED,
            gentleHint = "Exams, deadlines, or too much pressure"
        ),
        EmotionItem(
            id = "anxious",
            label = "Anxious",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFEAE3FF),
            accentColor = Color(0xFF6B52AE),
            faceStyle = CartoonFaceStyle.ANXIOUS,
            gentleHint = "Racing thoughts or butterflies"
        ),
        EmotionItem(
            id = "overwhelmed",
            label = "Overwhelmed",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFDFEAFF),
            accentColor = Color(0xFF4764A6),
            faceStyle = CartoonFaceStyle.OVERWHELMED,
            gentleHint = "Everything piling up at once"
        ),
        EmotionItem(
            id = "sad",
            label = "Sad",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFD9EBFA),
            accentColor = Color(0xFF3E6F9E),
            faceStyle = CartoonFaceStyle.SAD,
            gentleHint = "Heavy-hearted or wanting a quiet cry"
        ),
        EmotionItem(
            id = "lonely",
            label = "Lonely",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFE5E6F7),
            accentColor = Color(0xFF5D609C),
            faceStyle = CartoonFaceStyle.LONELY,
            gentleHint = "Missing connection or feeling left out"
        ),
        EmotionItem(
            id = "irritated",
            label = "Irritated",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFFFE4CE),
            accentColor = Color(0xFFD16526),
            faceStyle = CartoonFaceStyle.IRRITATED,
            gentleHint = "Everything feels a little prickly"
        ),
        EmotionItem(
            id = "angry",
            label = "Angry",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFFFD8D8),
            accentColor = Color(0xFFC23B3B),
            faceStyle = CartoonFaceStyle.ANGRY,
            gentleHint = "Frustrated or needing to let steam out"
        ),
        EmotionItem(
            id = "confused",
            label = "Confused",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFF3E6FF),
            accentColor = Color(0xFF7B50A3),
            faceStyle = CartoonFaceStyle.CONFUSED,
            gentleHint = "Not sure what to think or do next"
        ),
        EmotionItem(
            id = "disappointed",
            label = "Disappointed",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFE2ECF2),
            accentColor = Color(0xFF4E6D82),
            faceStyle = CartoonFaceStyle.DISAPPOINTED,
            gentleHint = "Something didn't go how you hoped"
        ),
        EmotionItem(
            id = "nervous",
            label = "Nervous",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFFFF0D4),
            accentColor = Color(0xFFBF7D24),
            faceStyle = CartoonFaceStyle.NERVOUS,
            gentleHint = "Jittery before a class, talk, or moment"
        ),
        EmotionItem(
            id = "unmotivated",
            label = "Unmotivated",
            group = EmotionGroup.HEAVY,
            cardColor = Color(0xFFECEAE6),
            accentColor = Color(0xFF6B675E),
            faceStyle = CartoonFaceStyle.UNMOTIVATED,
            gentleHint = "Hard to get started, and that's okay"
        )
    )

    fun byId(id: String): EmotionItem? = all.find { it.id == id }

    fun fromCsv(csv: String): List<EmotionItem> {
        if (csv.isBlank()) return emptyList()
        return csv.split(",")
            .mapNotNull { byId(it.trim()) }
            .distinctBy { it.id }
            .take(4)
    }
}
