package com.ataglance.walletglance.recordCreation.presentation.model.record

import androidx.compose.runtime.Stable
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.core.utils.addZeroIfDotIsAtTheBeginning
import com.ataglance.walletglance.core.utils.formatWithSpaces
import java.util.Locale

@Stable
data class RecordDraftItem(
    val lazyListKey: Int,
    val index: Int,
    val categoryWithSub: CategoryWithSub?,
    val note: String = "",
    val amount: String = "",
    val quantity: String = "",
    val collapsed: Boolean = false
) {

    fun getFormattedAmountOrPlaceholder(): String {
        return amount
            .takeIf { it.isNotBlank() && !(it.length == 1 && it.firstOrNull() == '.') }
            ?.toDouble()
            ?.formatWithSpaces()
            ?: return "------"
    }

    fun savingIsAllowed(): Boolean {
        return amount.isNotBlank() &&
                amount.last() != '.' &&
                amount.addZeroIfDotIsAtTheBeginning().toDouble() != 0.0 &&
                categoryWithSub != null
    }

    fun getTotalAmount(): Double {
        val totalAmount = amount.toDoubleOrNull()?.let { amountDouble ->
            amountDouble * (quantity.toIntOrNull()?.takeIf { it != 0 } ?: 1)
        } ?: 0.0
        return "%.2f".format(Locale.US, totalAmount).toDouble()
    }

}
