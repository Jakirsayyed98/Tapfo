package app.tapho.ui.scanner.model.SearchCurrentOrder

data class SearchBusinessOrdersRes(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)