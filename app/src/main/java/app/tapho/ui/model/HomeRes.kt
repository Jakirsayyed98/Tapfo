package app.tapho.ui.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import app.tapho.network.BaseRes

@Entity
class HomeRes(
    @PrimaryKey(autoGenerate = true)
    val id:Int,

    var data: ArrayList<Any>?,
    var app_category: ArrayList<AppCategory>?,
    var banner_list1: ArrayList<BannerList>?,
    var banner_list10: ArrayList<BannerList10>?,
    var banner_list2: ArrayList<BannerList2>?,
    var banner_list3: ArrayList<BannerList3>?,
    var banner_list4: ArrayList<BannerList4>?,
    var banner_list5: ArrayList<BannerList5>?,
    var banner_list6: ArrayList<BannerList6>?,
    var banner_list7: ArrayList<BannerList7>?,
    var banner_list8: ArrayList<BannerList8>?,
    var banner_list9: ArrayList<BannerList9>?,
//    var cashback_merchant: ArrayList<CashbackMerchant>?,
//    var headlines1: ArrayList<AppCategory>?,
    var headlines: ArrayList<Headline>?,
    var new_cashback: ArrayList<NewCashback>?,
//    var games: ArrayList<Data>,
    var new_cashback_merchant: ArrayList<CashbackMerchant>?,
    var popular: ArrayList<Popular>?,
    var profile_detail: ArrayList<app.tapho.ui.model.UserDetails.Data>?,
    var promoted_app: ArrayList<PromotedApp>?,
    val super_category: ArrayList<SuperCategory>?,
    var services: ArrayList<Service>?

) : BaseRes()