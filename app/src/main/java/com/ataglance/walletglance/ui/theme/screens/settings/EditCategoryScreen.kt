package com.ataglance.walletglance.ui.theme.screens.settings

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.EditCategoryUiState
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SecondaryButton
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.fields.CustomTextFieldWithLabel

@Composable
fun EditCategoryScreen(
    scaffoldPadding: PaddingValues,
    uiState: EditCategoryUiState,
    categoryIconList: List<Pair<String, Int>>,
    onNameChange: (String) -> Unit,
    onIconClick: (String) -> Unit,
    onSaveButton: () -> Unit,
    onDeleteButton: () -> Unit
) {
    val lazyGridState = rememberLazyGridState()

    if (uiState.category != null) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = scaffoldPadding.calculateTopPadding() +
                            dimensionResource(R.dimen.screen_vertical_padding),
                    bottom = dimensionResource(R.dimen.screen_vertical_padding)
                )
        ) {
            if (uiState.showDeleteCategoryButton) {
                SecondaryButton(onClick = onDeleteButton, text = stringResource(R.string.delete))
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
            }
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                GlassSurface {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.field_gap)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                            .padding(start = 12.dp, end = 12.dp, top = 24.dp)
                    ) {
                        CustomTextFieldWithLabel(
                            text = uiState.category.name,
                            labelText = stringResource(R.string.name),
                            onValueChange = onNameChange
                        )
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(40.dp),
                            state = lazyGridState,
                            contentPadding = PaddingValues(vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .size(300.dp, 500.dp)
                        ) {
                            items(items = categoryIconList, key = { it.first }) { nameAndIconRes ->
                                val color by animateColorAsState(
                                    targetValue = if (uiState.category.iconName == nameAndIconRes.first) {
                                        GlanceTheme.primary
                                    } else {
                                        GlanceTheme.onSurface
                                    },
                                    label = "icon color"
                                )
                                Icon(
                                    painter = painterResource(nameAndIconRes.second),
                                    contentDescription = nameAndIconRes.first,
                                    tint = color,
                                    modifier = Modifier
                                        .bounceClickEffect(.97f) {
                                            onIconClick(nameAndIconRes.first)
                                        }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
            PrimaryButton(onClick = onSaveButton, text = stringResource(R.string.save), enabled = uiState.allowSaving)
        }
    } else {
        Text(text = stringResource(R.string.category_not_found))
    }
}