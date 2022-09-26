package app.tapho.ui.vouchers.model

import app.tapho.network.BaseRes
import app.tapho.ui.model.AppCategory

class VoucherCategoryRes(
    var `data`: List<AppCategory>?,
) : BaseRes()