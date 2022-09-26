package app.tapho.ui.newAPI

data class MiniApp(
    val add_macros_url: String,
    val app_category_id: String,
    val app_sub_category_id: String,
    val created_at: String,
    val description: Any,
    val id: String,
    val image: String,
    val is_favourite: Int,
    val macro_publisher: String,
    val name: String,
    val punchline: String,
    val status: String,
    val tcash: Int,
    val url: String,
    val url_type: String
)