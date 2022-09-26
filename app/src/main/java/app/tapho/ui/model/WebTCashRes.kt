package app.tapho.ui.model

import app.tapho.network.BaseRes
import app.tapho.ui.MerchantDatamodelClass.MiniAppRes1
import app.tapho.ui.tcash.model.TCashDashboardData

class WebTCashRes(
    var data: List<MiniApp>,
    var category: List<TCashCategory>?,
    var mini_app: List<MiniApp>?,
    var cashback: List<NewCashback>?,
    var miniAppRes1:  List<MiniAppRes1>?,
    var dashboardData: List<TCashDashboardData>?,

) : BaseRes()