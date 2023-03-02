package app.tapho.ui.model


import java.net.URLDecoder

open class MiniApp {
    var add_macros_url: String? = null
    var app_category_id: String? = null
    var app_sub_category_id: String? = null
    var created_at: String? = null
    var description: String? = null
    var id: String? = null
    var image: String? = null
    val banner: String? = null
    val card: String? = null
    val cover:String? = null
    val logo:String? = null
    var is_favourite: String? = null
    var total_favourite_count: String? = ""
    var merchant_payout: MerchantPayout? = null
    var name: String? = null
    var punchline: String? = null
    var status: String? = null
    var tcash: String? = null
    var url: String? = null
    var url_type: String? = null
    var terms: String? = null
    var payout_type: String? = null
    var type: String? = null
    var cashback: String? = null
    var popular_cashback: String? = null
    var report: String? = null
    var cashback_merchant_category_id: String? = null
    var offer_count: String?=""
    var coupon_count: String?=""
    var merchant_model  : MerchantPayout? = null

    fun getDeals():String{
        offer_count?.let { offer->
            coupon_count?.let { coupon->
                return (offer.toInt()+coupon.toInt()).toString()
            }
        }
        return "0"
    }

    @JvmName("getCashback1")
    fun getCashback():String{
        kotlin.runCatching {
            return URLDecoder.decode(cashback, "UTF-8")
        }
        return cashback?:""
    }

}