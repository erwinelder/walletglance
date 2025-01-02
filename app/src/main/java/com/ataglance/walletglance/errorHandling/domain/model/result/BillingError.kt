package com.ataglance.walletglance.errorHandling.domain.model.result

enum class BillingError : Error {
    UserCancelledPurchase, UserNotSignedIn, NoNetwork, Unknown
}