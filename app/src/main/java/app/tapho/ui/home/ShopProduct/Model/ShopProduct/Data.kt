package app.tapho.ui.home.ShopProduct.Model.ShopProduct

data class Data(
    val app_category_mini_app_id: String,
    val created_at: String,
    val id: String,
    val image: String,
    val merchant_payout: MerchantPayout,
    val mini_app: MiniApp,
    val mrp_price: String,
    val name: String,
    val other_website_url: String,
    val sale_price: String,
    val shop_category_id: String,
    val shop_childcategory_id: String,
    val shop_subcategory_id: String,
    val status: String,
    val type: String,

    var isSelected:Boolean  // for local use
)