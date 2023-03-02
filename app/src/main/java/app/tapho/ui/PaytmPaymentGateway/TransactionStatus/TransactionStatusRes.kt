package app.tapho.ui.PaytmPaymentGateway.TransactionStatus

data class TransactionStatusRes(
    val `data`: Data,
    val errorCode: String,
    val errorMsg: String
)