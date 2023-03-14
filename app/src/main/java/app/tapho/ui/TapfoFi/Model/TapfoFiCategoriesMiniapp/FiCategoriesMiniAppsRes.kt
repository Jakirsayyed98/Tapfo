package app.tapho.ui.TapfoFi.Model.TapfoFiCategoriesMiniapp

data class FiCategoriesMiniAppsRes(
    val `data`: List<Any>,
    val errorCode: String,
    val errorMsg: String,
    val fin_mini_app: List<FinMiniApp>,
    val fin_sub_category: List<Any>
)