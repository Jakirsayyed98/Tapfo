package app.tapho.ui.localbizzUI.Model.BusinessCategories

data class Data(
    val created_at: String,
    val description: Any,
    val id: String,
    val image: Any,
    val name: String,
    val status: String,
    var checked : Boolean = false // custome added
)