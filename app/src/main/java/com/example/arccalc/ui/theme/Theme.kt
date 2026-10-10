package com.example.arccalc.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.arccalc.data.AppSettings
import com.example.arccalc.model.DesignFinish
import com.example.arccalc.model.FontSizeScale
import com.example.arccalc.model.TypographyStyle

val LocalArcPalette = staticCompositionLocalOf {
    ArcCalcThemes.Material3Expressive.palette
}

val LocalDesignFinish = staticCompositionLocalOf {
    DesignFinish.M3_FLAT
}

val LocalTypographyStyle = staticCompositionLocalOf {
    TypographyStyle.DOT_MATRIX
}

val LocalFontScale = staticCompositionLocalOf {
    1.0f
}

@Composable
fun ArcCalcTheme(
    settings: AppSettings,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val systemDark = isSystemInDarkTheme()

    // For Material 3 Expressive, automatically follow device system light/dark mode!
    val isThemeDark = if (settings.themeId == "m3_expressive") {
        systemDark
    } else {
        settings.isDarkMode
    }

    // Material 3 Expressive adapts to device wallpaper colors on Android 12+ (API 31+)
    val themeDef = if (settings.themeId == "m3_expressive" && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val dynamicScheme = if (isThemeDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        ArcThemeDefinition(
            id = "m3_expressive",
            name = "Material 3 Expressive",
            description = "Material 3 dynamic palette harmonized directly from your device wallpaper",
            category = "Material 3 Expressive",
            defaultFinish = DesignFinish.M3_FLAT,
            isDark = isThemeDark,
            previewDots = listOf(dynamicScheme.background, dynamicScheme.primary, dynamicScheme.tertiary),
            palette = TonalRolePalette(
                primaryAccent = dynamicScheme.primary,
                operators = dynamicScheme.secondaryContainer,
                equalsAction = dynamicScheme.primary,
                clearAction = dynamicScheme.errorContainer,
                background = dynamicScheme.background,
                displaySurface = if (isThemeDark) dynamicScheme.surfaceVariant.copy(alpha = 0.5f) else dynamicScheme.surface,
                row2Keys = dynamicScheme.secondaryContainer,
                row3Keys = dynamicScheme.tertiaryContainer,
                numberKeys = dynamicScheme.surfaceVariant,
                textPrimary = dynamicScheme.onSurface,
                textSecondary = dynamicScheme.onSurfaceVariant,
                borderGlow = dynamicScheme.outlineVariant.copy(alpha = 0.4f),
                isDark = isThemeDark
            )
        )
    } else {
        ArcCalcThemes.getTheme(
            id = settings.themeId,
            customSeedHex = settings.customSeedHex,
            isDark = isThemeDark
        )
    }

    val palette = themeDef.palette
    val finish = settings.designFinish

    val colorScheme = if (palette.isDark) {
        darkColorScheme(
            primary = palette.primaryAccent,
            onPrimary = Color.Black,
            primaryContainer = palette.row2Keys,
            onPrimaryContainer = palette.textPrimary,
            secondary = palette.operators,
            onSecondary = palette.textPrimary,
            background = palette.background,
            onBackground = palette.textPrimary,
            surface = palette.displaySurface,
            onSurface = palette.textPrimary,
            surfaceVariant = palette.numberKeys,
            onSurfaceVariant = palette.textSecondary
        )
    } else {
        lightColorScheme(
            primary = palette.primaryAccent,
            onPrimary = Color.White,
            primaryContainer = palette.row2Keys,
            onPrimaryContainer = palette.textPrimary,
            secondary = palette.operators,
            onSecondary = palette.textPrimary,
            background = palette.background,
            onBackground = palette.textPrimary,
            surface = palette.displaySurface,
            onSurface = palette.textPrimary,
            surfaceVariant = palette.numberKeys,
            onSurfaceVariant = palette.textSecondary
        )
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = palette.background.toArgb()
                window.navigationBarColor = palette.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !palette.isDark
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !palette.isDark
            }
        }
    }

    val typography = getAppTypography(settings.typographyStyle, settings.fontSizeScale.scale)

    CompositionLocalProvider(
        LocalArcPalette provides palette,
        LocalDesignFinish provides finish,
        LocalTypographyStyle provides settings.typographyStyle,
        LocalFontScale provides settings.fontSizeScale.scale
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            content = content
        )
    }
}
