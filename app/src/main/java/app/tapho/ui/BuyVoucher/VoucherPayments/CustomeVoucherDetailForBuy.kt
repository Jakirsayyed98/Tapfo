package app.tapho.ui.BuyVoucher.VoucherPayments

import com.google.gson.annotations.SerializedName

data class CustomeVoucherDetailForBuy(@SerializedName("voucher_id")val voucher_id: String,@SerializedName("denomination_id") val denomination_id: String,@SerializedName("qty") val qty: String)
