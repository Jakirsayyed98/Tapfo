package app.tapho.ui.model.mini_app_data

data class MiniAppsDataRes(
    val app_category_detail: List<AppCategoryDetail>,
    val app_data: List<Any>,
    val category: List<Category>,
    val `data`: List<Any>,
    val errorCode: String,
    val errorMsg: String,
    val mini_app_detail: List<MiniAppDetail>
)