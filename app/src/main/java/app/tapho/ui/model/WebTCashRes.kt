package app.tapho.ui.model

import app.tapho.network.BaseRes


class WebTCashRes(
    var data: List<MiniApp>,
    var category: List<TCashCategory>?,
    var mini_app: List<MiniApp>?,
    var cashback: List<NewCashback>?,


) : BaseRes()