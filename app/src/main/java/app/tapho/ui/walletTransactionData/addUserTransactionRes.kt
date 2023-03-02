package app.tapho.ui.walletTransactionData

data class addUserTransactionRes(
    val `data`: List<Any>,
    val errorCode: String,
    val user_txn_id: String,
    val errorMsg: String
)