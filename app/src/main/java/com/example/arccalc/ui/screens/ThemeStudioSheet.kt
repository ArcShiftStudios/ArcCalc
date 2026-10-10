package com.example.arccalc.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ColorLens
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Wallpaper
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arccalc.data.AppSettings
import com.example.arccalc.model.DesignFinish
import com.example.arccalc.ui.components.ColorMatrixPicker
import com.example.arccalc.ui.theme.ArcCalcThemes
import com.example.arccalc.ui.theme.LocalArcPalette
import com.example.arccalc.ui.theme.LocalTypographyStyle
import com.example.arccalc.ui.theme.WallpaperPresets
import com.example.arccalc.ui.theme.getAppFontFamily

enum class StudioTab(val label: String) {
    PRESETS("Presets (22)"),
    WALLPAPERS("Wallpaper Colors M3 (24)"),
    CUSTOM_SEED("Custom Seed")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeStudioSheet(
    settings: AppSettings,
    onSelectTheme: (String) -> Unit,
    onSelectWallpaper: (String) -> Unit,
    onApplyCustomPalette: (Long, DesignFinish) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalArcPalette.current
    val typoStyle = LocalTypographyStyle.current

    var activeTab by remember { mutableStateOf(StudioTab.PRESETS) }
    var selectedCategoryFilter by remember { mutableStateOf("All Themes") }

    val categories = listOf(
        "All Themes", "Material 3 Expressive", "Wireframe", "Automotive / Supercar", "Cyber & Neon", "AMOLED & Mineral"
    )

    var currentCustomColor by remember { mutableStateOf(Color(settings.customSeedHex)) }
    var currentFinish by remember { mutableStateOf(settings.designFinish) }

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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Palette,
                            contentDescription = null,
                            tint = palette.primaryAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Theme & Color Studio",
                            color = palette.textPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                    }
                    Text(
                        text = "Choose from 22 crafted presets, 24 M3 wallpaper colors, or custom seed",
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

            // Top Segmented Tab Switcher (Presets, Wallpaper Colors, Custom Seed)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(palette.displaySurface)
                    .padding(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                StudioTab.values().forEach { tab ->
                    val isSel = activeTab == tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (isSel) palette.operators else Color.Transparent)
                            .clickable { activeTab = tab }
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = tab.label,
                            color = if (isSel) palette.textPrimary else palette.textSecondary,
                            fontSize = 11.sp,
                            fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                    }
                }
            }

            when (activeTab) {
                StudioTab.PRESETS -> {
                    // Filter Chips
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        categories.forEach { cat ->
                            val isSel = selectedCategoryFilter == cat
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(if (isSel) palette.primaryAccent else palette.numberKeys)
                                    .clickable { selectedCategoryFilter = cat }
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = cat,
                                    color = if (isSel) (if (palette.isDark) Color.Black else Color.White) else palette.textPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                    fontFamily = getAppFontFamily(typoStyle)
                                )
                            }
                        }
                    }

                    // Theme Presets List
                    val filteredThemes = remember(selectedCategoryFilter) {
                        if (selectedCategoryFilter == "All Themes") {
                            ArcCalcThemes.allPresets
                        } else {
                            ArcCalcThemes.allPresets.filter { it.category == selectedCategoryFilter }
                        }
                    }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 480.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Custom Dynamic Palette Shortcut Row
                        item {
                            val isCustomActive = settings.themeId == "custom_dynamic"
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(palette.displaySurface)
                                    .border(
                                        1.5.dp,
                                        if (isCustomActive) palette.primaryAccent else palette.borderGlow.copy(alpha = 0.2f),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { activeTab = StudioTab.CUSTOM_SEED }
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = "Custom Dynamic Palette",
                                                color = palette.textPrimary,
                                                fontSize = 14.sp,
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
                                                    text = "Picker",
                                                    color = palette.primaryAccent,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                        Text(
                                            text = "Generate a custom harmonic theme from any single color",
                                            color = palette.textSecondary,
                                            fontSize = 11.sp,
                                            fontFamily = getAppFontFamily(typoStyle)
                                        )
                                    }

                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Box(Modifier.size(14.dp).clip(CircleShape).background(Color(settings.customSeedHex)))
                                        Box(Modifier.size(14.dp).clip(CircleShape).background(Color(0xFFE040FB)))
                                        Box(Modifier.size(14.dp).clip(CircleShape).background(Color(0xFFFFEB3B)))
                                    }
                                }
                            }
                        }

                        items(filteredThemes) { theme ->
                            val isCurrent = settings.themeId == theme.id
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isCurrent) palette.operators else palette.displaySurface)
                                    .border(
                                        1.5.dp,
                                        if (isCurrent) palette.primaryAccent else palette.borderGlow.copy(alpha = 0.2f),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { onSelectTheme(theme.id) }
                                    .padding(14.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Text(
                                                text = theme.name,
                                                color = palette.textPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = getAppFontFamily(typoStyle)
                                            )
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(6.dp))
                                                    .background(palette.numberKeys)
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(
                                                    text = theme.defaultFinish.label,
                                                    color = palette.textSecondary,
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                        Text(
                                            text = theme.description,
                                            color = palette.textSecondary,
                                            fontSize = 11.sp,
                                            fontFamily = getAppFontFamily(typoStyle),
                                            maxLines = 2
                                        )
                                    }

                                    // Color dots preview
                                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                                        theme.previewDots.forEach { dotColor ->
                                            Box(
                                                modifier = Modifier
                                                    .size(16.dp)
                                                    .clip(CircleShape)
                                                    .background(dotColor)
                                                    .border(1.dp, Color.White.copy(alpha = 0.3f), CircleShape)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                StudioTab.WALLPAPERS -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 480.dp)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "Material You Dynamic Wallpaper Colors",
                            color = palette.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )

                        WallpaperPresets.forEach { wp ->
                            val isSel = settings.activeWallpaperId == wp.id
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (isSel) palette.operators else palette.displaySurface)
                                    .border(
                                        1.5.dp,
                                        if (isSel) palette.primaryAccent else palette.borderGlow.copy(alpha = 0.2f),
                                        RoundedCornerShape(16.dp)
                                    )
                                    .clickable { onSelectWallpaper(wp.id) }
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
                                                .size(24.dp)
                                                .clip(CircleShape)
                                                .background(wp.color)
                                                .border(2.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                                        )
                                        Column {
                                            Text(
                                                text = wp.name,
                                                color = palette.textPrimary,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = getAppFontFamily(typoStyle)
                                            )
                                            Text(
                                                text = wp.hexCode,
                                                color = palette.textSecondary,
                                                fontSize = 11.sp,
                                                fontFamily = getAppFontFamily(typoStyle)
                                            )
                                        }
                                    }

                                    if (isSel) {
                                        Text(
                                            text = "Active",
                                            color = palette.primaryAccent,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            fontFamily = getAppFontFamily(typoStyle)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                StudioTab.CUSTOM_SEED -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 520.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        ColorMatrixPicker(
                            currentSeedColor = currentCustomColor,
                            onColorChanged = { currentCustomColor = it },
                            selectedFinish = currentFinish,
                            onFinishChanged = { currentFinish = it },
                            onApplyPalette = { color, finish ->
                                val hexLong = (color.toArgb().toLong() and 0xFFFFFFFFL)
                                onApplyCustomPalette(hexLong, finish)
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}
