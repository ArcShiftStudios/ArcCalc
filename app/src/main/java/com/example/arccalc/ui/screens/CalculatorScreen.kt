package com.example.arccalc.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arccalc.ui.components.*
import com.example.arccalc.ui.theme.ArcCalcThemes
import com.example.arccalc.ui.theme.LocalArcPalette
import com.example.arccalc.ui.theme.LocalTypographyStyle
import com.example.arccalc.ui.theme.getAppFontFamily
import com.example.arccalc.viewmodel.CalculatorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculatorScreen(
    viewModel: CalculatorViewModel,
    modifier: Modifier = Modifier
) {
    val expression by viewModel.expression.collectAsStateWithLifecycle()
    val result by viewModel.result.collectAsStateWithLifecycle()
    val isError by viewModel.isError.collectAsStateWithLifecycle()
    val livePreview by viewModel.livePreview.collectAsStateWithLifecycle()
    val isSciExpanded by viewModel.isScientificExpanded.collectAsStateWithLifecycle()
    val is2ndActive by viewModel.is2ndActive.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()

    val palette = LocalArcPalette.current
    val typoStyle = LocalTypographyStyle.current
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    var showHistory by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var showConverter by remember { mutableStateOf(false) }
    var showThemeStudio by remember { mutableStateOf(false) }

    val currentTheme = ArcCalcThemes.getTheme(
        settings.themeId,
        settings.customSeedHex,
        palette.isDark
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = palette.background,
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(palette.background)
        ) {
        if (isLandscape) {
            // Adaptive Landscape Dual-Pane Layout
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Left Column: Header + Large Display + Quick Actions + Status
                Column(
                    modifier = Modifier
                        .weight(1.1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    HeaderBar(
                        angleMode = settings.angleMode,
                        onAngleModeClick = { viewModel.toggleAngleMode() },
                        onThemeClick = { showThemeStudio = true },
                        onHistoryClick = { showHistory = true },
                        onSettingsClick = { showSettings = true }
                    )

                    DisplayScreen(
                        expression = expression,
                        result = result,
                        isError = isError,
                        livePreview = livePreview,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { showConverter = true },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = palette.displaySurface,
                                contentColor = palette.textSecondary
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.weight(1f).height(38.dp)
                        ) {
                            Text("⇄ CONVERTER", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Bottom Footer
                    StatusFooter(
                        themeName = currentTheme.name,
                        onThemeClick = { showThemeStudio = true },
                        onToggleDarkMode = { viewModel.updateSettings { it.copy(isDarkMode = !palette.isDark) } }
                    )
                }

                // Right Column: Full Scientific + Standard Keypad Side-by-Side
                Row(
                    modifier = Modifier
                        .weight(1.9f)
                        .fillMaxHeight(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Sci Pad Column
                    Box(modifier = Modifier.weight(1.1f).fillMaxHeight()) {
                        ScientificPad(
                            visible = true,
                            is2ndActive = is2ndActive,
                            onToggle2nd = { viewModel.toggle2nd() },
                            onButtonClick = { viewModel.onScientificFunction(it) },
                            modifier = Modifier.fillMaxHeight().verticalScroll(rememberScrollState())
                        )
                    }

                    // Standard Pad Column
                    Box(modifier = Modifier.weight(1.4f).fillMaxHeight()) {
                        StandardPad(
                            onDigitClick = { viewModel.onDigit(it) },
                            onOperatorClick = { viewModel.onOperator(it) },
                            onClearClick = { viewModel.onClear() },
                            onDeleteClick = { viewModel.onDelete() },
                            onEqualsClick = { viewModel.onEquals() },
                            onParenthesisClick = { viewModel.onParenthesis(it) },
                            onPercentClick = { viewModel.onPercent() },
                            onDotClick = { viewModel.onDot() },
                            modifier = Modifier.fillMaxHeight().verticalScroll(rememberScrollState())
                        )
                    }
                }
            }
        } else {
            // Portrait Layout
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Header & Mode Controls
                HeaderBar(
                    angleMode = settings.angleMode,
                    onAngleModeClick = { viewModel.toggleAngleMode() },
                    onThemeClick = { showThemeStudio = true },
                    onHistoryClick = { showHistory = true },
                    onSettingsClick = { showSettings = true }
                )

                ModeToggleBar(
                    isScientificExpanded = isSciExpanded,
                    onToggleScientific = { viewModel.toggleScientificExpanded() },
                    onOpenConverter = { showConverter = true }
                )

                // Calculation Display Area
                DisplayScreen(
                    expression = expression,
                    result = result,
                    isError = isError,
                    livePreview = livePreview,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(bottom = 4.dp)
                )

                // Keypad Area
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                ) {
                    ScientificPad(
                        visible = isSciExpanded,
                        is2ndActive = is2ndActive,
                        onToggle2nd = { viewModel.toggle2nd() },
                        onButtonClick = { viewModel.onScientificFunction(it) }
                    )

                    StandardPad(
                        onDigitClick = { viewModel.onDigit(it) },
                        onOperatorClick = { viewModel.onOperator(it) },
                        onClearClick = { viewModel.onClear() },
                        onDeleteClick = { viewModel.onDelete() },
                        onEqualsClick = { viewModel.onEquals() },
                        onParenthesisClick = { viewModel.onParenthesis(it) },
                        onPercentClick = { viewModel.onPercent() },
                        onDotClick = { viewModel.onDot() }
                    )

                    StatusFooter(
                        themeName = currentTheme.name,
                        onThemeClick = { showThemeStudio = true },
                        onToggleDarkMode = { viewModel.updateSettings { it.copy(isDarkMode = !palette.isDark) } }
                    )
                }
            }
        }
        }

        // Sheets
        if (showConverter) {
            UnitConverterSheet(
                onDismiss = { showConverter = false },
                onInsertIntoCalculator = { viewModel.insertValue(it) },
                initialValue = if (result.isNotEmpty() && !isError) result else "1"
            )
        }

        if (showThemeStudio) {
            ThemeStudioSheet(
                settings = settings,
                onSelectTheme = { viewModel.applyTheme(it) },
                onSelectWallpaper = { viewModel.applyWallpaper(it) },
                onApplyCustomPalette = { seedHex, finish -> viewModel.applyCustomPalette(seedHex, finish) },
                onDismiss = { showThemeStudio = false }
            )
        }

        if (showHistory) {
            val historyItems by viewModel.historyList.collectAsStateWithLifecycle(emptyList())
            HistorySheet(
                historyList = historyItems,
                onSelectHistory = { entity ->
                    viewModel.selectHistoryItem(entity)
                    showHistory = false
                },
                onDeleteHistory = { viewModel.deleteHistoryItem(it) },
                onClearAll = { viewModel.clearHistory() },
                onDismiss = { showHistory = false }
            )
        }

        if (showSettings) {
            SettingsSheet(
                settings = settings,
                onUpdateSettings = { viewModel.updateSettings(it) },
                onOpenThemeStudio = { showThemeStudio = true },
                onDismiss = { showSettings = false }
            )
        }
    }
}

@Composable
fun StatusFooter(
    themeName: String,
    onThemeClick: () -> Unit,
    onToggleDarkMode: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val palette = LocalArcPalette.current
    val typoStyle = LocalTypographyStyle.current
    val isEffectiveDark = palette.isDark

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(6.dp))
                .clickable { onToggleDarkMode?.invoke() }
                .padding(horizontal = 4.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (isEffectiveDark) "☾ Dark Mode" else "☼ Light Mode",
                color = palette.textSecondary.copy(alpha = 0.9f),
                fontSize = 11.sp,
                fontFamily = getAppFontFamily(typoStyle)
            )
        }

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { onThemeClick() }
                .padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(palette.primaryAccent)
            )
            Text(
                text = themeName,
                color = palette.primaryAccent,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = getAppFontFamily(typoStyle)
            )
        }
    }
}
