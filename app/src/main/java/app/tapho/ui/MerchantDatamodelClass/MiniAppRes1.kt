package app.tapho.ui.MerchantDatamodelClass

import app.tapho.ui.model.AppSubCategory

data class MiniAppRes1(
    val app_sub_category: List<AppSubCategory>,
    val banner_list: List<Any>,
    val `data`: List<Any>,
    val errorCode: String,
    val errorMsg: String,
    val mini_app: List<MiniApp>
)