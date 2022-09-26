package app.tapho.ui.merchants.model

import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback

data class OfferData(
    val mini_app:List<MiniApp>,
    val visited:String,
    val label:String?,
    val end:String,
    val code:String,
    val start_date:String,
    val end_date:String
):MiniApp()
