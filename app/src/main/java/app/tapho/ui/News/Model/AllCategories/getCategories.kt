package app.tapho.ui.News.Model.AllCategories

data class getCategories(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String
)