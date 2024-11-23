package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.auth.domain.model.AuthenticationSuccessfulScreenType
import com.ataglance.walletglance.auth.domain.model.ProfileScreenTypeEnum
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.screenContainers.ResultMessageScreenContainer

@Composable
fun AuthSuccessfulScreen(
    screenType: AuthenticationSuccessfulScreenType,
    onContinueButtonClick: () -> Unit
) {
    ResultMessageScreenContainer(
        message = stringResource(screenType.getProfileScreenTitleRes()),
        buttonText = stringResource(screenType.getProfileScreenPrimaryButtonTextRes()),
        onContinueButtonClick = onContinueButtonClick
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun AuthSuccessfulScreenPreview() {
    PreviewWithMainScaffoldContainer(appTheme = AppTheme.LightDefault) {
        AuthSuccessfulScreen(
            screenType = AuthenticationSuccessfulScreenType(ProfileScreenTypeEnum.AfterSignUp),
            onContinueButtonClick = {}
        )
    }
}