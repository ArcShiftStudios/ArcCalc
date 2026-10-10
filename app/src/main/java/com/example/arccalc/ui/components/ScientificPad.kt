package com.example.arccalc.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.arccalc.ui.theme.LocalArcPalette

@Composable
fun ScientificPad(
    visible: Boolean,
    is2ndActive: Boolean,
    onToggle2nd: () -> Unit,
    onButtonClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val palette = LocalArcPalette.current

    AnimatedVisibility(
        visible = visible,
        enter = expandVertically() + fadeIn(),
        exit = shrinkVertically() + fadeOut(),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Row 1: 2ND, SIN/SIN⁻¹, COS/COS⁻¹, TAN/TAN⁻¹, π
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ArcButton(
                    text = if (is2ndActive) "2ND•" else "2ND",
                    role = ButtonRole.FUNCTION,
                    customBackgroundColor = if (is2ndActive) palette.primaryAccent else null,
                    customTextColor = if (is2ndActive) (if (palette.isDark) palette.background else palette.textPrimary) else null,
                    onClick = onToggle2nd,
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_2nd"
                )
                ArcButton(
                    text = if (is2ndActive) "sin⁻¹" else "sin",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick(if (is2ndActive) "sin⁻¹(" else "sin(") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_sin"
                )
                ArcButton(
                    text = if (is2ndActive) "cos⁻¹" else "cos",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick(if (is2ndActive) "cos⁻¹(" else "cos(") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_cos"
                )
                ArcButton(
                    text = if (is2ndActive) "tan⁻¹" else "tan",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick(if (is2ndActive) "tan⁻¹(" else "tan(") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_tan"
                )
                ArcButton(
                    text = "π",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick("π") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_pi"
                )
            }

            // Row 2: ln/eˣ, log/10ˣ, √/x², ^, e
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ArcButton(
                    text = if (is2ndActive) "eˣ" else "ln",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick(if (is2ndActive) "e^(" else "ln(") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_ln"
                )
                ArcButton(
                    text = if (is2ndActive) "10ˣ" else "log",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick(if (is2ndActive) "10^(" else "log(") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_log"
                )
                ArcButton(
                    text = if (is2ndActive) "x²" else "√",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick(if (is2ndActive) "^2" else "√(") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_sqrt"
                )
                ArcButton(
                    text = "^",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick("^") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_pow"
                )
                ArcButton(
                    text = "e",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick("e") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_e"
                )
            }

            // Row 3: 1/x, |x|, x!, RAD/DEG indicator
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ArcButton(
                    text = "1/x",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick("1/(") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_recip"
                )
                ArcButton(
                    text = "|x|",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick("abs(") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_abs"
                )
                ArcButton(
                    text = "x!",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick("!") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 14.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_fact"
                )
                ArcButton(
                    text = "xʸ",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick("^") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 13.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_pow_xy"
                )
                ArcButton(
                    text = "±",
                    role = ButtonRole.SCIENTIFIC,
                    onClick = { onButtonClick("±") },
                    shape = RoundedCornerShape(16.dp),
                    fontSize = 15.sp,
                    modifier = Modifier.weight(1f).height(46.dp),
                    testTag = "btn_sci_neg"
                )
            }
        }
    }
}
