package app.tapho.ui.home.ShopProduct.Model.ShopCategory

import app.tapho.ui.home.ShopProduct.Model.ShopCategory.Data

data class ShopCategory(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)