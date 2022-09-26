package app.tapho.ui.newAPI

data class WebTCashRes(
    val category: List<Category>,
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String,
    val mini_app: List<MiniApp>
)