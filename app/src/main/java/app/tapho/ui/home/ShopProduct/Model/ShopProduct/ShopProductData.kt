package app.tapho.ui.home.ShopProduct.Model.ShopProduct

data class ShopProductData(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String,

    var isSelected:Boolean  // for local use
)