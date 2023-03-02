package app.tapho.ui.model

import java.io.Serializable

class AppCategory(
    var created_at: String?,
    var description: String?,
    var heading: String?,
    var heading_status: String?,
    var offer_count: String?,
    var coupon_count: String?,
    var id: String?,
    var image: String?,
    var name: String?,
    var status: String?,
    var isSelected: Boolean,
    var mini_app: List<MiniApp>?
    ): Serializable {
    fun getDeals(): String {
        offer_count?.let { offer ->
            coupon_count?.let { coupon ->
                return (offer.toInt() + coupon.toInt()).toString()
            }
        }
        return "0"
    }
}