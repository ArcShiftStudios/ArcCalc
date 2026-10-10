package com.example.arccalc.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arccalc.model.AngleMode
import com.example.arccalc.ui.theme.LocalArcPalette
import com.example.arccalc.ui.theme.LocalTypographyStyle
import com.example.arccalc.ui.theme.getAppFontFamily

@Composable
fun HeaderBar(
    angleMode: AngleMode,
    onAngleModeClick: () -> Unit,
    onThemeClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalArcPalette.current
    val typoStyle = LocalTypographyStyle.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // App Title & Brand
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(palette.primaryAccent.copy(alpha = 0.15f))
                    .border(1.dp, palette.primaryAccent.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Calculate,
                    contentDescription = null,
                    tint = palette.primaryAccent,
                    modifier = Modifier.size(18.dp)
                )
            }

            Column {
                Text(
                    text = "ARC CALC",
                    color = palette.textPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getAppFontFamily(typoStyle),
                    letterSpacing = 1.sp
                )
                Text(
                    text = "PRECISION ENGINE",
                    color = palette.textSecondary,
                    fontSize = 9.sp,
                    fontFamily = getAppFontFamily(typoStyle),
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Action Icons
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Angle Mode Chip (DEG / RAD)
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(palette.displaySurface)
                    .border(1.dp, palette.borderGlow.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                    .clickable { onAngleModeClick() }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
                    .testTag("btn_angle_mode"),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = angleMode.name,
                    color = palette.primaryAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getAppFontFamily(typoStyle)
                )
            }

            // Theme Studio Icon
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(palette.displaySurface)
                    .border(1.dp, palette.borderGlow.copy(alpha = 0.3f), CircleShape)
                    .clickable { onThemeClick() }
                    .testTag("btn_theme_studio"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    contentDescription = "Themes",
                    tint = palette.textPrimary,
                    modifier = Modifier.size(17.dp)
                )
            }

            // History Icon
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(palette.displaySurface)
                    .border(1.dp, palette.borderGlow.copy(alpha = 0.3f), CircleShape)
                    .clickable { onHistoryClick() }
                    .testTag("btn_history"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.History,
                    contentDescription = "History",
                    tint = palette.textPrimary,
                    modifier = Modifier.size(17.dp)
                )
            }

            // Settings Icon
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(palette.displaySurface)
                    .border(1.dp, palette.borderGlow.copy(alpha = 0.3f), CircleShape)
                    .clickable { onSettingsClick() }
                    .testTag("btn_settings"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Settings,
                    contentDescription = "Settings",
                    tint = palette.textPrimary,
                    modifier = Modifier.size(17.dp)
                )
            }
        }
    }
}

@Composable
fun ModeToggleBar(
    isScientificExpanded: Boolean,
    onToggleScientific: () -> Unit,
    onOpenConverter: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalArcPalette.current
    val typoStyle = LocalTypographyStyle.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // SCI Pill
        Box(
            modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .clip(RoundedCornerShape(21.dp))
                .background(if (isScientificExpanded) palette.operators else palette.displaySurface)
                .border(
                    1.dp,
                    if (isScientificExpanded) palette.primaryAccent else palette.borderGlow.copy(alpha = 0.35f),
                    RoundedCornerShape(21.dp)
                )
                .clickable { onToggleScientific() }
                .testTag("btn_toggle_sci"),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isScientificExpanded) "SCI ⌃" else "SCI ⌄",
                color = if (isScientificExpanded) palette.textPrimary else palette.textSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getAppFontFamily(typoStyle),
                letterSpacing = 1.sp
            )
        }

        // CONVERTER Pill
        Box(
            modifier = Modifier
                .weight(1f)
                .height(42.dp)
                .clip(RoundedCornerShape(21.dp))
                .background(palette.displaySurface)
                .border(1.dp, palette.borderGlow.copy(alpha = 0.35f), RoundedCornerShape(21.dp))
                .clickable { onOpenConverter() }
                .testTag("btn_open_converter"),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "⇄",
                    color = palette.textSecondary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "CONVERTER",
                    color = palette.textSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getAppFontFamily(typoStyle),
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
