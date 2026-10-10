package com.example.arccalc.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arccalc.data.AppSettings
import com.example.arccalc.model.AngleMode
import com.example.arccalc.model.DecimalPrecision
import com.example.arccalc.model.FontSizeScale
import com.example.arccalc.model.TypographyStyle
import com.example.arccalc.ui.theme.ArcCalcThemes
import com.example.arccalc.ui.theme.LocalArcPalette
import com.example.arccalc.ui.theme.LocalTypographyStyle
import com.example.arccalc.ui.theme.WallpaperPresets
import com.example.arccalc.ui.theme.getAppFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    settings: AppSettings,
    onUpdateSettings: ((AppSettings) -> AppSettings) -> Unit,
    onOpenThemeStudio: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalArcPalette.current
    val typoStyle = LocalTypographyStyle.current

    val currentTheme = ArcCalcThemes.getTheme(
        settings.themeId,
        settings.customSeedHex,
        settings.isDarkMode
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = palette.background,
        contentColor = palette.textPrimary,
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(top = 12.dp, bottom = 4.dp)
                    .width(42.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(palette.borderGlow.copy(alpha = 0.5f))
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Settings",
                        color = palette.textPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getAppFontFamily(typoStyle)
                    )
                    Text(
                        text = "Customize ArcCalc appearance, font sizing, and wallpaper adaptation",
                        color = palette.textSecondary,
                        fontSize = 11.sp,
                        fontFamily = getAppFontFamily(typoStyle)
                    )
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(palette.displaySurface)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Close,
                        contentDescription = "Close",
                        tint = palette.textPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // SECTION: APPEARANCE & TYPOGRAPHY
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Visibility,
                        contentDescription = null,
                        tint = palette.primaryAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "APPEARANCE & TYPOGRAPHY",
                        color = palette.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getAppFontFamily(typoStyle),
                        letterSpacing = 1.sp
                    )
                }

                // Active Theme Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(palette.displaySurface)
                        .border(1.dp, palette.borderGlow.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                        .clickable {
                            onDismiss()
                            onOpenThemeStudio()
                        }
                        .padding(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(palette.operators),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Palette,
                                    contentDescription = null,
                                    tint = palette.primaryAccent,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Active Theme",
                                    color = palette.textPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = getAppFontFamily(typoStyle)
                                )
                                Text(
                                    text = currentTheme.name,
                                    color = palette.textSecondary,
                                    fontSize = 11.sp,
                                    fontFamily = getAppFontFamily(typoStyle)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                currentTheme.previewDots.forEach { dot ->
                                    Box(Modifier.size(12.dp).clip(CircleShape).background(dot))
                                }
                            }
                            Text(
                                text = "Change →",
                                color = palette.primaryAccent,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = getAppFontFamily(typoStyle)
                            )
                        }
                    }
                }

                // Font Size Tweaking
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "Font Size Tweaking",
                        color = palette.textSecondary,
                        fontSize = 12.sp,
                        fontFamily = getAppFontFamily(typoStyle)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        FontSizeScale.values().forEach { scale ->
                            val isSel = settings.fontSizeScale == scale
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) palette.operators else palette.displaySurface)
                                    .border(
                                        1.5.dp,
                                        if (isSel) palette.primaryAccent else palette.borderGlow.copy(alpha = 0.2f),
                                        RoundedCornerShape(12.dp)
                                    )
                                    .clickable { onUpdateSettings { it.copy(fontSizeScale = scale) } }
                                    .padding(vertical = 10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "A",
                                        color = if (isSel) palette.textPrimary else palette.textSecondary,
                                        fontSize = (15 * scale.scale).sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = scale.label,
                                        color = if (isSel) palette.textPrimary else palette.textSecondary,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = scale.percent,
                                        color = palette.textSecondary,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Display Typography (Grid of 10 diverse fonts)
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Display Typography (10 Custom Fonts)",
                            color = palette.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(palette.operators)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "${TypographyStyle.values().size} STYLES",
                                color = palette.primaryAccent,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    val typoList = TypographyStyle.values().toList()
                    val chunkedList = typoList.chunked(2)
                    chunkedList.forEach { rowPairs ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowPairs.forEach { style ->
                                val isSel = settings.typographyStyle == style
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(14.dp))
                                        .background(if (isSel) palette.operators else palette.displaySurface)
                                        .border(
                                            1.5.dp,
                                            if (isSel) palette.primaryAccent else palette.borderGlow.copy(alpha = 0.2f),
                                            RoundedCornerShape(14.dp)
                                        )
                                        .clickable { onUpdateSettings { it.copy(typographyStyle = style) } }
                                        .padding(vertical = 10.dp, horizontal = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Text(
                                            text = "1 2 3",
                                            color = if (isSel) palette.primaryAccent else palette.textPrimary,
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = getAppFontFamily(style)
                                        )
                                        Text(
                                            text = style.label,
                                            color = if (isSel) palette.textPrimary else palette.textSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.SemiBold,
                                            fontFamily = getAppFontFamily(style)
                                        )
                                        Text(
                                            text = style.category,
                                            color = palette.textSecondary.copy(alpha = 0.7f),
                                            fontSize = 9.sp
                                        )
                                    }
                                }
                            }
                            if (rowPairs.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }

            // SECTION: MATERIAL YOU DYNAMIC COLOR
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Wallpaper,
                        contentDescription = null,
                        tint = palette.primaryAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "MATERIAL YOU DYNAMIC COLOR",
                        color = palette.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getAppFontFamily(typoStyle),
                        letterSpacing = 1.sp
                    )
                }

                // Adapt Theme to Wallpaper Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(palette.displaySurface)
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Adapt Theme to Wallpaper",
                            color = palette.textPrimary,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                        Text(
                            text = "Extracts tonal color palettes for authentic Material You theming",
                            color = palette.textSecondary,
                            fontSize = 11.sp,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                    }

                    Switch(
                        checked = settings.adaptWallpaper,
                        onCheckedChange = { isChecked ->
                            onUpdateSettings { it.copy(adaptWallpaper = isChecked) }
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = palette.primaryAccent,
                            checkedTrackColor = palette.operators
                        )
                    )
                }
            }

            // SECTION: INTERACTION & FEEDBACK
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.TouchApp,
                        contentDescription = null,
                        tint = palette.primaryAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "INTERACTION & FEEDBACK",
                        color = palette.textSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getAppFontFamily(typoStyle),
                        letterSpacing = 1.sp
                    )
                }

                // Decimal Precision Selector
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(palette.displaySurface)
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Decimal Precision",
                        color = palette.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getAppFontFamily(typoStyle)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            DecimalPrecision.AUTO,
                            DecimalPrecision.TWO,
                            DecimalPrecision.FOUR,
                            DecimalPrecision.EIGHT
                        ).forEach { prec ->
                            val isSel = settings.decimalPrecision == prec
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSel) palette.operators else palette.numberKeys)
                                    .border(
                                        1.dp,
                                        if (isSel) palette.primaryAccent else Color.Transparent,
                                        RoundedCornerShape(10.dp)
                                    )
                                    .clickable { onUpdateSettings { it.copy(decimalPrecision = prec) } }
                                    .padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = prec.label,
                                    color = if (isSel) palette.textPrimary else palette.textSecondary,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = getAppFontFamily(typoStyle)
                                )
                            }
                        }
                    }
                }
            }

            // About ArcCalc Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(palette.displaySurface)
                    .border(1.dp, palette.borderGlow.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(palette.numberKeys)
                            .border(1.dp, palette.borderGlow.copy(alpha = 0.4f), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Calculate,
                            contentDescription = null,
                            tint = palette.primaryAccent,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "ArcCalc v1.0",
                            color = palette.textPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                        Text(
                            text = "Material 3 Expressive Calculator with Adaptive Harmonics & Precision Engine",
                            color = palette.textSecondary,
                            fontSize = 11.sp,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                    }
                }
            }
        }
    }
}
