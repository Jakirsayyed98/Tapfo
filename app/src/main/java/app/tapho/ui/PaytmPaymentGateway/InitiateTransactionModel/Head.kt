package app.tapho.ui.PaytmPaymentGateway.InitiateTransactionModel

data class Head(
    val responseTimestamp: String,
    val signature: String,
    val version: String
)