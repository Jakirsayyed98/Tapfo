package app.tapho.ui.home.ShopProduct.Model.ShopSubCategory

data class Data(
    val created_at: String,
    val description: Any,
    val id: String,
    val image: Any,
    val name: String,
    val shop_category_id: String,
    val status: String,

    var isSelected:Boolean  // for local use
)