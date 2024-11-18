package com.ataglance.walletglance.core.presentation.components.screenContainers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.LocalWindowType
import com.ataglance.walletglance.core.presentation.Typography
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface

@Composable
fun GlassSurfaceScreenContainerWithTitle(
    title: String,
    glassSurfaceContent: @Composable BoxScope.() -> Unit,
    fillGlassSurface: Boolean = false,
    glassSurfaceFilledWidths: FilledWidthByScreenType = FilledWidthByScreenType(compact = .86f),
    buttonUnderGlassSurface: @Composable (() -> Unit)? = null,
    bottomButton: @Composable () -> Unit
) {
    ScreenContainer(
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = Typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth(FilledWidthByScreenType().getByType(LocalWindowType.current))
        )
        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f, fill = fillGlassSurface),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            GlassSurface(
                modifier = Modifier.weight(1f, fill = fillGlassSurface),
                filledWidths = glassSurfaceFilledWidths,
                content = glassSurfaceContent
            )
            buttonUnderGlassSurface?.let { it() }
        }

        Spacer(modifier = Modifier.height(32.dp))
        bottomButton()
    }
}