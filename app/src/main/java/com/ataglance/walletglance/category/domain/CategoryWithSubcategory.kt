package com.ataglance.walletglance.category.domain

import androidx.compose.runtime.Stable

@Stable
data class CategoryWithSubcategory(
    val category: Category,
    val subcategory: Category? = null
) {

    fun getSubcategoryOrCategory(): Category {
        return subcategory ?: category
    }

    fun matchCategoriesIds(categoriesIds: List<Int>): Boolean {
        return categoriesIds.contains(subcategory?.id ?: category.id)
    }

    fun groupParentAndSubcategoryOrderNums(): Double {
        return category.orderNum.toDouble() +
                (subcategory?.orderNum?.let { it.toDouble() / 100 } ?: 0.0)
    }

}