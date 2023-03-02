package app.tapho.ui.PaytmPaymentGateway.TransactionProcess

data class Body(
    val deepLinkInfo: DeepLinkInfo,
    val resultInfo: ResultInfo,
    val riskContent: RiskContent
)