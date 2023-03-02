package app.tapho.ui.tcash.model

data class PullVoucher(
    val ProductGuid: String,
    val ProductName: String,
    val VoucherName: String,
    val Vouchers: List<Voucher>
)