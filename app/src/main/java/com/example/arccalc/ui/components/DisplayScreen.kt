package com.example.arccalc.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arccalc.model.DesignFinish
import com.example.arccalc.ui.theme.LocalArcPalette
import com.example.arccalc.ui.theme.LocalDesignFinish
import com.example.arccalc.ui.theme.LocalFontScale
import com.example.arccalc.ui.theme.LocalTypographyStyle
import com.example.arccalc.ui.theme.getAppFontFamily
import kotlinx.coroutines.delay

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DisplayScreen(
    expression: String,
    result: String,
    isError: Boolean,
    livePreview: String?,
    modifier: Modifier = Modifier,
    onExpressionClick: () -> Unit = {}
) {
    val palette = LocalArcPalette.current
    val finish = LocalDesignFinish.current
    val typoStyle = LocalTypographyStyle.current
    val fontScale = LocalFontScale.current
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val view = LocalView.current
    val exprScrollState = rememberScrollState()

    var showCopiedBadge by remember { mutableStateOf(false) }
    var copiedValue by remember { mutableStateOf("") }

    // Auto-dismiss in-app copy badge
    LaunchedEffect(showCopiedBadge) {
        if (showCopiedBadge) {
            delay(1800)
            showCopiedBadge = false
        }
    }

    // Auto-scroll expression to end when it changes
    LaunchedEffect(expression) {
        exprScrollState.animateScrollTo(exprScrollState.maxValue)
    }

    val shape = RoundedCornerShape(22.dp)

    fun copyCurrentResult() {
        val target = if (result.isNotBlank() && !isError) {
            result
        } else if (!livePreview.isNullOrBlank()) {
            livePreview
        } else if (expression.isNotBlank()) {
            expression
        } else {
            "0"
        }
        clipboardManager.setText(AnnotatedString(target))
        copiedValue = target
        showCopiedBadge = true
        Toast.makeText(context, "Copied $target to clipboard", Toast.LENGTH_SHORT).show()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(shape)
            .then(
                if (finish == DesignFinish.WIREFRAME) {
                    Modifier.border(1.5.dp, palette.borderGlow, shape)
                } else {
                    Modifier.border(1.dp, palette.borderGlow.copy(alpha = 0.35f), shape)
                }
            )
            .background(color = palette.displaySurface, shape = shape) // Pure solid color, clean & sharp
            .then(
                if (palette.isNeonConcaveGradient) {
                    Modifier.drawWithContent {
                        val glowColor = palette.borderGlow
                        val radius = maxOf(size.width, size.height) * 0.85f
                        val displayBrush = Brush.radialGradient(
                            0.0f to Color.Black,
                            0.48f to Color.Black,
                            0.72f to glowColor.copy(alpha = 0.08f),
                            0.88f to glowColor.copy(alpha = 0.20f),
                            1.0f to glowColor.copy(alpha = 0.35f),
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = radius
                        )
                        drawRect(brush = displayBrush)
                        drawContent()
                    }
                } else {
                    Modifier
                }
            )
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = {
                    if (result.isNotBlank() && !isError) {
                        copyCurrentResult()
                    }
                },
                onLongClick = {
                    copyCurrentResult()
                }
            )
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("display_screen"),
        contentAlignment = Alignment.BottomEnd
    ) {
        // In-app visual feedback badge when copied via long press or tap
        AnimatedVisibility(
            visible = showCopiedBadge,
            enter = fadeIn() + scaleIn(initialScale = 0.85f),
            exit = fadeOut() + scaleOut(targetScale = 0.85f),
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(palette.operators)
                    .border(1.dp, palette.primaryAccent.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Check,
                        contentDescription = null,
                        tint = palette.primaryAccent,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Copied to clipboard",
                        color = palette.textPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = getAppFontFamily(typoStyle)
                    )
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Expression Line (scrollable)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(exprScrollState),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (expression.isEmpty()) "" else expression,
                    color = palette.textSecondary,
                    fontSize = (18 * fontScale).sp,
                    fontFamily = getAppFontFamily(typoStyle),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.End,
                    maxLines = 1,
                    modifier = Modifier.testTag("text_expression")
                )
            }

            // Main Result / Readout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ) {
                if (isError) {
                    Text(
                        text = result,
                        color = Color(0xFFFF4D4D),
                        fontSize = (28 * fontScale).sp,
                        fontFamily = getAppFontFamily(typoStyle),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("text_result_error")
                    )
                } else {
                    val displayText = if (result.isNotEmpty()) result else if (expression.isEmpty()) "0" else (livePreview ?: "")
                    val isPreview = result.isEmpty() && livePreview != null

                    Text(
                        text = displayText,
                        color = if (isPreview) palette.textSecondary.copy(alpha = 0.8f) else palette.textPrimary,
                        fontSize = if (displayText.length > 9) (32 * fontScale).sp else (44 * fontScale).sp,
                        fontFamily = getAppFontFamily(typoStyle),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("text_result")
                    )
                }
            }
        }
    }
}
