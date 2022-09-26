package app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData

import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData.admin.Data

data class adminmodel(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)