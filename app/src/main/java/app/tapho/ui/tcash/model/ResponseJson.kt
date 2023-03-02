package app.tapho.ui.tcash.model

data class ResponseJson(
    val ProductGuid: String,
    val ProductName: String,
    val VoucherName: String,
    val Vouchers: List<Voucher>
)