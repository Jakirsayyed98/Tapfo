package app.tapho.ui.vouchers.model

import app.tapho.network.BaseRes
import app.tapho.ui.model.AppSubCategory

class VoucherListRes(
    var `data`: List<VoucherListData>?,
    var voucher_sub_category: List<AppSubCategory>?
):BaseRes()