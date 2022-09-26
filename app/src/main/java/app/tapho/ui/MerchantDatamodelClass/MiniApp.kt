package app.tapho.ui.MerchantDatamodelClass

data class MiniApp(
    val add_macros_url: String,
    val app_category_id: String,
    val app_sub_category_id: String,
    val cashback: String,
    val created_at: String,
    val description: Any,
    val id: String,
    val image: String,
    val card: String,
    val is_favourite: Int,
    val macro_publisher: String,
    val merchant_payout: MerchantPayout,
    var popular_cashback: String,
    val name: String,
    val punchline: String,
    val status: String,
    val tcash: Int,
    val total_favourite_count: Int,
    val url: String,
    val url_type: String
)