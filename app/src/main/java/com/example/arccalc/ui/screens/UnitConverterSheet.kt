package com.example.arccalc.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arccalc.engine.ConversionUnit
import com.example.arccalc.engine.UnitCategory
import com.example.arccalc.engine.UnitConverterEngine
import com.example.arccalc.ui.components.ArcButton
import com.example.arccalc.ui.components.ButtonRole
import com.example.arccalc.ui.theme.LocalArcPalette
import com.example.arccalc.ui.theme.LocalDesignFinish
import com.example.arccalc.ui.theme.LocalTypographyStyle
import com.example.arccalc.ui.theme.getAppFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverterSheet(
    onDismiss: () -> Unit,
    onInsertIntoCalculator: (String) -> Unit,
    initialValue: String = "1",
    modifier: Modifier = Modifier
) {
    val palette = LocalArcPalette.current
    val finish = LocalDesignFinish.current
    val typoStyle = LocalTypographyStyle.current
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val view = LocalView.current

    val categories = UnitConverterEngine.categories
    var selectedCategoryId by remember { mutableStateOf("length") }
    val currentCategory = categories.first { it.id == selectedCategoryId }

    var fromUnitId by remember(selectedCategoryId) { mutableStateOf(currentCategory.defaultFrom) }
    var toUnitId by remember(selectedCategoryId) { mutableStateOf(currentCategory.defaultTo) }

    var inputString by remember { mutableStateOf(initialValue.ifBlank { "1" }) }

    val fromUnit = currentCategory.units.firstOrNull { it.id == fromUnitId } ?: currentCategory.units.first()
    val toUnit = currentCategory.units.firstOrNull { it.id == toUnitId } ?: currentCategory.units.getOrElse(1) { currentCategory.units.first() }

    val numericInput = inputString.toDoubleOrNull() ?: 0.0
    val convertedValue = remember(selectedCategoryId, fromUnitId, toUnitId, numericInput) {
        UnitConverterEngine.convert(selectedCategoryId, fromUnitId, toUnitId, numericInput)
    }
    val convertedFormatted = remember(convertedValue) {
        UnitConverterEngine.formatConvertedResult(convertedValue)
    }

    var showUnitPickerForFrom by remember { mutableStateOf(false) }
    var showUnitPickerForTo by remember { mutableStateOf(false) }

    val categoryScrollState = rememberScrollState()

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
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "⇄ Unit Converter",
                            color = palette.textPrimary,
                            fontSize = 18.sp,
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
                                text = "M3",
                                color = palette.primaryAccent,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "ArcCalc Live Conversion",
                        color = palette.textSecondary,
                        fontSize = 12.sp,
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

            // Category Chips (Horizontal Scroll)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(categoryScrollState),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = cat.id == selectedCategoryId
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) palette.primaryAccent else palette.numberKeys)
                            .border(
                                1.dp,
                                if (isSelected) palette.primaryAccent else palette.borderGlow.copy(alpha = 0.2f),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                selectedCategoryId = cat.id
                                fromUnitId = cat.defaultFrom
                                toUnitId = cat.defaultTo
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = cat.name,
                            color = if (isSelected) (if (palette.isDark) Color.Black else Color.White) else palette.textPrimary,
                            fontSize = 13.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                    }
                }
            }

            // FROM Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(palette.displaySurface)
                    .border(1.dp, palette.borderGlow.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "FROM",
                            color = palette.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )

                        // Material 3 Styled Unit Selector Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(palette.numberKeys)
                                .border(1.dp, palette.borderGlow.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                                .clickable {
                                    showUnitPickerForFrom = true
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("btn_select_from_unit")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(palette.operators)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = fromUnit.symbol,
                                        color = palette.primaryAccent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = fromUnit.name,
                                    color = palette.textPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = getAppFontFamily(typoStyle)
                                )
                                Icon(
                                    imageVector = Icons.Outlined.KeyboardArrowDown,
                                    contentDescription = "Select From Unit",
                                    tint = palette.primaryAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = if (inputString.isEmpty()) "0" else inputString,
                            color = palette.textPrimary,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                        Text(
                            text = fromUnit.symbol,
                            color = palette.primaryAccent,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                    }
                }
            }

            // Swap Units Button
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = {
                        val temp = fromUnitId
                        fromUnitId = toUnitId
                        toUnitId = temp
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(palette.operators)
                        .border(1.dp, palette.borderGlow.copy(alpha = 0.45f), CircleShape)
                        .testTag("btn_swap_units")
                ) {
                    Icon(
                        imageVector = Icons.Outlined.SwapVert,
                        contentDescription = "Swap Units",
                        tint = palette.primaryAccent,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // TO Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(palette.displaySurface)
                    .border(1.dp, palette.borderGlow.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                    .padding(14.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TO",
                            color = palette.textSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )

                        // Material 3 Styled Unit Selector Pill
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(palette.numberKeys)
                                .border(1.dp, palette.borderGlow.copy(alpha = 0.35f), RoundedCornerShape(12.dp))
                                .clickable {
                                    showUnitPickerForTo = true
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("btn_select_to_unit")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(palette.operators)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = toUnit.symbol,
                                        color = palette.primaryAccent,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Text(
                                    text = toUnit.name,
                                    color = palette.textPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = getAppFontFamily(typoStyle)
                                )
                                Icon(
                                    imageVector = Icons.Outlined.KeyboardArrowDown,
                                    contentDescription = "Select To Unit",
                                    tint = palette.primaryAccent,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = convertedFormatted,
                            color = palette.primaryAccent,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = getAppFontFamily(typoStyle)
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = toUnit.symbol,
                                color = palette.textSecondary,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = getAppFontFamily(typoStyle)
                            )
                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(convertedFormatted))
                                    Toast.makeText(context, "Copied $convertedFormatted", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ContentCopy,
                                    contentDescription = "Copy Converted Value",
                                    tint = palette.textSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Embedded Converter Numpad (4x4 Grid matching screenshot 2)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Row 1: 7, 8, 9, DEL
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ArcButton("7", ButtonRole.NUMBER, { inputString += "7" }, Modifier.weight(1f).height(48.dp), fontSize = 18.sp)
                    ArcButton("8", ButtonRole.NUMBER, { inputString += "8" }, Modifier.weight(1f).height(48.dp), fontSize = 18.sp)
                    ArcButton("9", ButtonRole.NUMBER, { inputString += "9" }, Modifier.weight(1f).height(48.dp), fontSize = 18.sp)
                    ArcButton("DEL", ButtonRole.FUNCTION, { if (inputString.isNotEmpty()) inputString = inputString.dropLast(1) }, Modifier.weight(1f).height(48.dp), fontSize = 14.sp)
                }
                // Row 2: 4, 5, 6, C
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ArcButton("4", ButtonRole.NUMBER, { inputString += "4" }, Modifier.weight(1f).height(48.dp), fontSize = 18.sp)
                    ArcButton("5", ButtonRole.NUMBER, { inputString += "5" }, Modifier.weight(1f).height(48.dp), fontSize = 18.sp)
                    ArcButton("6", ButtonRole.NUMBER, { inputString += "6" }, Modifier.weight(1f).height(48.dp), fontSize = 18.sp)
                    ArcButton("C", ButtonRole.ACTION_CLEAR, { inputString = "" }, Modifier.weight(1f).height(48.dp), fontSize = 16.sp)
                }
                // Row 3: 1, 2, 3, +/-
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ArcButton("1", ButtonRole.NUMBER, { inputString += "1" }, Modifier.weight(1f).height(48.dp), fontSize = 18.sp)
                    ArcButton("2", ButtonRole.NUMBER, { inputString += "2" }, Modifier.weight(1f).height(48.dp), fontSize = 18.sp)
                    ArcButton("3", ButtonRole.NUMBER, { inputString += "3" }, Modifier.weight(1f).height(48.dp), fontSize = 18.sp)
                    ArcButton("+/-", ButtonRole.FUNCTION, {
                        inputString = if (inputString.startsWith("-")) inputString.substring(1) else if (inputString.isNotEmpty()) "-$inputString" else "-"
                    }, Modifier.weight(1f).height(48.dp), fontSize = 16.sp)
                }
                // Row 4: 0, ., Swap, Insert
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ArcButton("0", ButtonRole.NUMBER, { inputString += "0" }, Modifier.weight(1f).height(48.dp), fontSize = 18.sp)
                    ArcButton(".", ButtonRole.NUMBER, { if (!inputString.contains(".")) inputString += "." }, Modifier.weight(1f).height(48.dp), fontSize = 18.sp)
                    ArcButton("Swap", ButtonRole.FUNCTION, {
                        val temp = fromUnitId; fromUnitId = toUnitId; toUnitId = temp
                    }, Modifier.weight(1f).height(48.dp), fontSize = 13.sp)
                    ArcButton("Insert", ButtonRole.ACTION_EQUALS, {
                        onInsertIntoCalculator(convertedFormatted)
                        onDismiss()
                    }, Modifier.weight(1f).height(48.dp), fontSize = 13.sp)
                }
            }

            // Bottom Actions: Insert into Calculator & Back
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        onInsertIntoCalculator(convertedFormatted)
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = palette.primaryAccent,
                        contentColor = if (palette.isDark) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.weight(1f).height(48.dp)
                ) {
                    Text(
                        text = "✨ Insert into Calculator",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getAppFontFamily(typoStyle)
                    )
                }

                OutlinedButton(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, palette.borderGlow.copy(alpha = 0.4f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = palette.textSecondary),
                    modifier = Modifier.height(48.dp)
                ) {
                    Text(
                        text = "Back",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = getAppFontFamily(typoStyle)
                    )
                }
            }
        }
    }

    // Material 3 Themed Unit Picker Sheet for FROM
    if (showUnitPickerForFrom) {
        Material3UnitPickerDialog(
            title = "Select 'FROM' Unit",
            categoryName = currentCategory.name,
            units = currentCategory.units,
            selectedUnitId = fromUnitId,
            onSelectUnit = {
                fromUnitId = it.id
                showUnitPickerForFrom = false
            },
            onDismiss = { showUnitPickerForFrom = false }
        )
    }

    // Material 3 Themed Unit Picker Sheet for TO
    if (showUnitPickerForTo) {
        Material3UnitPickerDialog(
            title = "Select 'TO' Unit",
            categoryName = currentCategory.name,
            units = currentCategory.units,
            selectedUnitId = toUnitId,
            onSelectUnit = {
                toUnitId = it.id
                showUnitPickerForTo = false
            },
            onDismiss = { showUnitPickerForTo = false }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Material3UnitPickerDialog(
    title: String,
    categoryName: String,
    units: List<ConversionUnit>,
    selectedUnitId: String,
    onSelectUnit: (ConversionUnit) -> Unit,
    onDismiss: () -> Unit
) {
    val palette = LocalArcPalette.current
    val typoStyle = LocalTypographyStyle.current
    val context = LocalContext.current
    val view = LocalView.current
    var searchQuery by remember { mutableStateOf("") }

    val filteredUnits = remember(units, searchQuery) {
        if (searchQuery.isBlank()) units
        else units.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.symbol.contains(searchQuery, ignoreCase = true)
        }
    }

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
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
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
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = title,
                            color = palette.textPrimary,
                            fontSize = 18.sp,
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
                                text = categoryName,
                                color = palette.primaryAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = "Choose from ${units.size} supported units",
                        color = palette.textSecondary,
                        fontSize = 12.sp,
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

            // Search Bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search unit or symbol...", color = palette.textSecondary) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        contentDescription = null,
                        tint = palette.primaryAccent
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(imageVector = Icons.Outlined.Clear, contentDescription = "Clear", tint = palette.textSecondary)
                        }
                    }
                },
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = palette.displaySurface,
                    unfocusedContainerColor = palette.displaySurface,
                    focusedTextColor = palette.textPrimary,
                    unfocusedTextColor = palette.textPrimary,
                    focusedIndicatorColor = palette.primaryAccent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, palette.borderGlow.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
            )

            // Units List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 380.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredUnits, key = { it.id }) { unit ->
                    val isSelected = unit.id == selectedUnitId
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (isSelected) palette.operators else palette.displaySurface)
                            .border(
                                1.5.dp,
                                if (isSelected) palette.primaryAccent else palette.borderGlow.copy(alpha = 0.15f),
                                RoundedCornerShape(16.dp)
                            )
                            .clickable {
                                onSelectUnit(unit)
                            }
                            .padding(horizontal = 16.dp, vertical = 14.dp)
                            .testTag("unit_option_${unit.id}")
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) palette.primaryAccent else palette.numberKeys),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = unit.symbol,
                                        color = if (isSelected) (if (palette.isDark) Color.Black else Color.White) else palette.textPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = getAppFontFamily(typoStyle)
                                    )
                                }

                                Column {
                                    Text(
                                        text = unit.name,
                                        color = palette.textPrimary,
                                        fontSize = 15.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                        fontFamily = getAppFontFamily(typoStyle)
                                    )
                                    Text(
                                        text = "Symbol: ${unit.symbol}",
                                        color = palette.textSecondary,
                                        fontSize = 12.sp,
                                        fontFamily = getAppFontFamily(typoStyle)
                                    )
                                }
                            }

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(palette.primaryAccent),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Check,
                                        contentDescription = "Selected",
                                        tint = if (palette.isDark) Color.Black else Color.White,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
