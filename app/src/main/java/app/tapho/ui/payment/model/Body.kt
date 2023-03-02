package app.tapho.ui.payment.model

class Body(
    var authenticated: Boolean?,
    var isPromoCodeValid: Boolean?,
    var resultInfo: ResultInfo?,
    var txnToken: String?
)