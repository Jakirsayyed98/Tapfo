package app.tapho.ui.BuyVoucher.VoucherDetails

data class voucherdatailRes(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String,
    val mini_app: List<Any>,
    val voucher_category: VoucherCategory,
    val voucher_sub_category: Any,
    val voucherlist_denomination: List<VoucherlistDenomination>
)