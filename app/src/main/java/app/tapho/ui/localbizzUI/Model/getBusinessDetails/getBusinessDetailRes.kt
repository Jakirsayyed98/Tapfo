package app.tapho.ui.localbizzUI.Model.getBusinessDetails

data class getBusinessDetailRes(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)