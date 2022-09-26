package app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData

import app.tapho.ui.NewNotificationService.UINotificationWithCategory.Model.NotificationListData.Game.Data

data class gamemodel(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)