package com.example.data

import com.example.model.AnimatedSceneFrame
import com.example.model.CartoonCharacterId
import com.example.model.CartoonSceneBackdrop
import com.example.model.CartoonStoryVideo
import com.example.model.CollectibleSticker
import com.example.model.DailyDiscoveryPack
import com.example.model.EmotionCatalog
import com.example.model.EmotionGroup
import com.example.model.StoryMusicMood

object PrebuiltContentLibrary {

    val stickers: List<CollectibleSticker> = listOf(
        CollectibleSticker("sunbeam_star", "Sunbeam Star", "Saved for soaking up a bright moment", 0xFFFFD86B),
        CollectibleSticker("paper_boat", "Paper Boat Captain", "Floated gently through a cloudy day", 0xFF8FD3F4),
        CollectibleSticker("calm_cloud", "Calm Cloud Badge", "Took a slow breath when things felt noisy", 0xFFB8E6D2),
        CollectibleSticker("cozy_sprout", "Cozy Sprout", "Grew something small in the Thought Garden", 0xFFAEE59B),
        CollectibleSticker("lantern_keeper", "Lantern Keeper", "Kept a warm spark glowing at dusk", 0xFFFFB28A),
        CollectibleSticker("kite_rider", "Breeze Rider", "Let go of a heavy worry for a minute", 0xFFD2C4FF),
        CollectibleSticker("teacup_pal", "Warm Teacup Pal", "Showed up kindly for yourself today", 0xFFFFC2D4),
        CollectibleSticker("sparkle_note", "Sparkle Note", "Celebrated a tiny win without pressure", 0xFFFFE599)
    )

    val comfortStories: List<CartoonStoryVideo> = listOf(
        CartoonStoryVideo(
            id = "story_paper_boats",
            title = "The Puddle Paper Boats",
            targetEmotions = listOf("sad", "overwhelmed", "disappointed", "stressed"),
            isComfortStory = true,
            totalDurationSeconds = 42,
            musicMood = StoryMusicMood.GENTLE_RAIN_LULLABY,
            scenes = listOf(
                AnimatedSceneFrame(
                    durationSeconds = 8,
                    backdrop = CartoonSceneBackdrop.RAINY_WINDOW_BOATS,
                    primaryCharacter = CartoonCharacterId.KAJU,
                    companionCharacter = CartoonCharacterId.PIP,
                    subtitle = "Kaju's math sheet got splashed in a sudden afternoon downpour.",
                    characterMood = "heavy"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 9,
                    backdrop = CartoonSceneBackdrop.RAINY_WINDOW_BOATS,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.KAJU,
                    subtitle = "Momo sat quietly by the window instead of trying to fix everything at once.",
                    characterMood = "gentle"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 9,
                    backdrop = CartoonSceneBackdrop.RAINY_WINDOW_BOATS,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.PIP,
                    subtitle = "They folded two scrap pages into tiny boats and watched them bob in the puddle.",
                    characterMood = "curious"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 8,
                    backdrop = CartoonSceneBackdrop.SUNNY_BALCONY_CHAI,
                    primaryCharacter = CartoonCharacterId.KAJU,
                    companionCharacter = CartoonCharacterId.MOMO,
                    subtitle = "The rain didn't vanish instantly — it just felt easier with warm tea and dry socks.",
                    characterMood = "calm"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 8,
                    backdrop = CartoonSceneBackdrop.SUNNY_BALCONY_CHAI,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.KAJU,
                    subtitle = "One tough afternoon is only one chapter. Resting right now counts as enough.",
                    characterMood = "warm"
                )
            ),
            takeawayMessage = "Difficult feelings are normal weather, not a grade on who you are. One rough day never defines your whole week."
        ),
        CartoonStoryVideo(
            id = "story_desk_fireflies",
            title = "Fireflies by the Study Lamp",
            targetEmotions = listOf("anxious", "nervous", "stressed", "unmotivated", "tired"),
            isComfortStory = true,
            totalDurationSeconds = 45,
            musicMood = StoryMusicMood.WARM_TERRACE_CHIMES,
            scenes = listOf(
                AnimatedSceneFrame(
                    durationSeconds = 9,
                    backdrop = CartoonSceneBackdrop.COZY_DESK_FIREFLIES,
                    primaryCharacter = CartoonCharacterId.TOTO,
                    companionCharacter = null,
                    subtitle = "Toto stared at a towering pile of notes, wishing tomorrow would pause.",
                    characterMood = "worried"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 9,
                    backdrop = CartoonSceneBackdrop.COZY_DESK_FIREFLIES,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.TOTO,
                    subtitle = "Momo tapped the window: 'Hey, you don't have to carry the whole mountain tonight.'",
                    characterMood = "gentle"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 9,
                    backdrop = CartoonSceneBackdrop.STARRY_TERRACE_LANTERNS,
                    primaryCharacter = CartoonCharacterId.TOTO,
                    companionCharacter = CartoonCharacterId.MOMO,
                    subtitle = "They stepped onto the balcony for just five slow breaths under the evening breeze.",
                    characterMood = "breathing"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 9,
                    backdrop = CartoonSceneBackdrop.COZY_DESK_FIREFLIES,
                    primaryCharacter = CartoonCharacterId.TOTO,
                    companionCharacter = CartoonCharacterId.PIP,
                    subtitle = "Back inside, Toto opened just one page and wrote one tiny heading.",
                    characterMood = "steady"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 9,
                    backdrop = CartoonSceneBackdrop.STARRY_TERRACE_LANTERNS,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.TOTO,
                    subtitle = "Small steps still move you forward. Tomorrow you will be thankful you rested too.",
                    characterMood = "warm"
                )
            ),
            takeawayMessage = "You don't need to solve everything tonight. One tiny step — or closing your eyes for ten minutes — is more than enough."
        ),
        CartoonStoryVideo(
            id = "story_lantern_bench",
            title = "The Quiet Terrace Lantern",
            targetEmotions = listOf("lonely", "confused", "irritated", "angry"),
            isComfortStory = true,
            totalDurationSeconds = 40,
            musicMood = StoryMusicMood.GENTLE_RAIN_LULLABY,
            scenes = listOf(
                AnimatedSceneFrame(
                    durationSeconds = 8,
                    backdrop = CartoonSceneBackdrop.STARRY_TERRACE_LANTERNS,
                    primaryCharacter = CartoonCharacterId.KAJU,
                    companionCharacter = null,
                    subtitle = "Everyone else seemed noisy and busy, while Kaju felt out of sync with the whole day.",
                    characterMood = "heavy"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 8,
                    backdrop = CartoonSceneBackdrop.WINDY_KITE_HILL,
                    primaryCharacter = CartoonCharacterId.PIP,
                    companionCharacter = CartoonCharacterId.KAJU,
                    subtitle = "Pip fluttered onto the railing and let Kaju grumble at the wind without interrupting.",
                    characterMood = "listening"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 8,
                    backdrop = CartoonSceneBackdrop.STARRY_TERRACE_LANTERNS,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.KAJU,
                    subtitle = "Even quiet stars take turns shining. Feeling prickly or disconnected happens to everyone.",
                    characterMood = "gentle"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 8,
                    backdrop = CartoonSceneBackdrop.QUIET_BOOK_NOOK,
                    primaryCharacter = CartoonCharacterId.KAJU,
                    companionCharacter = CartoonCharacterId.MOMO,
                    subtitle = "With soft music and a warm blanket, the noise inside started to settle down.",
                    characterMood = "calm"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 8,
                    backdrop = CartoonSceneBackdrop.STARRY_TERRACE_LANTERNS,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.PIP,
                    subtitle = "You're allowed to take up quiet space whenever you need it.",
                    characterMood = "warm"
                )
            ),
            takeawayMessage = "Feeling lonely or irritated doesn't mean anything is wrong with you. Your feelings are simply asking for a softer pace."
        )
    )

    val celebrationStories: List<CartoonStoryVideo> = listOf(
        CartoonStoryVideo(
            id = "story_sunny_rooftop",
            title = "Bunting & Breezes on the Rooftop",
            targetEmotions = listOf("happy", "excited", "proud", "confident"),
            isComfortStory = false,
            totalDurationSeconds = 38,
            musicMood = StoryMusicMood.SUNNY_CELEBRATION_FLUTE,
            scenes = listOf(
                AnimatedSceneFrame(
                    durationSeconds = 7,
                    backdrop = CartoonSceneBackdrop.SUNNY_BALCONY_CHAI,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.TOTO,
                    subtitle = "Look at that spark! Momo and Toto hung colorful paper flags across the balcony.",
                    characterMood = "cheering"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 8,
                    backdrop = CartoonSceneBackdrop.WINDY_KITE_HILL,
                    primaryCharacter = CartoonCharacterId.TOTO,
                    companionCharacter = CartoonCharacterId.PIP,
                    subtitle = "They launched a bright marigold kite just to celebrate a good moment.",
                    characterMood = "happy"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 8,
                    backdrop = CartoonSceneBackdrop.SUNNY_BALCONY_CHAI,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.KAJU,
                    subtitle = "You don't have to feel sunny every single day — so savoring today's warmth feels extra sweet.",
                    characterMood = "warm"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 7,
                    backdrop = CartoonSceneBackdrop.STARRY_TERRACE_LANTERNS,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.TOTO,
                    subtitle = "Here's a pocket star for your shelf — zero pressure, just high-fives!",
                    characterMood = "cheering"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 8,
                    backdrop = CartoonSceneBackdrop.SUNNY_BALCONY_CHAI,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.PIP,
                    subtitle = "Carry a tiny bit of this light into tomorrow, however tomorrow looks.",
                    characterMood = "happy"
                )
            ),
            takeawayMessage = "Yay! Enjoying good feelings without worrying about keeping them perfect forever makes them even brighter."
        ),
        CartoonStoryVideo(
            id = "story_grateful_garden",
            title = "Tea & Thankful Clouds",
            targetEmotions = listOf("calm", "grateful", "hopeful", "loved"),
            isComfortStory = false,
            totalDurationSeconds = 36,
            musicMood = StoryMusicMood.WARM_TERRACE_CHIMES,
            scenes = listOf(
                AnimatedSceneFrame(
                    durationSeconds = 9,
                    backdrop = CartoonSceneBackdrop.QUIET_BOOK_NOOK,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.KAJU,
                    subtitle = "Momo noticed how peaceful the afternoon felt when nobody rushed.",
                    characterMood = "calm"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 9,
                    backdrop = CartoonSceneBackdrop.SUNNY_BALCONY_CHAI,
                    primaryCharacter = CartoonCharacterId.KAJU,
                    companionCharacter = CartoonCharacterId.PIP,
                    subtitle = "A kind text, a favorite playlist, or a calm breath — small good things add up quietly.",
                    characterMood = "warm"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 9,
                    backdrop = CartoonSceneBackdrop.WINDY_KITE_HILL,
                    primaryCharacter = CartoonCharacterId.TOTO,
                    companionCharacter = CartoonCharacterId.MOMO,
                    subtitle = "Feeling steady and cared for is something worth smiling about.",
                    characterMood = "happy"
                ),
                AnimatedSceneFrame(
                    durationSeconds = 9,
                    backdrop = CartoonSceneBackdrop.SUNNY_BALCONY_CHAI,
                    primaryCharacter = CartoonCharacterId.MOMO,
                    companionCharacter = CartoonCharacterId.TOTO,
                    subtitle = "Tucked a fresh sticker into your meadow scrapbook just for today.",
                    characterMood = "cheering"
                )
            ),
            takeawayMessage = "Calm and grateful moments are quiet superpowers. Thanks for sharing a peaceful pause here."
        )
    )

    val dailyDiscoveryPacks: List<DailyDiscoveryPack> = listOf(
        DailyDiscoveryPack(
            dayIndex = 0,
            character = CartoonCharacterId.MOMO,
            characterGreeting = "Momo brought warm ginger tea and zero homework reminders!",
            storyVideo = comfortStories[0],
            tinyChallengeTitle = "Window Color Hunt",
            tinyChallengeSteps = "Look away from screens for 20 seconds and spot 3 green things around your room or outside.",
            creativePromptTitle = "Doodle a Weather Mood",
            creativePromptBody = "If today's vibe were a silly hat or a cloud shape, what would it look like? Sketch it in Mini Doodle!",
            gratitudePrompt = "Name one small comfort near you right now (socks, music, a snack, or quiet).",
            funnyMiniStoryTitle = "Momo vs. The Stubborn Backpack Zipper",
            funnyMiniStoryBody = "Momo spent four minutes wrestling a stuck zipper before realizing the pocket was already unzipped from the other side. Kaju watched silently with a mug of cocoa and applauded anyway.",
            breathingTitle = "Cloud Float 4-2-6",
            breathingRhythm = "Inhale 4s • Hold 2s • Long slow exhale 6s",
            surpriseSticker = stickers[1],
            surpriseNote = "Surprise! Even paper boats wobble before they glide smoothly."
        ),
        DailyDiscoveryPack(
            dayIndex = 1,
            character = CartoonCharacterId.TOTO,
            characterGreeting = "Toto says: 'Snacks first, giant to-do lists later!'",
            storyVideo = celebrationStories[0],
            tinyChallengeTitle = "Shoulder Drop Check",
            tinyChallengeSteps = "Lift your shoulders up to your ears for 3 seconds, then let them drop completely loose. Repeat twice!",
            creativePromptTitle = "Invent a Cozy Cafe Drink",
            creativePromptBody = "Give a dreamy name to a drink for your mood today (like 'Raindrop Caramel Fizz' or 'Midnight Mint').",
            gratitudePrompt = "Think of one person, pet, or fictional character who made you smile recently.",
            funnyMiniStoryTitle = "Toto's 11 PM Textbook Pillow",
            funnyMiniStoryBody = "Toto opened a history chapter with heroic determination, read half a sentence, and woke up twenty minutes later with the page diagram printed neatly onto his cheek.",
            breathingTitle = "Square Lantern 4-4-4-4",
            breathingRhythm = "Inhale 4s • Hold 4s • Exhale 4s • Rest 4s",
            surpriseSticker = stickers[4],
            surpriseNote = "You unlocked a glowing Lantern Keeper badge for checking out today's meadow!"
        ),
        DailyDiscoveryPack(
            dayIndex = 2,
            character = CartoonCharacterId.KAJU,
            characterGreeting = "Kaju curled up by the window and saved a sunny spot for you.",
            storyVideo = comfortStories[1],
            tinyChallengeTitle = "One-Song Stretch",
            tinyChallengeSteps = "Put on one calm tune in Sound Corner or your headphones and stretch your wrists and neck.",
            creativePromptTitle = "Design a Tiny Door",
            creativePromptBody = "Imagine a secret thumb-sized door at the bottom of your bookshelf. What cozy room is behind it?",
            gratitudePrompt = "What is one thing your body carried you through today (walking to class, writing notes, resting)?",
            funnyMiniStoryTitle = "Pip's Lost Pencil Mystery",
            funnyMiniStoryBody = "Pip searched the entire study desk, the backpack, and under two cushions for a yellow pencil—while holding it in his beak the entire time.",
            breathingTitle = "Pebble Sigh 4-1-5",
            breathingRhythm = "Soft breath in 4s • Pause 1s • Gentle sigh out 5s",
            surpriseSticker = stickers[2],
            surpriseNote = "Pocket fortune: You don't have to feel 100% ready to take one calm step."
        ),
        DailyDiscoveryPack(
            dayIndex = 3,
            character = CartoonCharacterId.PIP,
            characterGreeting = "Pip chirped a tiny good-morning song just off-key on purpose!",
            storyVideo = celebrationStories[1],
            tinyChallengeTitle = "Cool Water Sip",
            tinyChallengeSteps = "Drink half a glass of water slowly and notice how cool it feels. Easy win!",
            creativePromptTitle = "3-Word Day Title",
            creativePromptBody = "If today were an indie cartoon episode, what would its playful 3-word title be?",
            gratitudePrompt = "Name one sound you actually like hearing (rain on roofs, pencil scratching, distant laughter).",
            funnyMiniStoryTitle = "The Great Sock Mismatch",
            funnyMiniStoryBody = "Momo accidentally wore one polka-dot sock and one striped sock to morning assembly and declared it 'Avant-Garde Tuesday' before anyone else noticed.",
            breathingTitle = "Wingbeat Calm 3-3-6",
            breathingRhythm = "Breathe in 3s • Soft hold 3s • Long breeze out 6s",
            surpriseSticker = stickers[0],
            surpriseNote = "Today's pocket star: Being kind to yourself is way cooler than being perfect."
        ),
        DailyDiscoveryPack(
            dayIndex = 4,
            character = CartoonCharacterId.MOMO,
            characterGreeting = "Momo says: 'No rushing in the meadow today!'",
            storyVideo = comfortStories[2],
            tinyChallengeTitle = "Desk Corner Reset",
            tinyChallengeSteps = "Clear just ONE tiny corner of your table or bag—just enough space for a mug or notebook.",
            creativePromptTitle = "Draw Your Safe Corner",
            creativePromptBody = "Sketch a treehouse, pillow fort, or rooftop bench in Mini Doodle using your favorite 3 colors.",
            gratitudePrompt = "What is one small skill you've gotten slightly better at since last year?",
            funnyMiniStoryTitle = "Kaju and the Echoing Classroom",
            funnyMiniStoryBody = "Kaju tried to open a crunchy snack wrapper silently during study hall, which somehow sounded louder than a marching band. Momo pretended to cough in rhythm to cover it.",
            breathingTitle = "Starlight Rest 4-2-6",
            breathingRhythm = "Gentle inhale 4s • Float 2s • Exhale worries 6s",
            surpriseSticker = stickers[3],
            surpriseNote = "Cozy reminder: Growth happens slowly, even underground where nobody sees it."
        ),
        DailyDiscoveryPack(
            dayIndex = 5,
            character = CartoonCharacterId.TOTO,
            characterGreeting = "Toto brought origami paper and extra doodles!",
            storyVideo = comfortStories[0],
            tinyChallengeTitle = "Five-Finger Trace",
            tinyChallengeSteps = "Trace up and down each finger of your hand slowly while matching your breath.",
            creativePromptTitle = "Mascot Sidekick",
            creativePromptBody = "Give Pip the sparrow a ridiculous tiny accessory (sunglasses, scarf, or miniature umbrella).",
            gratitudePrompt = "Recall one funny or unexpected moment from this week.",
            funnyMiniStoryTitle = "The Bus Stop Umbrella Flip",
            funnyMiniStoryBody = "A sudden gust turned Toto's umbrella inside-out like a giant tulip. Instead of panicking, Pip perched inside it like a royal birdbath.",
            breathingTitle = "Breeze Box 4-4-4-4",
            breathingRhythm = "Inhale 4s • Hold 4s • Exhale 4s • Rest 4s",
            surpriseSticker = stickers[5],
            surpriseNote = "Breeze Rider reward unlocked! Let one small worry drift past like a kite."
        )
    )

    val companionPrompts: List<Pair<String, List<String>>> = listOf(
        "Give me a gentle reminder" to listOf(
            "You are allowed to rest even before every single task on your list is checked off.",
            "Bad moods and tough weeks are temporary visitors, not permanent roommates.",
            "Showing up at 20% energy still counts as showing up. Be gentle with yourself today.",
            "Nobody expects you to have life all figured out right now. One small breath is plenty."
        ),
        "Tell me a tiny cozy joke" to listOf(
            "Why did the cloud stay home from school? It was feeling a little mist-understood!",
            "What do you call a sleepy potato before an exam? A couch-tater studying for mashed finals!",
            "Kaju says: If homework is a mountain, I am currently a very round pebble taking a nap at the base.",
            "Why did the pencil get invited to every hangout? Because it always had a good point!"
        ),
        "Help me start a tough task" to listOf(
            "Pick the tiniest possible piece: open the book or document, read just TWO lines, and see how you feel.",
            "Set a 2-minute 'messy start' rule — you're allowed to stop after 2 minutes with zero guilt.",
            "Drop your shoulders, take a sip of water, and do just the easiest 10% first to warm up.",
            "Pretend you're helping Momo get started instead of yourself — speak to yourself like a friend!"
        ),
        "Just sit quietly with me" to listOf(
            "Momo pulls up a warm cushion next to you. No advice, no checklists — just quiet company.",
            "Sometimes words are too loud. Let's just listen to the rain or watch the cloud breathe together.",
            "You don't need to explain why today feels heavy. We can just sit in this cozy corner for a minute.",
            "Taking a quiet pause right now is something to feel glad about."
        )
    )

    fun storyForEmotions(selectedIds: List<String>, daySeed: Int): CartoonStoryVideo {
        val emotions = selectedIds.map { EmotionCatalog.byId(it) }.filterNotNull()
        val hasHeavy = emotions.any { it.group == com.example.model.EmotionGroup.HEAVY }
        val pool = if (hasHeavy) comfortStories else celebrationStories
        val matched = pool.filter { story -> story.targetEmotions.any { it in selectedIds } }
        val candidates = if (matched.isNotEmpty()) matched else pool
        return candidates[Math.floorMod(daySeed, candidates.size)]
    }

    fun discoveryForDate(dayOfYear: Int, avoidDayIndex: Int?): DailyDiscoveryPack {
        val total = dailyDiscoveryPacks.size
        var idx = Math.floorMod(dayOfYear, total)
        if (avoidDayIndex != null && idx == avoidDayIndex && total > 1) {
            idx = (idx + 1) % total
        }
        return dailyDiscoveryPacks[idx]
    }
}
