package com.example.arccalc.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arccalc.model.DesignFinish
import com.example.arccalc.ui.theme.LocalArcPalette
import com.example.arccalc.ui.theme.LocalDesignFinish
import com.example.arccalc.ui.theme.LocalFontScale
import com.example.arccalc.ui.theme.LocalTypographyStyle
import com.example.arccalc.ui.theme.getAppFontFamily

enum class ButtonRole {
    NUMBER,
    OPERATOR,
    FUNCTION,
    ACTION_CLEAR,
    ACTION_EQUALS,
    SCIENTIFIC
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArcButton(
    text: String,
    role: ButtonRole,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
    shape: Shape? = null,
    customBackgroundColor: Color? = null,
    customTextColor: Color? = null,
    customBorderColor: Color? = null,
    fontSize: TextUnit = 22.sp,
    testTag: String = "btn_$text"
) {
    val palette = LocalArcPalette.current
    val finish = LocalDesignFinish.current
    val typoStyle = LocalTypographyStyle.current
    val fontScale = LocalFontScale.current

    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.93f else 1.0f,
        animationSpec = spring(dampingRatio = 0.65f, stiffness = 600f),
        label = "btn_scale"
    )

    val isWireframe = finish == DesignFinish.WIREFRAME || palette.isWireframeReticle
    val isNeonGradient = palette.isNeonConcaveGradient

    // In Wireframe mode, all buttons are circles as shown in the reference image
    val effectiveShape = if (isWireframe) {
        CircleShape
    } else {
        shape ?: when (role) {
            ButtonRole.OPERATOR -> CircleShape
            ButtonRole.ACTION_EQUALS -> RoundedCornerShape(22.dp)
            ButtonRole.ACTION_CLEAR -> RoundedCornerShape(18.dp)
            ButtonRole.FUNCTION -> RoundedCornerShape(18.dp)
            ButtonRole.NUMBER -> RoundedCornerShape(20.dp)
            ButtonRole.SCIENTIFIC -> RoundedCornerShape(14.dp)
        }
    }

    // Determine Base Colors based on Role and Theme
    val (bgColor, textColor, borderColor) = remember(
        role, palette, customBackgroundColor, customTextColor, customBorderColor, finish, isWireframe, isNeonGradient, isPressed
    ) {
        val baseBg = when {
            isNeonGradient -> Color.Black
            else -> customBackgroundColor ?: when (role) {
                ButtonRole.NUMBER -> palette.numberKeys
                ButtonRole.OPERATOR -> palette.operators
                ButtonRole.FUNCTION -> palette.row2Keys
                ButtonRole.ACTION_CLEAR -> palette.clearAction
                ButtonRole.ACTION_EQUALS -> palette.equalsAction
                ButtonRole.SCIENTIFIC -> palette.row3Keys
            }
        }

        val pressedBg = if (isPressed && !isNeonGradient) {
            baseBg.copy(alpha = 0.82f)
        } else {
            baseBg
        }

        val txt = customTextColor ?: when {
            role == ButtonRole.ACTION_EQUALS && !isNeonGradient -> Color.White
            role == ButtonRole.ACTION_CLEAR && !isWireframe && !isNeonGradient -> if (finish == DesignFinish.M3_FLAT && !palette.isDark) Color(0xFFB3261E) else Color.White
            else -> palette.textPrimary
        }

        val border = when {
            isWireframe -> when (role) {
                ButtonRole.ACTION_EQUALS -> Color.Transparent
                else -> customBorderColor ?: palette.borderGlow
            }
            isNeonGradient -> customBorderColor ?: when (role) {
                ButtonRole.ACTION_EQUALS -> Color(0xFFDC2626)
                ButtonRole.ACTION_CLEAR -> Color(0xFFFF2D55)
                ButtonRole.OPERATOR -> palette.operatorsBorderGlow
                ButtonRole.NUMBER -> palette.row1BorderGlow
                else -> palette.borderGlow
            }
            finish == DesignFinish.GLOW_CONCAVE -> customBorderColor ?: when (role) {
                ButtonRole.ACTION_EQUALS -> palette.equalsAction.copy(alpha = 0.85f)
                ButtonRole.ACTION_CLEAR -> palette.clearAction.copy(alpha = 0.85f)
                ButtonRole.OPERATOR -> palette.operatorsBorderGlow.copy(alpha = 0.7f)
                ButtonRole.NUMBER -> palette.row1BorderGlow.copy(alpha = 0.7f)
                else -> palette.borderGlow.copy(alpha = 0.4f)
            }
            finish == DesignFinish.M3_FLAT -> customBorderColor ?: palette.borderGlow.copy(alpha = if (palette.isDark) 0.25f else 0.45f)
            else -> Color.Transparent
        }

        Triple(pressedBg, txt, border)
    }

    Box(
        modifier = modifier
            .scale(scale)
            .clip(effectiveShape)
            .then(
                if (isWireframe && borderColor != Color.Transparent) {
                    Modifier.border(BorderStroke(1.2.dp, borderColor), effectiveShape)
                } else if ((finish == DesignFinish.GLOW_CONCAVE || isNeonGradient) && borderColor != Color.Transparent) {
                    Modifier.border(BorderStroke(1.dp, borderColor.copy(alpha = if (isNeonGradient) 0.85f else 0.6f)), effectiveShape)
                } else if (finish == DesignFinish.M3_FLAT && borderColor != Color.Transparent) {
                    Modifier.border(BorderStroke(0.6.dp, borderColor), effectiveShape)
                } else {
                    Modifier
                }
            )
            .background(color = bgColor, shape = effectiveShape)
            .then(
                if (isNeonGradient) {
                    Modifier.drawWithContent {
                        val rimColor = when (role) {
                            ButtonRole.ACTION_EQUALS -> Color(0xFFDC2626)
                            ButtonRole.ACTION_CLEAR -> Color(0xFFFF2D55)
                            else -> customBorderColor ?: (borderColor.takeIf { it != Color.Transparent } ?: palette.borderGlow)
                        }
                        // Radial gradient: color slowly fades to deep black in the middle
                        val radius = maxOf(size.width, size.height) * 0.72f
                        val brush = Brush.radialGradient(
                            0.0f to Color.Black,
                            0.28f to Color.Black,
                            0.58f to rimColor.copy(alpha = if (isPressed) 0.50f else 0.35f),
                            0.82f to rimColor.copy(alpha = if (isPressed) 0.85f else 0.70f),
                            1.0f to rimColor,
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = radius
                        )
                        drawRect(brush = brush)
                        drawContent()
                    }
                } else {
                    Modifier
                }
            )
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
                onLongClick = onLongClick
            )
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        // Wireframe CAD blueprint crosshairs (diagonal X inside circular buttons)
        if (isWireframe) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val strokeW = 1.dp.toPx()
                val lineCol = if (role == ButtonRole.ACTION_EQUALS) {
                    Color.White.copy(alpha = 0.45f)
                } else {
                    borderColor.copy(alpha = 0.35f)
                }
                val pad = size.minDimension * 0.14f
                drawLine(
                    color = lineCol,
                    start = Offset(pad, pad),
                    end = Offset(size.width - pad, size.height - pad),
                    strokeWidth = strokeW
                )
                drawLine(
                    color = lineCol,
                    start = Offset(size.width - pad, pad),
                    end = Offset(pad, size.height - pad),
                    strokeWidth = strokeW
                )
            }
        }

        Text(
            text = text,
            color = textColor,
            fontSize = (fontSize.value * fontScale).sp,
            fontWeight = if (role == ButtonRole.ACTION_EQUALS || isWireframe) FontWeight.Bold else FontWeight.SemiBold,
            fontFamily = getAppFontFamily(typoStyle),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(Alignment.CenterVertically)
                .padding(horizontal = 2.dp)
        )
    }
}
