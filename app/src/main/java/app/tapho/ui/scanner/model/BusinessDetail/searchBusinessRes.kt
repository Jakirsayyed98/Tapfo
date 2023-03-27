package app.tapho.ui.scanner.model.BusinessDetail

data class searchBusinessRes(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)