package app.tapho.ui.PaytmPaymentGateway.TransactionStatus

data class Body(
    val bankTxnId: String,
    val gatewayName: String,
    val mid: String,
    val orderId: String,
    val paymentMode: String,
    val refundAmt: String,
    val resultInfo: ResultInfo,
    val txnAmount: String,
    val txnDate: String,
    val txnId: String,
    val txnType: String
)