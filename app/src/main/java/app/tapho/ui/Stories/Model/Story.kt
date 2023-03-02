package app.tapho.ui.Stories.Model

data class Story(
    val add_macros_url: String,
    val app_category: List<AppCategory>,
    val app_category_id: String,
    val app_category_mini_app_id: String,
    val code: Any,
    val created_at: String,
    val description: Any,
    val end_date: Any,
    val id: String,
    val image: String,
    val label: Any,
    val macro_publisher: String,
    val name: String,
    val start_date: Any,
    val status: String,
    val story_category_id: String,
    val url: String,
    val url_type: String
)