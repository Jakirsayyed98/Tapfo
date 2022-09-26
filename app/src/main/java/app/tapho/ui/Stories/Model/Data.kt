package app.tapho.ui.Stories.Model

data class Data(
    val created_at: String,
    val description: Any,
    val id: String,
    val image: String,
    val name: String,
    val status: String,
    val story: List<Story>
)