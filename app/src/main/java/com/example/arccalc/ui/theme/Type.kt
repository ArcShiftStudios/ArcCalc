package com.example.arccalc.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.arccalc.model.TypographyStyle

// Define custom FontFamilies using installed local Google Fonts
val SilkscreenFontFamily = FontFamily(Font(R.font.silkscreen, FontWeight.Normal))
val OrbitronFontFamily = FontFamily(Font(R.font.orbitron, FontWeight.Normal))
val ShareTechMonoFontFamily = FontFamily(Font(R.font.share_tech_mono, FontWeight.Normal))
val ChakraPetchFontFamily = FontFamily(Font(R.font.chakra_petch, FontWeight.Normal))
val FiraCodeFontFamily = FontFamily(Font(R.font.fira_code, FontWeight.Normal))
val OutfitFontFamily = FontFamily(Font(R.font.outfit, FontWeight.Normal))
val SyneFontFamily = FontFamily(Font(R.font.syne, FontWeight.Normal))
val BebasNeueFontFamily = FontFamily(Font(R.font.bebas_neue, FontWeight.Normal))
val CinzelFontFamily = FontFamily(Font(R.font.cinzel, FontWeight.Normal))
val NovaSquareFontFamily = FontFamily(Font(R.font.nova_square, FontWeight.Normal))

fun getAppFontFamily(style: TypographyStyle): FontFamily {
    return when (style) {
        TypographyStyle.DOT_MATRIX -> SilkscreenFontFamily
        TypographyStyle.SPACE_GEO -> OrbitronFontFamily
        TypographyStyle.TECH_MONO -> ShareTechMonoFontFamily
        TypographyStyle.CHAKRA -> ChakraPetchFontFamily
        TypographyStyle.FIRA_CODE -> FiraCodeFontFamily
        TypographyStyle.MODERN_SANS -> OutfitFontFamily
        TypographyStyle.SYNE_DISPLAY -> SyneFontFamily
        TypographyStyle.BEBAS_POSTER -> BebasNeueFontFamily
        TypographyStyle.CINZEL_SERIF -> CinzelFontFamily
        TypographyStyle.NOVA_SQUARE -> NovaSquareFontFamily
    }
}

fun getAppTypography(style: TypographyStyle, scale: Float): Typography {
    val family = getAppFontFamily(style)
    return Typography(
        displayLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (48 * scale).sp,
            lineHeight = (56 * scale).sp,
            letterSpacing = 0.sp
        ),
        displayMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (40 * scale).sp,
            lineHeight = (46 * scale).sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = (32 * scale).sp,
            lineHeight = (38 * scale).sp,
            letterSpacing = 0.sp
        ),
        headlineLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (28 * scale).sp,
            lineHeight = (34 * scale).sp
        ),
        headlineMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = (24 * scale).sp,
            lineHeight = (30 * scale).sp
        ),
        headlineSmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = (20 * scale).sp,
            lineHeight = (26 * scale).sp
        ),
        titleLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (18 * scale).sp,
            lineHeight = (24 * scale).sp
        ),
        titleMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = (16 * scale).sp,
            lineHeight = (22 * scale).sp
        ),
        titleSmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Medium,
            fontSize = (14 * scale).sp,
            lineHeight = (18 * scale).sp
        ),
        bodyLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Normal,
            fontSize = (15 * scale).sp,
            lineHeight = (20 * scale).sp
        ),
        bodyMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Normal,
            fontSize = (13 * scale).sp,
            lineHeight = (18 * scale).sp
        ),
        bodySmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Normal,
            fontSize = (11 * scale).sp,
            lineHeight = (15 * scale).sp
        ),
        labelLarge = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Bold,
            fontSize = (14 * scale).sp,
            lineHeight = (18 * scale).sp,
            letterSpacing = 0.5.sp
        ),
        labelMedium = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.SemiBold,
            fontSize = (12 * scale).sp,
            lineHeight = (16 * scale).sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle(
            fontFamily = family,
            fontWeight = FontWeight.Medium,
            fontSize = (10 * scale).sp,
            lineHeight = (14 * scale).sp,
            letterSpacing = 0.5.sp
        )
    )
}

val Typography = getAppTypography(TypographyStyle.DOT_MATRIX, 1.0f)
