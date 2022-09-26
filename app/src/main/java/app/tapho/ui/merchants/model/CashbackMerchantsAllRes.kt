package app.tapho.ui.merchants.model

import app.tapho.network.BaseRes
import app.tapho.ui.model.CashbackMerchant
import app.tapho.ui.model.CashbackMerchantCategory

class CashbackMerchantsAllRes(
    var cashback_merchant_category: ArrayList<CashbackMerchantCategory>?,
    var `data`: ArrayList<CashbackMerchant>?,
) : BaseRes()