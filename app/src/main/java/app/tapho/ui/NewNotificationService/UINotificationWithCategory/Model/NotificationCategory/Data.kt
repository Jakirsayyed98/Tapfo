package app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationCategory

data class Data(
    val date: String,
    val id: String,
    val name: String,
    val notification: List<Notification>,
    val type: Int
)