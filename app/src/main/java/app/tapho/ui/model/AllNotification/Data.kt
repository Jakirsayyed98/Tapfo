package app.tapho.ui.model.AllNotification

data class Data(
    val admin_noti_type: String,
    val app_category_mini_app_id: String,
    val app_data_id: String,
    val body: String,
    val created_at: String,
    val data: List<DataX>,
    val gamezop: List<Gamezop>,
    val gamezop_id: String,
    val id: String,
    val merchant_postback_value: List<MerchantPostbackValue>,
    val merchant_postback_value_id: String,
    val merchant_status: String,
    val noti_type: String,
    val notification_admin_id: String,
    val offer_data: List<OfferData>,
    val offer_name: String,
    val read: String,
    val tag: String,
    val title: String,
    val type: String,
    val user_id: String
)