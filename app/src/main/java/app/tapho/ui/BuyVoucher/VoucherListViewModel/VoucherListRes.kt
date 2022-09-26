package app.tapho.ui.BuyVoucher.VoucherListViewModel

data class VoucherListRes(
    val `data`: List<Data>,
    val errorCode: String,
    val errorMsg: String,
    val voucher_sub_category: List<VoucherSubCategory>
)