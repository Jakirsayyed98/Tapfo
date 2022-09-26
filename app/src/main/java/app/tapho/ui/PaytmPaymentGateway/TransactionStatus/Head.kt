package app.tapho.ui.PaytmPaymentGateway.TransactionStatus

data class Head(
    val responseTimestamp: String,
    val signature: String,
    val version: String
)