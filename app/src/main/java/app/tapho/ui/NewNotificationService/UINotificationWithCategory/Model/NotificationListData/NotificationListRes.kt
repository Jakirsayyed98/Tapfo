package app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData

data class NotificationListRes(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)