package app.tapho.ui.localbizzUI.Model.BusinessSubCategory

data class Data(
    val business_category_id: String,
    val created_at: String,
    val description: Any,
    val id: String,
    val image: Any,
    val name: String,
    val status: String,
    var checked :Boolean = false // Static
)