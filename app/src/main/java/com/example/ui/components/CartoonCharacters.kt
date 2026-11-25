package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.model.CartoonCharacterId
import com.example.model.CartoonFaceStyle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

object MoodCompanionMessages {
    val homeWelcomeMessages = listOf(
        "Hey friend! No grades, no streaks, no pressure here—just a soft spot to breathe.",
        "Whether today felt huge or totally exhausting, I'm really glad you stopped by.",
        "Momo saved you a warm cup of cocoa. Ready for a quick 10-second check-in?",
        "Your feelings don't have to be tidy or cheerful to belong here.",
        "Even small pauses count! Want to pop a few bubbles or check in with how today felt?"
    )

    val comfortingMessages = listOf(
        "One heavy day does not erase all the good things ahead of you.",
        "You don't have to solve next week tonight. Just taking the next tiny breath is enough.",
        "Feeling stressed or worn out means you're human—not failing.",
        "If homework or noise feels too loud right now, try a 60-second Tiny Reset with me.",
        "It's completely okay if today's biggest win was just getting through the afternoon."
    )

    val celebrationMessages = listOf(
        "Look at that spark! Let's enjoy this bright feeling—zero pressure to force it tomorrow.",
        "Momo is doing a tiny happy wiggle! Celebrating small good moments matters.",
        "Quiet calm and small proud moments are worth tucking into your pocket.",
        "Yay! You noticed something good today—here's a cheerful wave from the meadow."
    )
}

@Composable
fun CartoonEmotionFace(
    faceStyle: CartoonFaceStyle,
    baseColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp
) {
    val outlineColor = Color(0xFF2B2D42)
    Canvas(
        modifier = modifier
            .size(size)
            .semantics { contentDescription = "Cartoon emotion face for ${faceStyle.name.lowercase()}" }
    ) {
        val w = this.size.width
        val h = this.size.height
        val cx = w / 2f
        val cy = h / 2f
        val r = minOf(w, h) * 0.42f
        val strokeW = r * 0.11f

        // Soft blob/circle head
        drawCircle(color = baseColor, radius = r, center = Offset(cx, cy))
        drawCircle(
            color = outlineColor,
            radius = r,
            center = Offset(cx, cy),
            style = Stroke(width = strokeW)
        )

        // Rosy cheeks
        val cheekColor = Color(0xFFFF9AA2).copy(alpha = 0.65f)
        drawCircle(cheekColor, radius = r * 0.16f, center = Offset(cx - r * 0.46f, cy + r * 0.16f))
        drawCircle(cheekColor, radius = r * 0.16f, center = Offset(cx + r * 0.46f, cy + r * 0.16f))

        val eyeY = cy - r * 0.12f
        val eyeDx = r * 0.32f
        val eyeR = r * 0.09f

        when (faceStyle) {
            CartoonFaceStyle.HAPPY, CartoonFaceStyle.PROUD, CartoonFaceStyle.CONFIDENT -> {
                drawCircle(outlineColor, eyeR, Offset(cx - eyeDx, eyeY))
                drawCircle(outlineColor, eyeR, Offset(cx + eyeDx, eyeY))
                // Happy arc smile
                drawArc(
                    color = outlineColor,
                    startAngle = 15f,
                    sweepAngle = 150f,
                    useCenter = false,
                    topLeft = Offset(cx - r * 0.28f, cy - r * 0.04f),
                    size = Size(r * 0.56f, r * 0.36f),
                    style = Stroke(width = strokeW, cap = StrokeCap.Round)
                )
                if (faceStyle == CartoonFaceStyle.PROUD) {
                    // Little brow boost
                    drawLine(outlineColor, Offset(cx - eyeDx - r * 0.12f, eyeY - r * 0.22f), Offset(cx - eyeDx + r * 0.1f, eyeY - r * 0.28f), strokeW * 0.8f, StrokeCap.Round)
                    drawLine(outlineColor, Offset(cx + eyeDx - r * 0.1f, eyeY - r * 0.28f), Offset(cx + eyeDx + r * 0.12f, eyeY - r * 0.22f), strokeW * 0.8f, StrokeCap.Round)
                }
            }
            CartoonFaceStyle.EXCITED -> {
                // Starry eyes
                drawStarSimple(Offset(cx - eyeDx, eyeY), r * 0.15f, accentColor)
                drawStarSimple(Offset(cx + eyeDx, eyeY), r * 0.15f, accentColor)
                // Big open smile
                drawArc(
                    color = outlineColor,
                    startAngle = 0f,
                    sweepAngle = 180f,
                    useCenter = true,
                    topLeft = Offset(cx - r * 0.28f, cy + r * 0.04f),
                    size = Size(r * 0.56f, r * 0.34f)
                )
            }
            CartoonFaceStyle.CALM, CartoonFaceStyle.GRATEFUL -> {
                // Peaceful closed smile-eyes
                drawArc(outlineColor, 200f, 140f, false, Offset(cx - eyeDx - r * 0.13f, eyeY - r * 0.06f), Size(r * 0.26f, r * 0.2f), style = Stroke(strokeW, cap = StrokeCap.Round))
                drawArc(outlineColor, 200f, 140f, false, Offset(cx + eyeDx - r * 0.13f, eyeY - r * 0.06f), Size(r * 0.26f, r * 0.2f), style = Stroke(strokeW, cap = StrokeCap.Round))
                drawArc(outlineColor, 20f, 140f, false, Offset(cx - r * 0.22f, cy + r * 0.02f), Size(r * 0.44f, r * 0.26f), style = Stroke(strokeW, cap = StrokeCap.Round))
            }
            CartoonFaceStyle.LOVED, CartoonFaceStyle.HOPEFUL -> {
                drawCircle(outlineColor, eyeR * 1.1f, Offset(cx - eyeDx, eyeY))
                drawCircle(outlineColor, eyeR * 1.1f, Offset(cx + eyeDx, eyeY))
                drawCircle(Color.White, eyeR * 0.4f, Offset(cx - eyeDx - eyeR * 0.25f, eyeY - eyeR * 0.25f))
                drawCircle(Color.White, eyeR * 0.4f, Offset(cx + eyeDx - eyeR * 0.25f, eyeY - eyeR * 0.25f))
                drawArc(outlineColor, 15f, 150f, false, Offset(cx - r * 0.22f, cy + r * 0.04f), Size(r * 0.44f, r * 0.26f), style = Stroke(strokeW, cap = StrokeCap.Round))
                if (faceStyle == CartoonFaceStyle.LOVED) {
                    drawCircle(Color(0xFFE94E77), r * 0.11f, Offset(cx + r * 0.65f, cy - r * 0.58f))
                }
            }
            CartoonFaceStyle.TIRED, CartoonFaceStyle.UNMOTIVATED -> {
                // Half-lidded sleepy eyes
                drawLine(outlineColor, Offset(cx - eyeDx - r * 0.12f, eyeY), Offset(cx - eyeDx + r * 0.12f, eyeY), strokeW, StrokeCap.Round)
                drawLine(outlineColor, Offset(cx + eyeDx - r * 0.12f, eyeY), Offset(cx + eyeDx + r * 0.12f, eyeY), strokeW, StrokeCap.Round)
                drawLine(outlineColor, Offset(cx - r * 0.18f, cy + r * 0.24f), Offset(cx + r * 0.18f, cy + r * 0.24f), strokeW, StrokeCap.Round)
            }
            CartoonFaceStyle.STRESSED, CartoonFaceStyle.OVERWHELMED -> {
                // Curved worried eyes + wavy mouth + sweat drop
                drawLine(outlineColor, Offset(cx - eyeDx - r * 0.1f, eyeY - r * 0.06f), Offset(cx - eyeDx + r * 0.1f, eyeY + r * 0.04f), strokeW, StrokeCap.Round)
                drawLine(outlineColor, Offset(cx - eyeDx - r * 0.1f, eyeY + r * 0.08f), Offset(cx - eyeDx + r * 0.1f, eyeY + r * 0.04f), strokeW, StrokeCap.Round)
                drawLine(outlineColor, Offset(cx + eyeDx + r * 0.1f, eyeY - r * 0.06f), Offset(cx + eyeDx - r * 0.1f, eyeY + r * 0.04f), strokeW, StrokeCap.Round)
                drawLine(outlineColor, Offset(cx + eyeDx + r * 0.1f, eyeY + r * 0.08f), Offset(cx + eyeDx - r * 0.1f, eyeY + r * 0.04f), strokeW, StrokeCap.Round)
                // Wavy mouth
                val wave = Path().apply {
                    moveTo(cx - r * 0.26f, cy + r * 0.24f)
                    quadraticTo(cx - r * 0.1f, cy + r * 0.14f, cx, cy + r * 0.24f)
                    quadraticTo(cx + r * 0.1f, cy + r * 0.34f, cx + r * 0.26f, cy + r * 0.24f)
                }
                drawPath(wave, outlineColor, style = Stroke(strokeW, cap = StrokeCap.Round))
                // Sweat droplet
                drawCircle(Color(0xFF6EC1E4), r * 0.11f, Offset(cx + r * 0.68f, cy - r * 0.42f))
            }
            CartoonFaceStyle.ANXIOUS, CartoonFaceStyle.NERVOUS -> {
                drawCircle(outlineColor, eyeR * 1.15f, Offset(cx - eyeDx, eyeY))
                drawCircle(outlineColor, eyeR * 1.15f, Offset(cx + eyeDx, eyeY))
                // Raised worried brows
                drawLine(outlineColor, Offset(cx - eyeDx - r * 0.14f, eyeY - r * 0.2f), Offset(cx - eyeDx + r * 0.1f, eyeY - r * 0.28f), strokeW * 0.85f, StrokeCap.Round)
                drawLine(outlineColor, Offset(cx + eyeDx - r * 0.1f, eyeY - r * 0.28f), Offset(cx + eyeDx + r * 0.14f, eyeY - r * 0.2f), strokeW * 0.85f, StrokeCap.Round)
                drawOval(outlineColor, Offset(cx - r * 0.12f, cy + r * 0.16f), Size(r * 0.24f, r * 0.16f), style = Stroke(strokeW * 0.9f))
            }
            CartoonFaceStyle.SAD, CartoonFaceStyle.LONELY, CartoonFaceStyle.DISAPPOINTED -> {
                drawCircle(outlineColor, eyeR, Offset(cx - eyeDx, eyeY))
                drawCircle(outlineColor, eyeR, Offset(cx + eyeDx, eyeY))
                // Sad brows
                drawLine(outlineColor, Offset(cx - eyeDx - r * 0.12f, eyeY - r * 0.16f), Offset(cx - eyeDx + r * 0.1f, eyeY - r * 0.24f), strokeW * 0.8f, StrokeCap.Round)
                drawLine(outlineColor, Offset(cx + eyeDx - r * 0.1f, eyeY - r * 0.24f), Offset(cx + eyeDx + r * 0.12f, eyeY - r * 0.16f), strokeW * 0.8f, StrokeCap.Round)
                // Gentle frown
                drawArc(outlineColor, 200f, 140f, false, Offset(cx - r * 0.24f, cy + r * 0.16f), Size(r * 0.48f, r * 0.26f), style = Stroke(strokeW, cap = StrokeCap.Round))
                if (faceStyle == CartoonFaceStyle.SAD) {
                    drawCircle(Color(0xFF72B5E8), r * 0.08f, Offset(cx - eyeDx, eyeY + r * 0.2f))
                }
            }
            CartoonFaceStyle.IRRITATED, CartoonFaceStyle.ANGRY -> {
                drawCircle(outlineColor, eyeR, Offset(cx - eyeDx, eyeY + r * 0.04f))
                drawCircle(outlineColor, eyeR, Offset(cx + eyeDx, eyeY + r * 0.04f))
                // Slanted brows
                drawLine(outlineColor, Offset(cx - eyeDx - r * 0.12f, eyeY - r * 0.22f), Offset(cx - eyeDx + r * 0.14f, eyeY - r * 0.1f), strokeW * 0.95f, StrokeCap.Round)
                drawLine(outlineColor, Offset(cx + eyeDx - r * 0.14f, eyeY - r * 0.1f), Offset(cx + eyeDx + r * 0.12f, eyeY - r * 0.22f), strokeW * 0.95f, StrokeCap.Round)
                drawArc(outlineColor, 195f, 150f, false, Offset(cx - r * 0.22f, cy + r * 0.2f), Size(r * 0.44f, r * 0.2f), style = Stroke(strokeW, cap = StrokeCap.Round))
            }
            CartoonFaceStyle.CONFUSED -> {
                drawCircle(outlineColor, eyeR * 1.15f, Offset(cx - eyeDx, eyeY))
                drawCircle(outlineColor, eyeR * 0.8f, Offset(cx + eyeDx, eyeY + r * 0.03f))
                // Asymmetric brows
                drawLine(outlineColor, Offset(cx - eyeDx - r * 0.12f, eyeY - r * 0.26f), Offset(cx - eyeDx + r * 0.12f, eyeY - r * 0.24f), strokeW * 0.8f, StrokeCap.Round)
                drawLine(outlineColor, Offset(cx + eyeDx - r * 0.12f, eyeY - r * 0.12f), Offset(cx + eyeDx + r * 0.12f, eyeY - r * 0.18f), strokeW * 0.8f, StrokeCap.Round)
                drawLine(outlineColor, Offset(cx - r * 0.16f, cy + r * 0.24f), Offset(cx + r * 0.16f, cy + r * 0.16f), strokeW, StrokeCap.Round)
            }
        }
    }
}

private fun DrawScope.drawStarSimple(center: Offset, radius: Float, color: Color) {
    val path = Path()
    for (i in 0 until 10) {
        val angle = (i * 36.0 - 90.0) * PI / 180.0
        val r = if (i % 2 == 0) radius else radius * 0.45f
        val x = center.x + (cos(angle) * r).toFloat()
        val y = center.y + (sin(angle) * r).toFloat()
        if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
    }
    path.close()
    drawPath(path, color)
}

@Composable
fun AnimatedCartoonBuddy(
    character: CartoonCharacterId,
    mood: String = "warm",
    reducedMotion: Boolean = false,
    modifier: Modifier = Modifier,
    size: Dp = 92.dp
) {
    val transition = rememberInfiniteTransition(label = "buddy_idle")
    val bobOffset by transition.animateFloat(
        initialValue = 0f,
        targetValue = if (reducedMotion) 0f else -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bob"
    )
    val blinkValue by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3600),
            repeatMode = RepeatMode.Restart
        ),
        label = "blink"
    )
    val isBlinking = !reducedMotion && blinkValue > 0.94f

    Canvas(
        modifier = modifier
            .size(size)
            .semantics { contentDescription = "${character.displayName} the ${character.roleTag}" }
    ) {
        drawCharacterOnCanvas(
            character = character,
            center = Offset(this.size.width / 2f, this.size.height * 0.56f + bobOffset),
            radius = this.size.minDimension * 0.34f,
            mood = mood,
            isBlinking = isBlinking
        )
    }
}

fun DrawScope.drawCharacterOnCanvas(
    character: CartoonCharacterId,
    center: Offset,
    radius: Float,
    mood: String,
    isBlinking: Boolean
) {
    val outline = Color(0xFF282B38)
    val stroke = (radius * 0.085f).coerceAtLeast(2f)

    // Soft ground shadow
    drawOval(
        color = Color(0xFF282B38).copy(alpha = 0.09f),
        topLeft = Offset(center.x - radius * 0.85f, center.y + radius * 0.88f),
        size = Size(radius * 1.7f, radius * 0.3f)
    )

    when (character) {
        CartoonCharacterId.MOMO -> {
            // Cloud-sprout buddy: puffy mint-cream cloud body + tiny green sprout on top
            val cloudColor = Color(0xFFD2F4EA)
            drawCircle(cloudColor, radius * 0.68f, Offset(center.x - radius * 0.48f, center.y + radius * 0.1f))
            drawCircle(cloudColor, radius * 0.68f, Offset(center.x + radius * 0.48f, center.y + radius * 0.1f))
            drawCircle(cloudColor, radius * 0.85f, Offset(center.x, center.y - radius * 0.05f))

            // Outline arcs
            drawCircle(outline, radius * 0.86f, Offset(center.x, center.y - radius * 0.05f), style = Stroke(stroke))
            drawCircle(cloudColor, radius * 0.82f, Offset(center.x, center.y - radius * 0.05f))
            drawCircle(cloudColor, radius * 0.63f, Offset(center.x - radius * 0.46f, center.y + radius * 0.1f))
            drawCircle(cloudColor, radius * 0.63f, Offset(center.x + radius * 0.46f, center.y + radius * 0.1f))

            // Cute leaf sprout on top
            val stemBase = Offset(center.x, center.y - radius * 0.88f)
            drawLine(Color(0xFF2B8A6E), stemBase, Offset(center.x, stemBase.y - radius * 0.36f), stroke * 0.9f, StrokeCap.Round)
            drawOval(
                Color(0xFF69C98E),
                topLeft = Offset(center.x, stemBase.y - radius * 0.56f),
                size = Size(radius * 0.44f, radius * 0.24f)
            )
            drawOval(
                Color(0xFF54B67B),
                topLeft = Offset(center.x - radius * 0.42f, stemBase.y - radius * 0.46f),
                size = Size(radius * 0.4f, radius * 0.22f)
            )
        }
        CartoonCharacterId.KAJU -> {
            // Cozy round pebble-cat
            val furColor = Color(0xFFFFE1C6)
            // Ears
            val leftEar = Path().apply {
                moveTo(center.x - radius * 0.65f, center.y - radius * 0.35f)
                lineTo(center.x - radius * 0.82f, center.y - radius * 0.98f)
                lineTo(center.x - radius * 0.2f, center.y - radius * 0.68f)
                close()
            }
            val rightEar = Path().apply {
                moveTo(center.x + radius * 0.65f, center.y - radius * 0.35f)
                lineTo(center.x + radius * 0.82f, center.y - radius * 0.98f)
                lineTo(center.x + radius * 0.2f, center.y - radius * 0.68f)
                close()
            }
            drawPath(leftEar, furColor)
            drawPath(leftEar, outline, style = Stroke(stroke, join = StrokeJoin.Round))
            drawPath(rightEar, furColor)
            drawPath(rightEar, outline, style = Stroke(stroke, join = StrokeJoin.Round))

            drawOval(
                color = furColor,
                topLeft = Offset(center.x - radius * 0.92f, center.y - radius * 0.78f),
                size = Size(radius * 1.84f, radius * 1.62f)
            )
            drawOval(
                color = outline,
                topLeft = Offset(center.x - radius * 0.92f, center.y - radius * 0.78f),
                size = Size(radius * 1.84f, radius * 1.62f),
                style = Stroke(stroke)
            )
            // Little stripes on forehead
            drawLine(Color(0xFFE09F67), Offset(center.x, center.y - radius * 0.72f), Offset(center.x, center.y - radius * 0.48f), stroke * 0.8f, StrokeCap.Round)
        }
        CartoonCharacterId.PIP -> {
            // Little yellow pocket sparrow
            val bodyColor = Color(0xFFFFE680)
            drawCircle(bodyColor, radius * 0.82f, center)
            drawCircle(outline, radius * 0.82f, center, style = Stroke(stroke))
            // Wing
            drawOval(Color(0xFFF6C84C), Offset(center.x + radius * 0.35f, center.y), Size(radius * 0.55f, radius * 0.42f))
            // Little crest
            drawLine(outline, Offset(center.x, center.y - radius * 0.82f), Offset(center.x - radius * 0.14f, center.y - radius * 1.05f), stroke * 0.8f, StrokeCap.Round)
        }
        CartoonCharacterId.TOTO -> {
            // Lantern fox
            val foxColor = Color(0xFFFFAE8D)
            val leftEar = Path().apply {
                moveTo(center.x - radius * 0.7f, center.y - radius * 0.25f)
                lineTo(center.x - radius * 0.92f, center.y - radius * 1.06f)
                lineTo(center.x - radius * 0.18f, center.y - radius * 0.72f)
                close()
            }
            val rightEar = Path().apply {
                moveTo(center.x + radius * 0.7f, center.y - radius * 0.25f)
                lineTo(center.x + radius * 0.92f, center.y - radius * 1.06f)
                lineTo(center.x + radius * 0.18f, center.y - radius * 0.72f)
                close()
            }
            drawPath(leftEar, foxColor)
            drawPath(leftEar, outline, style = Stroke(stroke, join = StrokeJoin.Round))
            drawPath(rightEar, foxColor)
            drawPath(rightEar, outline, style = Stroke(stroke, join = StrokeJoin.Round))
            drawCircle(foxColor, radius * 0.88f, center)
            drawCircle(outline, radius * 0.88f, center, style = Stroke(stroke))
            // Cream scarf
            drawRoundRect(
                color = Color(0xFFFFF5D6),
                topLeft = Offset(center.x - radius * 0.65f, center.y + radius * 0.52f),
                size = Size(radius * 1.3f, radius * 0.3f),
                cornerRadius = CornerRadius(radius * 0.15f, radius * 0.15f)
            )
        }
    }

    // Shared expressive face
    val eyeDx = radius * 0.32f
    val eyeY = center.y - radius * 0.08f
    drawCircle(Color(0xFFFF9AA2).copy(alpha = 0.6f), radius * 0.15f, Offset(center.x - radius * 0.48f, center.y + radius * 0.16f))
    drawCircle(Color(0xFFFF9AA2).copy(alpha = 0.6f), radius * 0.15f, Offset(center.x + radius * 0.48f, center.y + radius * 0.16f))

    if (isBlinking || mood == "calm" || mood == "breathing") {
        drawArc(outline, 200f, 140f, false, Offset(center.x - eyeDx - radius * 0.12f, eyeY - radius * 0.06f), Size(radius * 0.24f, radius * 0.18f), style = Stroke(stroke, cap = StrokeCap.Round))
        drawArc(outline, 200f, 140f, false, Offset(center.x + eyeDx - radius * 0.12f, eyeY - radius * 0.06f), Size(radius * 0.24f, radius * 0.18f), style = Stroke(stroke, cap = StrokeCap.Round))
    } else {
        drawCircle(outline, radius * 0.095f, Offset(center.x - eyeDx, eyeY))
        drawCircle(outline, radius * 0.095f, Offset(center.x + eyeDx, eyeY))
        drawCircle(Color.White, radius * 0.035f, Offset(center.x - eyeDx - radius * 0.03f, eyeY - radius * 0.03f))
        drawCircle(Color.White, radius * 0.035f, Offset(center.x + eyeDx - radius * 0.03f, eyeY - radius * 0.03f))
    }

    if (character == CartoonCharacterId.PIP) {
        // Cute orange beak
        val beak = Path().apply {
            moveTo(center.x - radius * 0.12f, center.y + radius * 0.06f)
            lineTo(center.x + radius * 0.12f, center.y + radius * 0.06f)
            lineTo(center.x, center.y + radius * 0.24f)
            close()
        }
        drawPath(beak, Color(0xFFFF8F40))
    } else if (mood == "worried" || mood == "heavy") {
        drawArc(outline, 200f, 140f, false, Offset(center.x - radius * 0.16f, center.y + radius * 0.14f), Size(radius * 0.32f, radius * 0.16f), style = Stroke(stroke, cap = StrokeCap.Round))
    } else {
        drawArc(outline, 15f, 150f, false, Offset(center.x - radius * 0.2f, center.y + radius * 0.03f), Size(radius * 0.4f, radius * 0.24f), style = Stroke(stroke, cap = StrokeCap.Round))
    }
}

@Composable
fun MomoCompanionBanner(
    message: String,
    onTapNextMessage: () -> Unit,
    reducedMotion: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onTapNextMessage),
        shape = RoundedCornerShape(26.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(contentAlignment = Alignment.BottomEnd) {
                Image(
                    painter = painterResource(id = R.drawable.img_buddy_momo),
                    contentDescription = "Momo the Mood Companion",
                    modifier = Modifier
                        .size(72.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                if (!reducedMotion) {
                    AnimatedCartoonBuddy(
                        character = CartoonCharacterId.MOMO,
                        mood = "warm",
                        reducedMotion = reducedMotion,
                        size = 32.dp
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Momo • Mood Companion",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Tap Momo for another gentle note",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
