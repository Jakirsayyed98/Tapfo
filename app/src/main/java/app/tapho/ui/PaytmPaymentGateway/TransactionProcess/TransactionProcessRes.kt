package app.tapho.ui.PaytmPaymentGateway.TransactionProcess

data class TransactionProcessRes(
    val data: Data,
    val errorCode: String,
    val errorMsg: String
)