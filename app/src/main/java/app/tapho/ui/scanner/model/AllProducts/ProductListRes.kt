package app.tapho.ui.scanner.model.AllProducts

data class ProductListRes(
    val data: List<Data>,
    val errorCode: String,
    val errorMsg: String
)