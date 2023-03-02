package app.tapho.ui.localbizzUI.Model.AddRating

import app.tapho.ui.BuyVoucher.CategoriesModel.Data

data class AddBusinessRatingRes(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)