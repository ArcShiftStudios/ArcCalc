package com.example.arccalc.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import kotlin.math.*

data class TonalRolePalette(
    val primaryAccent: Color,
    val operators: Color,
    val equalsAction: Color,
    val clearAction: Color,
    val background: Color,
    val displaySurface: Color,
    val row2Keys: Color,
    val row3Keys: Color,
    val numberKeys: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val borderGlow: Color,
    val isDark: Boolean = true,
    val row1BorderGlow: Color = borderGlow,
    val row2BorderGlow: Color = borderGlow,
    val row3BorderGlow: Color = borderGlow,
    val operatorsBorderGlow: Color = borderGlow,
    val isWireframeReticle: Boolean = false,
    val isNeonConcaveGradient: Boolean = false
)

object ColorHarmonizer {

    // Converts Color to HSV (Hue 0-360, Saturation 0-1, Value/Brightness 0-1)
    fun colorToHSV(color: Color): FloatArray {
        val hsv = FloatArray(3)
        android.graphics.Color.colorToHSV(color.toArgb(), hsv)
        return hsv
    }

    // Creates Color from HSV
    fun hsvToColor(hue: Float, saturation: Float, value: Float): Color {
        val cleanH = ((hue % 360f) + 360f) % 360f
        val cleanS = saturation.coerceIn(0f, 1f)
        val cleanV = value.coerceIn(0f, 1f)
        val argb = android.graphics.Color.HSVToColor(floatArrayOf(cleanH, cleanS, cleanV))
        return Color(argb)
    }

    // Generate 10 Material 3 style tonal stops from 95 to 10
    fun generateTonalStops(seed: Color): List<Pair<Int, Color>> {
        val hsv = colorToHSV(seed)
        val h = hsv[0]
        val s = hsv[1]

        val tones = listOf(95, 90, 80, 70, 60, 50, 40, 30, 20, 10)
        return tones.map { tone ->
            val brightness = tone / 100f
            val saturation = (s * (1f - abs(tone - 50f) / 65f)).coerceIn(0.15f, 1f)
            tone to hsvToColor(h, saturation, brightness)
        }
    }

    // Automatically derive the 7 harmonic roles from any seed color
    fun deriveHarmonicPalette(seed: Color, isDark: Boolean = true): TonalRolePalette {
        val hsv = colorToHSV(seed)
        val h = hsv[0]
        val s = hsv[1].coerceIn(0.4f, 0.95f)

        // Harmonic angles
        val opHue = (h + 30f) % 360f
        val eqHue = (h + 160f) % 360f
        val clrHue = (h - 35f + 360f) % 360f
        val r2Hue = (h + 60f) % 360f
        val r3Hue = (h + 120f) % 360f

        return if (isDark) {
            val bg = hsvToColor(h, 0.35f, 0.07f)
            val disp = hsvToColor(h, 0.30f, 0.11f)
            val numKeys = hsvToColor(h, 0.25f, 0.14f)
            val primary = hsvToColor(h, s, 0.85f)
            val op = hsvToColor(opHue, s, 0.80f)
            val eq = hsvToColor(eqHue, 0.90f, 0.90f)
            val clr = hsvToColor(clrHue, 0.85f, 0.85f)
            val r2 = hsvToColor(r2Hue, s, 0.75f)
            val r3 = hsvToColor(r3Hue, s, 0.75f)

            TonalRolePalette(
                primaryAccent = primary,
                operators = op,
                equalsAction = eq,
                clearAction = clr,
                background = bg,
                displaySurface = disp,
                row2Keys = r2,
                row3Keys = r3,
                numberKeys = numKeys,
                textPrimary = Color(0xFFF0F0F5),
                textSecondary = Color(0xFFA5A5B5),
                borderGlow = primary.copy(alpha = 0.35f),
                isDark = true
            )
        } else {
            val bg = Color(0xFFF7F8FA)
            val disp = Color(0xFFFFFFFF)
            val numKeys = Color(0xFFEFF1F5)
            val primary = hsvToColor(h, s, 0.45f)
            val op = hsvToColor(opHue, s, 0.45f)
            val eq = hsvToColor(eqHue, 0.90f, 0.45f)
            val clr = hsvToColor(clrHue, 0.85f, 0.50f)
            val r2 = hsvToColor(r2Hue, s, 0.45f)
            val r3 = hsvToColor(r3Hue, s, 0.45f)

            TonalRolePalette(
                primaryAccent = primary,
                operators = op,
                equalsAction = eq,
                clearAction = clr,
                background = bg,
                displaySurface = disp,
                row2Keys = r2,
                row3Keys = r3,
                numberKeys = numKeys,
                textPrimary = Color(0xFF14161A),
                textSecondary = Color(0xFF5E6472),
                borderGlow = primary.copy(alpha = 0.25f),
                isDark = false
            )
        }
    }
}
