package app.tapho.ui.localbizzUI.Model.SaveBusiness

data class SaveBusinessRes(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)