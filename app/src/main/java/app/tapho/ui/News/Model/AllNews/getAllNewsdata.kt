package app.tapho.ui.News.Model.AllNews

data class getAllNewsdata(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)