package app.tapho.ui.PaytmPaymentGateway.TransactionProcess

data class DeepLinkInfo(
    val cashierRequestId: String,
    val deepLink: String,
    val orderId: String,
    val transId: String
)