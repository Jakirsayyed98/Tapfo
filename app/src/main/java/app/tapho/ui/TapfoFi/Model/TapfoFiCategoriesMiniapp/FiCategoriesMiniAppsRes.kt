package app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp

data class FiCategoriesMiniAppsRes(
    val `data`: List<Any>,
    val errorCode: String,
    val errorMsg: String,
    val fin_mini_app: ArrayList<FinMiniApp>,
    val fin_sub_category: ArrayList<FinSubCategory>
)