package app.tapho.ui.model

class NotificationData(
    var app_data_id: Int?,
    var created_at: String?,
    var `data`: List<NotificationData2>?,
    var id: Int?,
    var notification_admin_id: Int?,
    var read: Int?,
    var title: String?,
    var type: Int?,
    var user_id: Int?
)