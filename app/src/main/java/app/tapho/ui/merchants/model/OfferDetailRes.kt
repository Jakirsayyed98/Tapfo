package app.tapho.ui.merchants.model

import app.tapho.network.BaseRes

data class OfferDetailRes(
    var app_data: List<OfferData>?,
    val category: List<Category>,
) : BaseRes()
