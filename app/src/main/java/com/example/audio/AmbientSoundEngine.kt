package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import com.example.model.StoryMusicMood
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin
import kotlin.random.Random

enum class AmbientTrack(val title: String, val subtitle: String) {
    RAIN("Gentle Rain", "Soft droplets on a cozy window"),
    WIND("Meadow Wind", "Slow warm afternoon breeze"),
    BIRDS("Morning Birds", "Cheerful distant garden chirps"),
    MUSIC_BOX("Soft Chimes", "Gentle warm lullaby notes")
}

class AmbientSoundEngine {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var playJob: Job? = null
    private var activeTrack: AmbientTrack? = null
    @Volatile
    private var volume: Float = 0.6f
    @Volatile
    private var soundEnabled: Boolean = true
    @Volatile
    private var duckedForVoice: Boolean = false

    fun updateSettings(enabled: Boolean, newVolume: Float) {
        soundEnabled = enabled
        volume = newVolume.coerceIn(0f, 1f)
        if (!enabled) {
            stopAll()
        }
    }

    fun setDuckingForVoice(ducked: Boolean) {
        duckedForVoice = ducked
    }

    fun startAmbient(track: AmbientTrack) {
        if (!soundEnabled) return
        if (activeTrack == track && playJob?.isActive == true) return
        stopAll()
        activeTrack = track
        playJob = scope.launch {
            runSynthesisLoop(track)
        }
    }

    fun startStoryMusic(mood: StoryMusicMood) {
        val mapped = when (mood) {
            StoryMusicMood.GENTLE_RAIN_LULLABY -> AmbientTrack.RAIN
            StoryMusicMood.WARM_TERRACE_CHIMES -> AmbientTrack.MUSIC_BOX
            StoryMusicMood.SUNNY_CELEBRATION_FLUTE -> AmbientTrack.BIRDS
        }
        startAmbient(mapped)
    }

    fun playSoftPop() {
        if (!soundEnabled) return
        scope.launch {
            playToneBurst(freq = 540.0, durationMs = 75, amp = 0.25f * volume)
        }
    }

    fun playGentleChime() {
        if (!soundEnabled) return
        scope.launch {
            playToneBurst(freq = 523.25, durationMs = 180, amp = 0.22f * volume)
            playToneBurst(freq = 659.25, durationMs = 240, amp = 0.20f * volume)
        }
    }

    fun stopAll() {
        activeTrack = null
        playJob?.cancel()
        playJob = null
    }

    private fun CoroutineScope.runSynthesisLoop(track: AmbientTrack) {
        val sampleRate = 22050
        val bufferFrames = 2048
        val minBuf = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ).coerceAtLeast(bufferFrames * 2)

        val audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()
            )
            .setBufferSizeInBytes(minBuf)
            .setTransferMode(AudioTrack.MODE_STREAM)
            .build()

        val pcm = ShortArray(bufferFrames)
        val pentatonic = doubleArrayOf(261.63, 293.66, 329.63, 392.00, 440.00, 523.25)
        var brown = 0.0
        var phase = 0.0
        var notePhase = 0.0
        var noteFreq = pentatonic[0]
        var noteSamples = 0
        var sampleIndex = 0L
        var birdTimer = 0
        var birdFreq = 1400.0
        var birdEnv = 0.0

        try {
            audioTrack.play()
            while (isActive && soundEnabled) {
                val vol = volume * if (duckedForVoice) 0.20f else 0.45f
                for (i in 0 until bufferFrames) {
                    val t = sampleIndex.toDouble() / sampleRate
                    val raw = when (track) {
                        AmbientTrack.RAIN -> {
                            val white = Random.nextDouble(-1.0, 1.0)
                            brown = (brown + 0.04 * white).coerceIn(-1.0, 1.0)
                            val drop = if (Random.nextInt(3200) == 0) 0.35 else 0.0
                            (brown * 0.55 + white * 0.08 + drop * 0.25)
                        }
                        AmbientTrack.WIND -> {
                            val white = Random.nextDouble(-1.0, 1.0)
                            brown = (brown * 0.96 + white * 0.04).coerceIn(-1.0, 1.0)
                            val swell = 0.4 + 0.6 * ((sin(2 * PI * 0.14 * t) + 1.0) / 2.0)
                            brown * swell * 0.85
                        }
                        AmbientTrack.BIRDS -> {
                            val white = Random.nextDouble(-1.0, 1.0)
                            brown = (brown * 0.95 + white * 0.03).coerceIn(-1.0, 1.0)
                            if (birdTimer <= 0 && Random.nextInt(18000) == 0) {
                                birdTimer = sampleRate / 5
                                birdFreq = 1150.0 + Random.nextDouble(0.0, 650.0)
                            }
                            val chirp = if (birdTimer > 0) {
                                birdTimer--
                                birdEnv = sin(PI * birdTimer / (sampleRate / 5.0))
                                phase += 2 * PI * (birdFreq + 160 * sin(t * 24)) / sampleRate
                                sin(phase) * birdEnv * 0.22
                            } else 0.0
                            brown * 0.35 + chirp
                        }
                        AmbientTrack.MUSIC_BOX -> {
                            if (noteSamples <= 0) {
                                noteSamples = (sampleRate * 0.95).toInt()
                                noteFreq = pentatonic[Random.nextInt(pentatonic.size)]
                                notePhase = 0.0
                            }
                            val progress = 1.0 - (noteSamples.toDouble() / (sampleRate * 0.95))
                            val env = exp(-progress * 4.2) * (1.0 - exp(-progress * 45.0))
                            notePhase += 2 * PI * noteFreq / sampleRate
                            noteSamples--
                            (sin(notePhase) + 0.25 * sin(notePhase * 2.0)) * env * 0.45
                        }
                    }
                    val scaled = (raw * vol * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt())
                    pcm[i] = scaled.toShort()
                    sampleIndex++
                }
                audioTrack.write(pcm, 0, pcm.size)
            }
        } catch (_: Throwable) {
            // Safe fallback if audio hardware is unavailable in headless/test environments
        } finally {
            try {
                audioTrack.stop()
                audioTrack.release()
            } catch (_: Throwable) {
            }
        }
    }

    private fun playToneBurst(freq: Double, durationMs: Int, amp: Float) {
        val sampleRate = 22050
        val count = (sampleRate * durationMs) / 1000
        val pcm = ShortArray(count)
        for (i in 0 until count) {
            val env = exp(-4.5 * i / count)
            val s = sin(2 * PI * freq * i / sampleRate) * env * amp
            pcm[i] = (s * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        try {
            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(pcm.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()
            track.write(pcm, 0, pcm.size)
            track.play()
            Thread.sleep(durationMs.toLong() + 30)
            track.stop()
            track.release()
        } catch (_: Throwable) {
        }
    }
}
