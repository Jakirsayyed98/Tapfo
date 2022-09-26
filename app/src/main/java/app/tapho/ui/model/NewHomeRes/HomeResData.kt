package app.tapho.ui.model.NewHomeRes

data class HomeResData(
    val app_category: List<AppCategory>,
    val banner_list1: List<BannerList1>,
    val banner_list10: List<Any>,
    val banner_list2: List<BannerList2>,
    val banner_list3: List<BannerList3>,
    val banner_list4: List<BannerList4>,
    val banner_list5: List<BannerList5>,
    val banner_list6: List<BannerList6>,
    val banner_list7: List<BannerList7>,
    val banner_list8: List<Any>,
    val banner_list9: List<Any>,
    val cashback_merchant: List<CashbackMerchant>,
    val `data`: List<Any>,
    val errorCode: String,
    val errorMsg: String,
    val headlines: List<Headline>,
    val new_cashback: List<NewCashback>,
    val new_cashback_merchant: List<NewCashbackMerchant>,
    val popular: List<Popular>,
    val profile_detail: List<ProfileDetail>,
    val promoted_app: List<PromotedApp>,
    val services: List<Service>
)