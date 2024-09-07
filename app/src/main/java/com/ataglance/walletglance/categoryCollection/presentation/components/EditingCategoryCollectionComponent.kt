package com.ataglance.walletglance.categoryCollection.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.presentation.components.CategoryIconComponent
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionWithCategories
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun EditingCategoryCollectionComponent(
    collection: CategoryCollectionWithCategories,
    onClick: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val categoriesWithUniqueIcons = collection.categoryList
        ?.groupBy { it.icon }
        ?.flatMap { (_, categories) ->
            categories.distinctBy { it.colorWithName }
        }
        ?: emptyList()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .bounceClickEffect(.98f, onClick = onClick)
            .clip(RoundedCornerShape(dimensionResource(R.dimen.record_corner_size)))
            .background(
                brush = Brush.linearGradient(
                    colors = GlanceTheme.onGlassSurfaceGradient,
                    start = Offset(50f, 190f),
                    end = Offset(100f, 0f)
                )
            )
            .border(
                1.dp,
                GlanceTheme.outlineVariant,
                RoundedCornerShape(dimensionResource(R.dimen.record_corner_size))
            )
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = collection.name,
                color = GlanceTheme.onSurface,
                fontSize = 20.sp,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.weight(1f, false)
            )
            Icon(
                painter = painterResource(R.drawable.short_arrow_right_icon),
                contentDescription = "right arrow",
                tint = GlanceTheme.onSurface,
                modifier = Modifier.size(12.dp, 20.dp)
            )
        }
        LazyRow(
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            userScrollEnabled = false
        ) {
            items(items = categoriesWithUniqueIcons, key = { it.id }) { category ->
                CategoryIconComponent(category)
            }
        }
    }
}