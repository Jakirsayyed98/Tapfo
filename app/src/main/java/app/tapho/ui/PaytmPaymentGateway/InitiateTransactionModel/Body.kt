package app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel

data class Body(
    val authenticated: Boolean,
    val isPromoCodeValid: Boolean,
    val resultInfo: ResultInfo,
    val txnToken: String
)