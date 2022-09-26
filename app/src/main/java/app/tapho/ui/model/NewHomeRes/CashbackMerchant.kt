package app.tapho.ui.model.NewHomeRes

data class CashbackMerchant(
    val app_category_mini_app_id: String,
    val cashback: String,
    val cashback_merchant_category: CashbackMerchantCategory,
    val cashback_merchant_category_id: String,
    val created_at: String,
    val id: String,
    val image: String,
    val mini_app: MiniApp,
    val payout_type: String,
    val popular_cashback: String,
    val report: String,
    val status: String,
    val terms: Any
)