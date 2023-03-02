package app.tapho.ui.merchants.model

import app.tapho.network.BaseRes
import app.tapho.ui.model.AppSubCategory
import app.tapho.ui.model.BannerList
import app.tapho.ui.model.MiniApp
import app.tapho.ui.model.NewCashback

class MiniAppRes(
    var mini_app: List<MiniApp>?,
    var app_sub_category: List<AppSubCategory>?,
    var newCashback: List<NewCashback>?,
    var banner_list: List<BannerList>?,
    val data: List<Any>,


) : BaseRes()