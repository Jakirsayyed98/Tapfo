package app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData.admin

import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData.DataX

data class Data(
    val app_data_id: String,
    val body: Any,
    val created_at: String,
    val `data`: List<DataX>,
    val gamezop_id: Any,
    val id: String,
    val merchant_status: Any,
    val noti_type: String,
    val notification_admin_id: String,
    val offer_name: Any,
    val read: String,
    val title: String,
    val type: String,
    val user_id: String
)