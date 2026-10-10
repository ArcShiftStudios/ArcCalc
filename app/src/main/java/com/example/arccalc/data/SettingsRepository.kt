package com.example.arccalc.data

import android.content.Context
import android.content.SharedPreferences
import com.example.arccalc.model.AngleMode
import com.example.arccalc.model.DecimalPrecision
import com.example.arccalc.model.DesignFinish
import com.example.arccalc.model.FontSizeScale
import com.example.arccalc.model.ScientificNotation
import com.example.arccalc.model.TypographyStyle
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AppSettings(
    val themeId: String = "m3_expressive",
    val customSeedHex: Long = 0xFF6750A4,
    val designFinish: DesignFinish = DesignFinish.M3_FLAT,
    val fontSizeScale: FontSizeScale = FontSizeScale.NORMAL,
    val typographyStyle: TypographyStyle = TypographyStyle.DOT_MATRIX,
    val adaptWallpaper: Boolean = true,
    val activeWallpaperId: String = "m3_expressive",
    val angleMode: AngleMode = AngleMode.DEG,
    val decimalPrecision: DecimalPrecision = DecimalPrecision.AUTO,
    val scientificNotation: ScientificNotation = ScientificNotation.AUTO,
    val thousandsSeparator: Boolean = true,
    val isDarkMode: Boolean = true
)

class SettingsRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("arccalc_settings", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<AppSettings> = _settings.asStateFlow()

    private fun loadSettings(): AppSettings {
        val themeId = prefs.getString("theme_id", "m3_expressive") ?: "m3_expressive"
        val customSeedHex = prefs.getLong("custom_seed_hex", 0xFF6750A4)
        val finishName = prefs.getString("design_finish", DesignFinish.M3_FLAT.name) ?: DesignFinish.M3_FLAT.name
        val fontScaleName = prefs.getString("font_size_scale", FontSizeScale.NORMAL.name) ?: FontSizeScale.NORMAL.name
        val typoName = prefs.getString("typography_style", TypographyStyle.DOT_MATRIX.name) ?: TypographyStyle.DOT_MATRIX.name
        val adaptWallpaper = prefs.getBoolean("adapt_wallpaper", true)
        val activeWallpaperId = prefs.getString("active_wallpaper_id", "m3_expressive") ?: "m3_expressive"
        val angleModeName = prefs.getString("angle_mode", AngleMode.DEG.name) ?: AngleMode.DEG.name
        val precisionName = prefs.getString("decimal_precision", DecimalPrecision.AUTO.name) ?: DecimalPrecision.AUTO.name
        val sciNotationName = prefs.getString("scientific_notation", ScientificNotation.AUTO.name) ?: ScientificNotation.AUTO.name
        val thousands = prefs.getBoolean("thousands_separator", true)
        val isDarkMode = prefs.getBoolean("is_dark_mode", true)

        return AppSettings(
            themeId = themeId,
            customSeedHex = customSeedHex,
            designFinish = try { DesignFinish.valueOf(finishName) } catch (_: Exception) { DesignFinish.M3_FLAT },
            fontSizeScale = try { FontSizeScale.valueOf(fontScaleName) } catch (_: Exception) { FontSizeScale.NORMAL },
            typographyStyle = try { TypographyStyle.valueOf(typoName) } catch (_: Exception) { TypographyStyle.DOT_MATRIX },
            adaptWallpaper = adaptWallpaper,
            activeWallpaperId = activeWallpaperId,
            angleMode = try { AngleMode.valueOf(angleModeName) } catch (_: Exception) { AngleMode.DEG },
            decimalPrecision = try { DecimalPrecision.valueOf(precisionName) } catch (_: Exception) { DecimalPrecision.AUTO },
            scientificNotation = try { ScientificNotation.valueOf(sciNotationName) } catch (_: Exception) { ScientificNotation.AUTO },
            thousandsSeparator = thousands,
            isDarkMode = isDarkMode
        )
    }

    fun updateSettings(transform: (AppSettings) -> AppSettings) {
        val current = _settings.value
        val updated = transform(current)
        _settings.value = updated

        prefs.edit().apply {
            putString("theme_id", updated.themeId)
            putLong("custom_seed_hex", updated.customSeedHex)
            putString("design_finish", updated.designFinish.name)
            putString("font_size_scale", updated.fontSizeScale.name)
            putString("typography_style", updated.typographyStyle.name)
            putBoolean("adapt_wallpaper", updated.adaptWallpaper)
            putString("active_wallpaper_id", updated.activeWallpaperId)
            putString("angle_mode", updated.angleMode.name)
            putString("decimal_precision", updated.decimalPrecision.name)
            putString("scientific_notation", updated.scientificNotation.name)
            putBoolean("thousands_separator", updated.thousandsSeparator)
            putBoolean("is_dark_mode", updated.isDarkMode)
            apply()
        }
    }
}
