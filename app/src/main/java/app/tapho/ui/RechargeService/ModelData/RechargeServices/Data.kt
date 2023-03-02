package app.tapho.ui.RechargeService.ModelData.RechargeServices


data class Data(
    val api_name: String,
    val created_at: String,
    val id: String,
    val image: Any,
    val name: String,
    val service_banner: List<ServiceBanner>,
    val status: String
)