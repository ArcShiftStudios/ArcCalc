package com.example.arccalc.ui.components

import android.os.Build
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Applies hardware-accelerated RenderEffect blur on Android 12+ (API 31+)
 * without causing lag or frame drops on older devices.
 */
fun Modifier.safeGlassBlur(radius: Dp = 12.dp): Modifier {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        this.blur(radius)
    } else {
        this
    }
}
