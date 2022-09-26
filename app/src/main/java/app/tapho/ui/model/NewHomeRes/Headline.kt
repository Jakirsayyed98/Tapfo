package app.tapho.ui.model.NewHomeRes

data class Headline(
    val created_at: String,
    val description: String,
    val heading: String,
    val heading_status: String,
    val id: String,
    val image: String,
    val mini_app: List<MiniAppX>,
    val name: String,
    val status: String
)