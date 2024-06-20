package com.ataglance.walletglance.ui.theme.widgets

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.domain.entities.Account
import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.records.EmptyRecordsHistoryMessageContainer
import com.ataglance.walletglance.ui.theme.uielements.records.RecordStackComponent
import com.ataglance.walletglance.ui.theme.uielements.records.TransferComponent
import com.ataglance.walletglance.ui.utils.needToIncludeYearToDate
import com.ataglance.walletglance.data.records.RecordStack
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.data.records.RecordsTypeFilter

@Composable
fun RecordHistoryWidget(
    recordStackList: List<RecordStack>,
    appTheme: AppTheme?,
    getCategoryAndIcon: (Int, Int?, RecordType?) -> Pair<Category?, Int?>?,
    getAccount: (Int) -> Account?,
    onRecordClick: (Int) -> Unit,
    onTransferClick: (Int) -> Unit,
    recordsTypeFilter: RecordsTypeFilter = RecordsTypeFilter.All
) {
    val includeYearToRecordDate = recordStackList.needToIncludeYearToDate()

    GlassSurface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
        ) {
            Text(
                text = stringResource(R.string.recent),
                color = GlanceTheme.onSurface,
                fontSize = 24.sp,
                fontWeight = FontWeight.Light
            )
            AnimatedContent(
                targetState = Pair(recordStackList, recordsTypeFilter),
                label = "records history widget content"
            ) { targetRecordStackListAndTypeFilter ->
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(370.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        for (recordStack in targetRecordStackListAndTypeFilter.first) {
                            if (recordStack.isTransfer()) {
                                TransferComponent(
                                    recordStack = recordStack,
                                    includeYearToDate = includeYearToRecordDate,
                                    appTheme = appTheme,
                                    getAccount = getAccount,
                                    onTransferClick = onTransferClick
                                )
                            } else {
                                RecordStackComponent(
                                    recordStack = recordStack,
                                    includeYearToDate = includeYearToRecordDate,
                                    getCategoryAndIcon = getCategoryAndIcon,
                                    getAccount = getAccount,
                                    onRecordClick = onRecordClick
                                )
                            }
                        }
                        if (targetRecordStackListAndTypeFilter.first.isNotEmpty()) {
                            Spacer(modifier = Modifier)
                        }
                    }
                    if (targetRecordStackListAndTypeFilter.first.isEmpty()) {
                        EmptyRecordsHistoryMessageContainer()
                    }
                }
            }
        }
    }
}