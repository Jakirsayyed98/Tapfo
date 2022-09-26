package app.tapho.ui.merchants.model

import app.tapho.network.BaseRes
import app.tapho.ui.model.AppCategory
import app.tapho.ui.model.BannerList

data class MerchantOfferRes (
    var `data`: ArrayList<Any>?,
    var category: List<AppCategory>?,
    var store_deals: List<StoreDeals>?,
    var banner_list: ArrayList<BannerList>?
//    var banner_list: List<BannerList>?
): BaseRes()