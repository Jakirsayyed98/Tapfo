package app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp

data class fin_merchant_payout(
    val id : String,
    val fin_mini_app_id:String,
    val cashback:String,
    val report:String,
    val terms:String,
    val payout_type:String,
    val status:String,
    val created_at:String,
    val popular_cashback:String,
    val fin_cashback_merchant_category_id:String,
    val image:String,
)
