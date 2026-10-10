package com.example.arccalc.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arccalc.ui.theme.LocalArcPalette

@Composable
fun StandardPad(
    onDigitClick: (String) -> Unit,
    onOperatorClick: (String) -> Unit,
    onClearClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onEqualsClick: () -> Unit,
    onParenthesisClick: (String) -> Unit,
    onPercentClick: () -> Unit,
    onDotClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalArcPalette.current

    val isCustomRowGlow = palette.row1BorderGlow != palette.borderGlow || palette.row2BorderGlow != palette.borderGlow

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Row 1 (5 buttons): AC, DEL, (, ), %
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ArcButton(
                text = "AC",
                role = ButtonRole.ACTION_CLEAR,
                onClick = onClearClick,
                shape = RoundedCornerShape(18.dp),
                customBorderColor = if (isCustomRowGlow) Color(0xFFFF2D55) else null,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f).height(54.dp),
                testTag = "btn_ac"
            )
            ArcButton(
                text = "DEL",
                role = ButtonRole.FUNCTION,
                onClick = onDeleteClick,
                onLongClick = onClearClick,
                shape = RoundedCornerShape(18.dp),
                customBorderColor = if (isCustomRowGlow) Color(0xFFFF6A00) else null,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f).height(54.dp),
                testTag = "btn_del"
            )
            ArcButton(
                text = "(",
                role = ButtonRole.FUNCTION,
                onClick = { onParenthesisClick("(") },
                shape = RoundedCornerShape(18.dp),
                customBorderColor = if (isCustomRowGlow) Color(0xFF00E676) else null,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f).height(54.dp),
                testTag = "btn_paren_open"
            )
            ArcButton(
                text = ")",
                role = ButtonRole.FUNCTION,
                onClick = { onParenthesisClick(")") },
                shape = RoundedCornerShape(18.dp),
                customBorderColor = if (isCustomRowGlow) Color(0xFF00E676) else null,
                fontSize = 16.sp,
                modifier = Modifier.weight(1f).height(54.dp),
                testTag = "btn_paren_close"
            )
            ArcButton(
                text = "%",
                role = ButtonRole.FUNCTION,
                onClick = onPercentClick,
                shape = RoundedCornerShape(18.dp),
                customBorderColor = if (isCustomRowGlow) Color(0xFF00E676) else null,
                fontSize = 15.sp,
                modifier = Modifier.weight(1f).height(54.dp),
                testTag = "btn_percent"
            )
        }

        // Row 2: 1, 2, 3, + (+ is circular)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ArcButton(
                text = "1",
                role = ButtonRole.NUMBER,
                onClick = { onDigitClick("1") },
                customBackgroundColor = if (isCustomRowGlow) palette.numberKeys else null,
                customBorderColor = if (isCustomRowGlow) palette.row1BorderGlow else null,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_1"
            )
            ArcButton(
                text = "2",
                role = ButtonRole.NUMBER,
                onClick = { onDigitClick("2") },
                customBackgroundColor = if (isCustomRowGlow) palette.numberKeys else null,
                customBorderColor = if (isCustomRowGlow) palette.row1BorderGlow else null,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_2"
            )
            ArcButton(
                text = "3",
                role = ButtonRole.NUMBER,
                onClick = { onDigitClick("3") },
                customBackgroundColor = if (isCustomRowGlow) palette.numberKeys else null,
                customBorderColor = if (isCustomRowGlow) palette.row1BorderGlow else null,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_3"
            )
            ArcButton(
                text = "+",
                role = ButtonRole.OPERATOR,
                onClick = { onOperatorClick("+") },
                shape = CircleShape,
                customBorderColor = if (isCustomRowGlow) palette.operatorsBorderGlow else null,
                fontSize = 24.sp,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_add"
            )
        }

        // Row 3: 4, 5, 6, - (- is circular)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ArcButton(
                text = "4",
                role = ButtonRole.NUMBER,
                onClick = { onDigitClick("4") },
                customBackgroundColor = if (isCustomRowGlow) palette.row2Keys else null,
                customBorderColor = if (isCustomRowGlow) palette.row2BorderGlow else null,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_4"
            )
            ArcButton(
                text = "5",
                role = ButtonRole.NUMBER,
                onClick = { onDigitClick("5") },
                customBackgroundColor = if (isCustomRowGlow) palette.row2Keys else null,
                customBorderColor = if (isCustomRowGlow) palette.row2BorderGlow else null,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_5"
            )
            ArcButton(
                text = "6",
                role = ButtonRole.NUMBER,
                onClick = { onDigitClick("6") },
                customBackgroundColor = if (isCustomRowGlow) palette.row2Keys else null,
                customBorderColor = if (isCustomRowGlow) palette.row2BorderGlow else null,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_6"
            )
            ArcButton(
                text = "−",
                role = ButtonRole.OPERATOR,
                onClick = { onOperatorClick("-") },
                shape = CircleShape,
                customBorderColor = if (isCustomRowGlow) palette.operatorsBorderGlow else null,
                fontSize = 24.sp,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_subtract"
            )
        }

        // Row 4: 7, 8, 9, × (× is circular)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ArcButton(
                text = "7",
                role = ButtonRole.NUMBER,
                onClick = { onDigitClick("7") },
                customBackgroundColor = if (isCustomRowGlow) palette.row3Keys else null,
                customBorderColor = if (isCustomRowGlow) palette.row3BorderGlow else null,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_7"
            )
            ArcButton(
                text = "8",
                role = ButtonRole.NUMBER,
                onClick = { onDigitClick("8") },
                customBackgroundColor = if (isCustomRowGlow) palette.row3Keys else null,
                customBorderColor = if (isCustomRowGlow) palette.row3BorderGlow else null,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_8"
            )
            ArcButton(
                text = "9",
                role = ButtonRole.NUMBER,
                onClick = { onDigitClick("9") },
                customBackgroundColor = if (isCustomRowGlow) palette.row3Keys else null,
                customBorderColor = if (isCustomRowGlow) palette.row3BorderGlow else null,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_9"
            )
            ArcButton(
                text = "×",
                role = ButtonRole.OPERATOR,
                onClick = { onOperatorClick("×") },
                shape = CircleShape,
                customBorderColor = if (isCustomRowGlow) palette.operatorsBorderGlow else null,
                fontSize = 24.sp,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_multiply"
            )
        }

        // Row 5: 0, ., ÷, = (÷ is circular, = is wide prominent accent)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ArcButton(
                text = "0",
                role = ButtonRole.NUMBER,
                onClick = { onDigitClick("0") },
                customBackgroundColor = if (isCustomRowGlow) palette.numberKeys else null,
                customBorderColor = if (isCustomRowGlow) palette.row1BorderGlow else null,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_0"
            )
            ArcButton(
                text = ".",
                role = ButtonRole.NUMBER,
                onClick = onDotClick,
                customBorderColor = if (isCustomRowGlow) palette.operatorsBorderGlow else null,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_dot"
            )
            ArcButton(
                text = "÷",
                role = ButtonRole.OPERATOR,
                onClick = { onOperatorClick("÷") },
                shape = CircleShape,
                customBorderColor = if (isCustomRowGlow) palette.operatorsBorderGlow else null,
                fontSize = 24.sp,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_divide"
            )
            ArcButton(
                text = "=",
                role = ButtonRole.ACTION_EQUALS,
                onClick = onEqualsClick,
                fontSize = 26.sp,
                modifier = Modifier.weight(1f).height(58.dp),
                testTag = "btn_equals"
            )
        }
    }
}
