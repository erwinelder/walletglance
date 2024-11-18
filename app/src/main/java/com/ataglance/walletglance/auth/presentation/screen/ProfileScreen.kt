package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.LocalWindowType
import com.ataglance.walletglance.core.presentation.Typography
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.ScreenContainer

@Composable
fun ProfileScreen(
) {
    ScreenContainer {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.hello),
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth(FilledWidthByScreenType().getByType(LocalWindowType.current))
            )
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun ProfileScreenPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        ProfileScreen(
        )
    }
}