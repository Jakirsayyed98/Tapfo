package app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData

import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData.Transaction.Data

data class transaction(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)