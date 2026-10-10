package com.example.arccalc.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arccalc.model.DesignFinish
import com.example.arccalc.ui.theme.ColorHarmonizer
import com.example.arccalc.ui.theme.LocalArcPalette
import com.example.arccalc.ui.theme.LocalDesignFinish
import com.example.arccalc.ui.theme.LocalTypographyStyle
import com.example.arccalc.ui.theme.getAppFontFamily
import kotlin.random.Random

@Composable
fun ColorMatrixPicker(
    currentSeedColor: Color,
    onColorChanged: (Color) -> Unit,
    selectedFinish: DesignFinish,
    onFinishChanged: (DesignFinish) -> Unit,
    onApplyPalette: (Color, DesignFinish) -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalArcPalette.current
    val typoStyle = LocalTypographyStyle.current
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

    val initialHsv = remember(currentSeedColor) { ColorHarmonizer.colorToHSV(currentSeedColor) }
    var hue by remember(currentSeedColor) { mutableFloatStateOf(initialHsv[0]) }
    var sat by remember(currentSeedColor) { mutableFloatStateOf(initialHsv[1]) }
    var bri by remember(currentSeedColor) { mutableFloatStateOf(initialHsv[2]) }

    val activeColor = remember(hue, sat, bri) {
        ColorHarmonizer.hsvToColor(hue, sat, bri)
    }

    val hexString = remember(activeColor) {
        String.format("#%06X", (0xFFFFFF and activeColor.toArgb()))
    }

    val harmonicRoles = remember(activeColor) {
        ColorHarmonizer.deriveHarmonicPalette(activeColor, isDark = true)
    }

    val tonalStops = remember(activeColor) {
        ColorHarmonizer.generateTonalStops(activeColor)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header: Active Hex Pill, Color Name, Copy & Randomize Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(palette.numberKeys)
                .border(1.dp, palette.borderGlow.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Color swatch square
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(activeColor)
                        .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                )
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = hexString,
                            color = palette.textPrimary,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                        IconButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(hexString))
                            },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = "Copy Hex",
                                tint = palette.textSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Text(
                        text = "RGB(${((activeColor.red * 255).toInt())}, ${((activeColor.green * 255).toInt())}, ${((activeColor.blue * 255).toInt())})",
                        color = palette.textSecondary,
                        fontSize = 11.sp,
                        fontFamily = getAppFontFamily(typoStyle)
                    )
                }
            }

            // Randomize button
            IconButton(
                onClick = {
                    hue = Random.nextFloat() * 360f
                    sat = 0.5f + Random.nextFloat() * 0.45f
                    bri = 0.7f + Random.nextFloat() * 0.3f
                    onColorChanged(ColorHarmonizer.hsvToColor(hue, sat, bri))
                },
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(palette.displaySurface)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Shuffle,
                    contentDescription = "Randomize Seed",
                    tint = palette.primaryAccent,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        // 2D Saturation / Brightness Color Matrix
        Text(
            text = "Interactive Color Matrix  •  S: ${(sat * 100).toInt()}%  B: ${(bri * 100).toInt()}%",
            color = palette.textSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            fontFamily = getAppFontFamily(typoStyle)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp)
                .clip(RoundedCornerShape(16.dp))
                .border(1.dp, palette.borderGlow.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                .pointerInput(hue) {
                    detectTapGestures { offset ->
                        sat = (offset.x / size.width).coerceIn(0f, 1f)
                        bri = (1f - (offset.y / size.height)).coerceIn(0f, 1f)
                        onColorChanged(ColorHarmonizer.hsvToColor(hue, sat, bri))
                    }
                }
                .pointerInput(hue) {
                    detectDragGestures { change, _ ->
                        sat = (change.position.x / size.width).coerceIn(0f, 1f)
                        bri = (1f - (change.position.y / size.height)).coerceIn(0f, 1f)
                        onColorChanged(ColorHarmonizer.hsvToColor(hue, sat, bri))
                    }
                }
        ) {
            val pureHueColor = ColorHarmonizer.hsvToColor(hue, 1f, 1f)
            Canvas(modifier = Modifier.fillMaxSize()) {
                // Horizontal white to pure color
                drawRect(
                    brush = Brush.horizontalGradient(listOf(Color.White, pureHueColor))
                )
                // Vertical transparent to black
                drawRect(
                    brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Black))
                )

                // Draw thumb
                val thumbX = sat * size.width
                val thumbY = (1f - bri) * size.height
                drawCircle(
                    color = Color.White,
                    radius = 9.dp.toPx(),
                    center = Offset(thumbX, thumbY)
                )
                drawCircle(
                    color = activeColor,
                    radius = 6.dp.toPx(),
                    center = Offset(thumbX, thumbY)
                )
            }
        }

        // Hue Spectrum Slider
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Hue Spectrum Wheel",
                    color = palette.textSecondary,
                    fontSize = 12.sp,
                    fontFamily = getAppFontFamily(typoStyle)
                )
                Text(
                    text = "${hue.toInt()}°",
                    color = palette.primaryAccent,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = getAppFontFamily(typoStyle)
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(1.dp, palette.borderGlow.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                    .pointerInput(Unit) {
                        detectDragGestures { change, _ ->
                            hue = ((change.position.x / size.width) * 360f).coerceIn(0f, 360f)
                            onColorChanged(ColorHarmonizer.hsvToColor(hue, sat, bri))
                        }
                    }
                    .pointerInput(Unit) {
                        detectTapGestures { offset ->
                            hue = ((offset.x / size.width) * 360f).coerceIn(0f, 360f)
                            onColorChanged(ColorHarmonizer.hsvToColor(hue, sat, bri))
                        }
                    }
            ) {
                val rainbowColors = listOf(
                    Color.Red, Color.Yellow, Color.Green, Color.Cyan, Color.Blue, Color.Magenta, Color.Red
                )
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawRect(brush = Brush.horizontalGradient(rainbowColors))
                    val thumbX = (hue / 360f) * size.width
                    drawCircle(Color.White, 9.dp.toPx(), Offset(thumbX, size.height / 2))
                    drawCircle(activeColor, 6.dp.toPx(), Offset(thumbX, size.height / 2))
                }
            }
        }

        // Saturation Slider
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Chroma / Saturation",
                    color = palette.textSecondary,
                    fontSize = 12.sp,
                    fontFamily = getAppFontFamily(typoStyle)
                )
                Text(
                    text = "${(sat * 100).toInt()}%",
                    color = palette.textPrimary,
                    fontSize = 12.sp,
                    fontFamily = getAppFontFamily(typoStyle)
                )
            }
            Slider(
                value = sat,
                onValueChange = {
                    sat = it
                    onColorChanged(ColorHarmonizer.hsvToColor(hue, sat, bri))
                },
                colors = SliderDefaults.colors(
                    thumbColor = activeColor,
                    activeTrackColor = activeColor,
                    inactiveTrackColor = palette.numberKeys
                )
            )
        }

        // Luminance / Brightness Slider
        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Luminance / Brightness",
                    color = palette.textSecondary,
                    fontSize = 12.sp,
                    fontFamily = getAppFontFamily(typoStyle)
                )
                Text(
                    text = "${(bri * 100).toInt()}%",
                    color = palette.textPrimary,
                    fontSize = 12.sp,
                    fontFamily = getAppFontFamily(typoStyle)
                )
            }
            Slider(
                value = bri,
                onValueChange = {
                    bri = it
                    onColorChanged(ColorHarmonizer.hsvToColor(hue, sat, bri))
                },
                colors = SliderDefaults.colors(
                    thumbColor = activeColor,
                    activeTrackColor = activeColor,
                    inactiveTrackColor = palette.numberKeys
                )
            )
        }

        // Material 3 Dynamic Tonal Palette (10 stops)
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "✨ Material 3 Dynamic Tonal Palette (Tap any stop)",
                color = palette.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = getAppFontFamily(typoStyle)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tonalStops.take(5).forEach { (stop, col) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(col)
                            .clickable {
                                val hsv = ColorHarmonizer.colorToHSV(col)
                                hue = hsv[0]; sat = hsv[1]; bri = hsv[2]
                                onColorChanged(col)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$stop",
                            color = if (stop > 50) Color.Black else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                tonalStops.drop(5).forEach { (stop, col) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(col)
                            .clickable {
                                val hsv = ColorHarmonizer.colorToHSV(col)
                                hue = hsv[0]; sat = hsv[1]; bri = hsv[2]
                                onColorChanged(col)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$stop",
                            color = if (stop > 50) Color.Black else Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                    }
                }
            }
        }

        // DESIGN AESTHETIC FINISH
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = "🎨 DESIGN AESTHETIC FINISH",
                color = palette.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getAppFontFamily(typoStyle)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                DesignFinish.values().forEach { finish ->
                    val isSel = selectedFinish == finish
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSel) palette.operators else palette.numberKeys)
                            .border(
                                1.5.dp,
                                if (isSel) activeColor else palette.borderGlow.copy(alpha = 0.2f),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable { onFinishChanged(finish) }
                            .padding(vertical = 10.dp, horizontal = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = finish.label,
                                color = if (isSel) palette.textPrimary else palette.textSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = getAppFontFamily(typoStyle)
                            )
                            Text(
                                text = finish.subtitle,
                                color = palette.textSecondary,
                                fontSize = 9.sp,
                                fontFamily = getAppFontFamily(typoStyle)
                            )
                        }
                    }
                }
            }
        }

        // HARMONICALLY DERIVED TONAL ROLES
        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "HARMONICALLY DERIVED TONAL ROLES  •  7 Stops Derived",
                color = palette.textSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = getAppFontFamily(typoStyle)
            )

            val roles = listOf(
                Pair("Primary Accent", harmonicRoles.primaryAccent),
                Pair("Operators (+30°)", harmonicRoles.operators),
                Pair("Equals (+160°)", harmonicRoles.equalsAction),
                Pair("Clear (-35°)", harmonicRoles.clearAction),
                Pair("Row 2 (+60°)", harmonicRoles.row2Keys),
                Pair("Row 3 (+120°)", harmonicRoles.row3Keys)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                roles.take(3).forEach { (label, col) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(palette.numberKeys)
                            .border(1.dp, col.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(col)
                            )
                            Text(
                                text = label,
                                color = palette.textPrimary,
                                fontSize = 10.sp,
                                fontFamily = getAppFontFamily(typoStyle)
                            )
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                roles.drop(3).forEach { (label, col) ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(10.dp))
                            .background(palette.numberKeys)
                            .border(1.dp, col.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(col)
                            )
                            Text(
                                text = label,
                                color = palette.textPrimary,
                                fontSize = 10.sp,
                                fontFamily = getAppFontFamily(typoStyle)
                            )
                        }
                    }
                }
            }
        }

        // Apply Custom Palette Button
        Button(
            onClick = { onApplyPalette(activeColor, selectedFinish) },
            colors = ButtonDefaults.buttonColors(
                containerColor = activeColor,
                contentColor = if (palette.isDark) Color.Black else Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_apply_custom_palette")
        ) {
            Text(
                text = "✓ Apply Custom Palette ($hexString)",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                fontFamily = getAppFontFamily(typoStyle)
            )
        }
    }
}
