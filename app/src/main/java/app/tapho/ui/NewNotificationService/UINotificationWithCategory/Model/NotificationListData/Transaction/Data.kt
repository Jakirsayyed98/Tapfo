package app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData.Transaction

data class Data(
    val app_data_id: String,
    val body: String,
    val created_at: String,
    val `data`: List<Any>,
    val gamezop_id: Any,
    val id: String,
    val merchant_status: String,
    val noti_type: String,
    val notification_admin_id: String,

    val offer_name: String,
    val read: String,
    val title: String,
    val type: String,
    val user_id: String
)