package com.example.model

enum class CartoonSceneBackdrop {
    RAINY_WINDOW_BOATS,
    SUNNY_BALCONY_CHAI,
    STARRY_TERRACE_LANTERNS,
    COZY_DESK_FIREFLIES,
    WINDY_KITE_HILL,
    QUIET_BOOK_NOOK
}

enum class CartoonCharacterId(
    val displayName: String,
    val roleTag: String
) {
    MOMO("Momo", "Cloud-Sprout Buddy"),
    KAJU("Kaju", "Pebble-Cat"),
    PIP("Pip", "Pocket Sparrow"),
    TOTO("Toto", "Lantern Fox")
}

data class AnimatedSceneFrame(
    val durationSeconds: Int,
    val backdrop: CartoonSceneBackdrop,
    val primaryCharacter: CartoonCharacterId,
    val companionCharacter: CartoonCharacterId?,
    val subtitle: String,
    val characterMood: String
)

data class CartoonStoryVideo(
    val id: String,
    val title: String,
    val targetEmotions: List<String>,
    val isComfortStory: Boolean,
    val totalDurationSeconds: Int,
    val musicMood: StoryMusicMood,
    val scenes: List<AnimatedSceneFrame>,
    val takeawayMessage: String
)

enum class StoryMusicMood {
    GENTLE_RAIN_LULLABY,
    WARM_TERRACE_CHIMES,
    SUNNY_CELEBRATION_FLUTE
}

data class CollectibleSticker(
    val id: String,
    val name: String,
    val description: String,
    val colorHex: Long
)

data class DailyDiscoveryPack(
    val dayIndex: Int,
    val character: CartoonCharacterId,
    val characterGreeting: String,
    val storyVideo: CartoonStoryVideo,
    val tinyChallengeTitle: String,
    val tinyChallengeSteps: String,
    val creativePromptTitle: String,
    val creativePromptBody: String,
    val gratitudePrompt: String,
    val funnyMiniStoryTitle: String,
    val funnyMiniStoryBody: String,
    val breathingTitle: String,
    val breathingRhythm: String,
    val surpriseSticker: CollectibleSticker,
    val surpriseNote: String
)
