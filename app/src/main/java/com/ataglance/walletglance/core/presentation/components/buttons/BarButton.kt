package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun BarButton(
    text: String,
    active: Boolean = false,
    fontSize: TextUnit = 16.sp,
    onClick: () -> Unit
) {
    val containerColorLighter by animateColorAsState(
        targetValue = if (active) {
            GlanceTheme.primaryGradientLightToDark.first
        } else {
            GlanceTheme.glassGradientLightToDark.first
        },
        animationSpec = tween(300),
        label = "BarButton container lighter color"
    )
    val containerColorDarker by animateColorAsState(
        targetValue = if (active) {
            GlanceTheme.primaryGradientLightToDark.second
        } else {
            GlanceTheme.glassGradientLightToDark.second
        },
        animationSpec = tween(300),
        label = "BarButton container darker color"
    )
    val contentColor by animateColorAsState(
        targetValue = if (active) {
            GlanceTheme.onPrimary
        } else {
            GlanceTheme.primary
        },
        animationSpec = tween(300),
        label = "BarButton content color"
    )
    val borderColor by animateColorAsState(
        targetValue = if (active) {
            Color.Transparent
        } else {
            GlanceTheme.onGlassSurfaceBorder
        },
        animationSpec = tween(300),
        label = "BarButton border color"
    )

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = contentColor,
        ),
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .bounceClickEffect()
            .clip(RoundedCornerShape(50))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(containerColorDarker, containerColorLighter),
                    start = Offset(75f, 210f),
                    end = Offset(95f, -10f)
                )
            )
            .border(1.dp, borderColor, RoundedCornerShape(50))
    ) {
        Text(
            text = text,
            fontSize = fontSize,
            fontFamily = Manrope
        )
    }
}