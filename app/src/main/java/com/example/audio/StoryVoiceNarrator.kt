package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.speech.tts.TextToSpeech
import java.util.Locale

class StoryVoiceNarrator(context: Context) {
    private var tts: TextToSpeech? = null
    @Volatile
    private var isReady: Boolean = false
    private var pendingLine: Triple<String, String, Boolean>? = null

    init {
        try {
            tts = TextToSpeech(context.applicationContext) { status ->
                if (status == TextToSpeech.SUCCESS) {
                    val engine = tts ?: return@TextToSpeech
                    val langResult = engine.setLanguage(Locale.US)
                    if (langResult == TextToSpeech.LANG_MISSING_DATA || langResult == TextToSpeech.LANG_NOT_SUPPORTED) {
                        engine.setLanguage(Locale.getDefault())
                    }
                    engine.setAudioAttributes(
                        AudioAttributes.Builder()
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                            .build()
                    )
                    isReady = true
                    pendingLine?.let { (id, text, isComfort) ->
                        pendingLine = null
                        speakScene(id, text, isComfort)
                    }
                }
            }
        } catch (_: Throwable) {
            isReady = false
        }
    }

    fun speakScene(utteranceId: String, text: String, isComfortStory: Boolean) {
        if (text.isBlank()) return
        if (!isReady) {
            pendingLine = Triple(utteranceId, text, isComfortStory)
            return
        }
        try {
            val engine = tts ?: return
            engine.setSpeechRate(if (isComfortStory) 0.90f else 0.97f)
            engine.setPitch(if (isComfortStory) 1.02f else 1.08f)
            engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
        } catch (_: Throwable) {
        }
    }

    fun stop() {
        pendingLine = null
        try {
            tts?.stop()
        } catch (_: Throwable) {
        }
    }

    fun release() {
        pendingLine = null
        try {
            tts?.stop()
            tts?.shutdown()
        } catch (_: Throwable) {
        }
        tts = null
        isReady = false
    }
}
