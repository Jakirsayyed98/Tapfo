package app.tapho.ui.home.ShopProduct.Model.ShopCategory

data class Data(
    val created_at: String,
    val description: Any,
    val id: String,
    val image: Any,
    val name: String,
    val status: String,

    var isSelected:Boolean  // for local use
)