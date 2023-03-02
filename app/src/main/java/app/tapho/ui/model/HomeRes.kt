package app.tapho.ui.model

import app.tapho.network.BaseRes
import app.tapho.ui.games.models.getGames.Data

class HomeRes(
    var `data`: ArrayList<Any>?,
    var app_category: ArrayList<AppCategory>?,
    var banner_list1: ArrayList<BannerList>?,
    var banner_list10: ArrayList<BannerList>?,
    var banner_list2: ArrayList<BannerList>?,
    var banner_list3: ArrayList<BannerList>?,
    var banner_list4: ArrayList<BannerList>?,
    var banner_list5: ArrayList<BannerList>?,
    var banner_list6: ArrayList<BannerList>?,
    var banner_list7: ArrayList<BannerList>?,
    var banner_list8: ArrayList<BannerList>?,
    var banner_list9: ArrayList<BannerList>?,
    var cashback_merchant: ArrayList<CashbackMerchant>?,
//    var headlines1: ArrayList<AppCategory>?,
    var headlines: ArrayList<Headline>?,
    var new_cashback: ArrayList<NewCashback>?,
    var games: ArrayList<Data>,
    var new_cashback_merchant: ArrayList<CashbackMerchant>?,
    var popular: ArrayList<Popular>?,
    var miniApp: ArrayList<MiniApp>?,
    var profile_detail: ArrayList<app.tapho.ui.model.UserDetails.Data>?,
    var promoted_app: ArrayList<PromotedApp>?,
    val super_category: List<SuperCategory>?,
    var services: ArrayList<Service>?






) : BaseRes()