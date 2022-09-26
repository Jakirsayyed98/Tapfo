package app.tapho.ui.model.AllNotification

data class AllNotificationRes(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)