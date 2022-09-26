package app.tapho.ui.wallet.model

data class CashbackCouponsModel(
    val id: String,
    val amount: String,
    val cashback: String,
    val cashback_percentage: String,
    val status: String,
    var isSelected: Boolean,
)