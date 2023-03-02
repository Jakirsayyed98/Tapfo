package app.tapho.ui.tcash.model

data class VPullVouchersResult(
    val ErrorCode: String,
    val ErrorMessage: String,
    val ExternalOrderIdOut: String,
    val Message: String,
    val PullVouchers: List<PullVoucher>,
    val ResultType: String
)