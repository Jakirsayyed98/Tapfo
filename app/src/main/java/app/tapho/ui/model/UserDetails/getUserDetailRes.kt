package app.tapho.ui.model.UserDetails

data class getUserDetailRes(
    val data: List<Data>,
    val errorCode: String,
    val errorMsg: String
)