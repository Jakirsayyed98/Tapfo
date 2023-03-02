package app.tapho.ui.merchants.model

import app.tapho.ui.model.MiniApp

data class OfferData(
    val mini_app:List<MiniApp>,
    val visited:String,
    val label:String?,
    val end:String,
    var image: String,
    val code:String,
    val start_date:String,
    val end_date:String,
    val app_category_mini_app_id: String,
    val app_other_category_id: String,
    val macro_publisher: String,
    val merchant_payout: MerchantPayout,
    val tcash: String,
    val type: String,
    val add_macros_url: String,
    val app_category_id: String,
    val cashback: String,
    val created_at: String,
    val description: String,
    val id: String,
    val is_favourite: String,
    val name: String,
    val status: String,
    val url: String,
    val url_type: String,

    )// :MiniApp()
