package app.tapho.ui.tcash.model

import app.tapho.network.BaseRes


class TCashDasboardRes(
    var cash_available: String?,
    var claimed: String?,
    var data: List<TCashDashboardData>?,
    val txn: List<Txn>,
    var lifetime_earning: String?,
    var pending: String,
    var reject: String?,
    var verified: String?
):BaseRes()




