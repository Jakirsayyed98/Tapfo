package app.tapho.ui.localbizzUI.Model.getBusinessDetails

data class getBusinessDetailRes(
    val data: ArrayList<Data>,
    val errorCode: String,
    val errorMsg: String
)